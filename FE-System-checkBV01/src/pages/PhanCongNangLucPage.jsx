import { useEffect, useState, useMemo } from "react";
import * as bacSiService from "../services/bacSiService";
import * as phanCongService from "../services/phanCongService";
import * as dvktService from "../services/dichVuKyThuatService";
import * as caLamViecService from "../services/caLamViecService";

const BS_PAGE_SIZE = 6;
const PC_PAGE_SIZE = 2; // Số lượng Dịch vụ hiển thị trên 1 trang ở cột giữa

const PhanCongPage = () => {
  const [bacSiList, setBacSiList] = useState([]);
  const [selectedBacSi, setSelectedBacSi] = useState(null);
  const [phanCongList, setPhanCongList] = useState([]);
  const [dvktList, setDvktList] = useState([]);
  const [caLamViecList, setCaLamViecList] = useState([]);

  const [search, setSearch] = useState(""); 
  const [pcSearch, setPcSearch] = useState(""); 
  const [dvktSearch, setDvktSearch] = useState(""); 
  const [filterDvktChaId, setFilterDvktChaId] = useState("");

  const [bsPage, setBsPage] = useState(1);
  const [pcPage, setPcPage] = useState(1); // State phân trang cho cột giữa
  const [loading, setLoading] = useState(false);

  const [form, setForm] = useState({
    thu: "T2",
    caLamViecId: "",
    dvktId: "",
    vaiTro: "DOC_KQ",
  });

  // ================= 1. LOAD DATA =================
  useEffect(() => {
    const loadMaster = async () => {
      try {
        const [bs, dvkt, ca] = await Promise.all([
          bacSiService.getAll(),
          dvktService.getAll(),
          caLamViecService.getAll(),
        ]);
        setBacSiList(Array.isArray(bs) ? bs : bs?.data || []);
        setDvktList(Array.isArray(dvkt) ? dvkt : dvkt?.data || []);
        setCaLamViecList(Array.isArray(ca) ? ca : ca?.data || []);
        if (bs.length > 0) setSelectedBacSi(bs[0]);
      } catch (err) { console.error("Lỗi load data:", err); }
    };
    loadMaster();
  }, []);

  const loadPhanCong = async (bacSiId) => {
    try {
      const list = await phanCongService.getByBacSi(bacSiId);
      setPhanCongList(Array.isArray(list) ? list : []);
      setPcPage(1); // Reset trang khi đổi bác sĩ
    } catch (err) { setPhanCongList([]); }
  };

  useEffect(() => {
    if (selectedBacSi) loadPhanCong(selectedBacSi.id);
  }, [selectedBacSi]);

  // ================= 2. LOGIC LỌC & NHÓM =================
  
  const filteredBacSi = bacSiList.filter(b => b.hoTen?.toLowerCase().includes(search.toLowerCase()));
  const pagedBacSi = filteredBacSi.slice((bsPage - 1) * BS_PAGE_SIZE, bsPage * BS_PAGE_SIZE);
  const totalBsPages = Math.ceil(filteredBacSi.length / BS_PAGE_SIZE) || 1;

  const filteredDvktOptions = useMemo(() => {
    return dvktList.filter(d => {
      const matchSearch = d.tenDvkt?.toLowerCase().includes(dvktSearch.toLowerCase()) || d.maDvkt?.toLowerCase().includes(dvktSearch.toLowerCase());
      const matchCha = filterDvktChaId === "" ? true : (filterDvktChaId === "null" ? !d.dvktChaId : d.dvktChaId === Number(filterDvktChaId));
      return matchSearch && matchCha;
    });
  }, [dvktList, dvktSearch, filterDvktChaId]);

  // LOGIC NHÓM VÀ PHÂN TRANG CỘT GIỮA
  const { pagedGroups, totalPcPages } = useMemo(() => {
    const filtered = phanCongList.filter(pc => 
      pc.tenDvkt?.toLowerCase().includes(pcSearch.toLowerCase()) || pc.thu?.toLowerCase().includes(pcSearch.toLowerCase())
    );

    // Nhóm toàn bộ
    const grouped = filtered.reduce((acc, pc) => {
      if (!acc[pc.tenDvkt]) acc[pc.tenDvkt] = {};
      if (!acc[pc.tenDvkt][pc.thu]) acc[pc.tenDvkt][pc.thu] = [];
      acc[pc.tenDvkt][pc.thu].push(pc);
      return acc;
    }, {});

    const groupKeys = Object.keys(grouped);
    const totalPages = Math.ceil(groupKeys.length / PC_PAGE_SIZE) || 1;
    
    // Cắt mảng theo trang
    const pagedKeys = groupKeys.slice((pcPage - 1) * PC_PAGE_SIZE, pcPage * PC_PAGE_SIZE);
    
    const pagedData = {};
    pagedKeys.forEach(key => {
      pagedData[key] = grouped[key];
    });

    return { pagedGroups: pagedData, totalPcPages: totalPages };
  }, [phanCongList, pcSearch, pcPage]);

  // ================= 3. HANDLERS =================
  const handleSubmit = async () => {
    if (!selectedBacSi || !form.dvktId || !form.caLamViecId) return alert("Thiếu thông tin!");
    try {
      setLoading(true);
      await phanCongService.create({ ...form, bacSiId: selectedBacSi.id, caLamViecId: Number(form.caLamViecId), dvktId: Number(form.dvktId) });
      loadPhanCong(selectedBacSi.id);
      setForm({ ...form, dvktId: "" });
    } catch (err) { alert("Lỗi phân công!"); } finally { setLoading(false); }
  };

  const handleDelete = async (id) => {
    if (!window.confirm("Xóa phân công này?")) return;
    await phanCongService.remove(id);
    loadPhanCong(selectedBacSi.id);
  };

  // ================= 4. RENDER =================
  const styles = {
    colCard: { background: '#fff', borderRadius: '12px', padding: '15px', border: '1px solid #e0e0e0', boxShadow: '0 2px 10px rgba(0,0,0,0.05)', height: '100%' },
    tag: (vaiTro) => ({
      background: vaiTro === "DOC_KQ" ? "#e7f5ff" : "#ebfbee",
      color: vaiTro === "DOC_KQ" ? "#1971c2" : "#2f9e44",
      border: `1px solid ${vaiTro === "DOC_KQ" ? "#a5d8ff" : "#b2f2bb"}`,
      padding: '3px 8px', borderRadius: '6px', fontSize: '12px', display: 'flex', alignItems: 'center', gap: '5px'
    }),
    paginationBtn: { padding: '5px 10px', cursor: 'pointer', background: '#fff', border: '1px solid #ddd', borderRadius: '4px' }
  };

  return (
    <div style={{ padding: '25px', backgroundColor: '#f8f9fa', minHeight: '100vh', fontFamily: 'Segoe UI' }}>
      <h2 style={{ marginBottom: '25px', color: '#333', textAlign: 'center' }}>🗓️ Hệ thống Điều phối Phân công</h2>

      <div style={{ display: "grid", gridTemplateColumns: "280px 1fr 320px", gap: '20px', alignItems: 'stretch' }}>
        
        {/* CỘT 1 */}
        <div style={styles.colCard}>
          <h4 style={{ margin: '0 0 15px 0' }}>👨‍⚕️ Bác sĩ</h4>
          <input type="text" placeholder="Tìm tên bác sĩ..." value={search} onChange={(e) => {setSearch(e.target.value); setBsPage(1);}}
            style={{ width: '100%', padding: '10px', borderRadius: '8px', border: '1px solid #ddd', marginBottom: '15px', boxSizing: 'border-box' }} />
          
          <div style={{ minHeight: '400px' }}>
            {pagedBacSi.map(bs => (
                <div key={bs.id} onClick={() => setSelectedBacSi(bs)}
                style={{ padding: '12px', borderRadius: '8px', cursor: 'pointer', marginBottom: '8px',
                    background: selectedBacSi?.id === bs.id ? "#409eff" : "#fff",
                    color: selectedBacSi?.id === bs.id ? "#fff" : "#333",
                    border: `1px solid ${selectedBacSi?.id === bs.id ? "#409eff" : "#eee"}`
                }}>
                <b>{bs.hoTen}</b>
                <div style={{ fontSize: '11px', opacity: 0.8 }}>Mã: {bs.maBacSi}</div>
                </div>
            ))}
          </div>

          <div style={{ textAlign: 'center', marginTop: '15px', display: 'flex', justifyContent: 'center', gap: '5px' }}>
            <button style={styles.paginationBtn} disabled={bsPage === 1} onClick={() => setBsPage(p => p - 1)}>◀</button>
            <span style={{ alignSelf: 'center', fontSize: '13px' }}>{bsPage}/{totalBsPages}</span>
            <button style={styles.paginationBtn} disabled={bsPage === totalBsPages} onClick={() => setBsPage(p => p + 1)}>▶</button>
          </div>
        </div>

        {/* CỘT 2: PHÂN TRANG CHO DỊCH VỤ ĐÃ NHÓM */}
        <div style={styles.colCard}>
          <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '15px' }}>
            <h4 style={{ margin: 0 }}>📋 Lịch: <span style={{ color: '#409eff' }}>{selectedBacSi?.hoTen}</span></h4>
            <input type="text" placeholder="Lọc nhanh..." value={pcSearch} onChange={e => {setPcSearch(e.target.value); setPcPage(1);}}
              style={{ padding: '8px', borderRadius: '6px', border: '1px solid #ddd', fontSize: '13px' }} />
          </div>

          <div style={{ minHeight: '500px' }}>
            {Object.entries(pagedGroups).length === 0 ? <p style={{ textAlign: 'center', color: '#999', marginTop: '50px' }}>Chưa có dữ liệu phân công.</p> :
              Object.entries(pagedGroups).map(([tenDvkt, byThu]) => (
                <div key={tenDvkt} style={{ border: '1px solid #eee', borderRadius: '10px', marginBottom: '15px', overflow: 'hidden' }}>
                  <div style={{ background: '#f8f9fa', padding: '10px 15px', fontWeight: 'bold', fontSize: '14px', borderBottom: '1px solid #eee', color: '#555' }}>💉 {tenDvkt}</div>
                  <div style={{ padding: '10px' }}>
                    {Object.entries(byThu).map(([thu, items]) => (
                      <div key={thu} style={{ display: 'flex', alignItems: 'center', gap: '15px', padding: '8px 0', borderBottom: '1px solid #f9f9f9' }}>
                        <div style={{ minWidth: '45px', fontWeight: 'bold', color: '#409eff' }}>{thu}</div>
                        <div style={{ display: 'flex', flexWrap: 'wrap', gap: '8px' }}>
                          {items.map(pc => (
                            <div key={pc.id} style={styles.tag(pc.vaiTro)}>
                              <b>{pc.tenCa}</b>: {pc.vaiTro === "DOC_KQ" ? "Đọc" : "Làm"}
                              <span onClick={() => handleDelete(pc.id)} style={{ cursor: 'pointer', fontWeight: 'bold', marginLeft: '5px' }}>×</span>
                            </div>
                          ))}
                        </div>
                      </div>
                    ))}
                  </div>
                </div>
              ))
            }
          </div>

          {/* Điều khiển phân trang cột giữa */}
          <div style={{ textAlign: 'center', marginTop: '10px', display: 'flex', justifyContent: 'center', gap: '10px' }}>
             <button style={styles.paginationBtn} disabled={pcPage === 1} onClick={() => setPcPage(p => p - 1)}>Trang trước</button>
             <span style={{ alignSelf: 'center', fontWeight: 'bold' }}>{pcPage} / {totalPcPages}</span>
             <button style={styles.paginationBtn} disabled={pcPage === totalPcPages} onClick={() => setPcPage(p => p + 1)}>Trang sau</button>
          </div>
        </div>

        {/* CỘT 3 */}
        <div style={styles.colCard}>
            <h4 style={{ margin: '0 0 15px 0' }}>➕ Thêm Phân công</h4>
            {/* ... Giữ nguyên phần Form ... */}
            <div style={{ background: '#f9f9f9', padding: '12px', borderRadius: '8px', marginBottom: '15px' }}>
                <select style={{ width: '100%', padding: '8px', marginBottom: '10px' }} value={filterDvktChaId} onChange={e => setFilterDvktChaId(e.target.value)}>
                    <option value="">-- Tất cả nhóm --</option>
                    <option value="null">Nhóm Gốc</option>
                    {dvktList.filter(d => !d.dvktChaId).map(d => <option key={d.id} value={d.id}>{d.tenDvkt}</option>)}
                </select>
                <input type="text" placeholder="Tìm DV nhanh..." value={dvktSearch} onChange={e => setDvktSearch(e.target.value)} style={{ width: '100%', padding: '8px', boxSizing: 'border-box' }} />
            </div>

            <label style={{ fontSize: '12px', fontWeight: 'bold' }}>Dịch vụ:</label>
            <select style={{ width: '100%', padding: '10px', marginBottom: '15px', borderRadius: '6px' }} value={form.dvktId} onChange={e => setForm({...form, dvktId: e.target.value})}>
                <option value="">-- Chọn DVKT ({filteredDvktOptions.length}) --</option>
                {filteredDvktOptions.map(d => <option key={d.id} value={d.id}>{d.tenDvkt}</option>)}
            </select>

            <div style={{ display: 'flex', gap: '10px', marginBottom: '15px' }}>
                <div style={{ flex: 1 }}>
                <label style={{ fontSize: '12px', fontWeight: 'bold' }}>Thứ:</label>
                <select style={{ width: '100%', padding: '10px' }} value={form.thu} onChange={e => setForm({...form, thu: e.target.value})}>
                    {["T2","T3","T4","T5","T6","T7","CN"].map(t => <option key={t} value={t}>{t}</option>)}
                </select>
                </div>
                <div style={{ flex: 1 }}>
                <label style={{ fontSize: '12px', fontWeight: 'bold' }}>Ca:</label>
                <select style={{ width: '100%', padding: '10px' }} value={form.caLamViecId} onChange={e => setForm({...form, caLamViecId: e.target.value})}>
                    <option value="">Ca</option>
                    {caLamViecList.map(c => <option key={c.id} value={c.id}>{c.tenCa}</option>)}
                </select>
                </div>
            </div>

            <label style={{ fontSize: '12px', fontWeight: 'bold' }}>Vai trò:</label>
            <select style={{ width: '100%', padding: '10px', marginBottom: '20px' }} value={form.vaiTro} onChange={e => setForm({...form, vaiTro: e.target.value})}>
                <option value="DOC_KQ">Đọc kết quả</option>
                <option value="THUC_HIEN">Thực hiện</option>
            </select>

            <button onClick={handleSubmit} disabled={loading} style={{ width: '100%', padding: '12px', background: '#40c057', color: '#fff', border: 'none', borderRadius: '8px', fontWeight: 'bold', cursor: 'pointer' }}>
                {loading ? "ĐANG LƯU..." : "LƯU PHÂN CÔNG"}
            </button>
        </div>
      </div>
    </div>
  );
};

export default PhanCongPage;