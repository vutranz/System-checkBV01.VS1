import { useEffect, useState, useMemo, useCallback } from "react";
import * as bacSiService from "../services/bacSiService";
import * as phanCongService from "../services/phanCongService";
import * as dvktService from "../services/dichVuKyThuatService";
import * as caLamViecService from "../services/caLamViecService";
import * as nangLucService from "../services/nangLucService";

const BS_PAGE_SIZE = 6;
const PC_PAGE_SIZE = 2; 

const PhanCongPage = () => {
  const [bacSiList, setBacSiList] = useState([]);
  const [selectedBacSi, setSelectedBacSi] = useState(null);
  const [phanCongList, setPhanCongList] = useState([]);
  const [dvktGroups, setDvktGroups] = useState([]);
  const [caLamViecList, setCaLamViecList] = useState([]);
  const [dvktByNangLuc, setDvktByNangLuc] = useState([]);

  const [search, setSearch] = useState(""); 
  const [pcSearch, setPcSearch] = useState(""); 
  const [dvktSearch, setDvktSearch] = useState(""); 
  const [filterDvktChaId, setFilterDvktChaId] = useState("");

  const [bsPage, setBsPage] = useState(1);
  const [pcPage, setPcPage] = useState(1);
  const [loading, setLoading] = useState(false);

  const [form, setForm] = useState({
    selectedDvktIds: [],
    selectedThus: [],
    selectedCaRoles: {}, 
  });

  // ================= 1. LOAD MASTER DATA =================
  useEffect(() => {
    const loadMaster = async () => {
      try {
        const [bs, allDvkt, ca] = await Promise.all([
          bacSiService.getAll(),
          dvktService.getAll(),
          caLamViecService.getAll(),
        ]);
        setBacSiList(Array.isArray(bs) ? bs : bs?.data || []);
        setCaLamViecList(Array.isArray(ca) ? ca : ca?.data || []);
        setDvktGroups(allDvkt.filter(d => !d.dvktChaId));
        if (bs.length > 0) setSelectedBacSi(bs[0]);
      } catch (err) { console.error("Lỗi load data:", err); }
    };
    loadMaster();
  }, []);

  // ================= 2. LOAD DATA THEO BÁC SĨ =================
  const loadDoctorData = useCallback(async (bacSiId) => {
    if (!bacSiId) return;
    try {
      setLoading(true);
      const [pc, nl] = await Promise.all([
        phanCongService.getByBacSi(bacSiId),
        nangLucService.getByBacSi(bacSiId)
      ]);
      setPhanCongList(Array.isArray(pc) ? pc : []);
      setDvktByNangLuc(Array.isArray(nl) ? nl : []);
      setPcPage(1);
      setForm({ selectedDvktIds: [], selectedThus: [], selectedCaRoles: {} });
    } catch (err) { 
      setPhanCongList([]);
      setDvktByNangLuc([]);
    } finally { setLoading(false); }
  }, []);

  useEffect(() => {
    loadDoctorData(selectedBacSi?.id);
  }, [selectedBacSi, loadDoctorData]);

  // ================= 3. LOGIC PHÂN TRANG & LỌC =================
  
  // Phân trang Bác sĩ (Cột 1)
  const filteredBacSi = useMemo(() => bacSiList.filter(b => b.hoTen?.toLowerCase().includes(search.toLowerCase())), [bacSiList, search]);
  const pagedBacSi = useMemo(() => filteredBacSi.slice((bsPage - 1) * BS_PAGE_SIZE, bsPage * BS_PAGE_SIZE), [filteredBacSi, bsPage]);
  const totalBsPages = Math.ceil(filteredBacSi.length / BS_PAGE_SIZE) || 1;

  // Khử trùng & Lọc Dịch vụ Năng lực (Cột 3)
  const uniqueNangLucOptions = useMemo(() => {
    const uniqueMap = new Map();
    dvktByNangLuc.forEach(item => {
      const id = item.dvktId || item.id || item.dichVuKyThuat?.id;
      const ten = item.tenDvkt || item.dichVuKyThuat?.tenDvkt;
      const ma = item.maDvkt || item.dichVuKyThuat?.maDvkt;
      const chaId = item.dvktChaId || item.dichVuKyThuat?.dvktChaId;
      if (id && !uniqueMap.has(id)) uniqueMap.set(id, { id, ten, ma, chaId });
    });
    return Array.from(uniqueMap.values()).filter(d => {
      const matchSearch = d.ten?.toLowerCase().includes(dvktSearch.toLowerCase()) || d.ma?.toLowerCase().includes(dvktSearch.toLowerCase());
      const matchCha = filterDvktChaId === "" ? true : Number(d.chaId) === Number(filterDvktChaId);
      return matchSearch && matchCha;
    });
  }, [dvktByNangLuc, dvktSearch, filterDvktChaId]);

  // Phân trang Lịch trực đã nhóm (Cột 2)
  const { pagedGroups, totalPcPages } = useMemo(() => {
    const filtered = phanCongList.filter(pc => 
      pc.tenDvkt?.toLowerCase().includes(pcSearch.toLowerCase()) || pc.thu?.toLowerCase().includes(pcSearch.toLowerCase())
    );
    const grouped = filtered.reduce((acc, pc) => {
      if (!acc[pc.tenDvkt]) acc[pc.tenDvkt] = {};
      if (!acc[pc.tenDvkt][pc.thu]) acc[pc.tenDvkt][pc.thu] = [];
      acc[pc.tenDvkt][pc.thu].push(pc);
      return acc;
    }, {});
    const keys = Object.keys(grouped);
    const pagedKeys = keys.slice((pcPage - 1) * PC_PAGE_SIZE, pcPage * PC_PAGE_SIZE);
    const pagedData = {};
    pagedKeys.forEach(k => pagedData[k] = grouped[k]);
    return { pagedGroups: pagedData, totalPcPages: Math.ceil(keys.length / PC_PAGE_SIZE) || 1 };
  }, [phanCongList, pcSearch, pcPage]);

  // ================= 4. HANDLERS =================
  const toggleSelection = (field, value) => {
    setForm(prev => ({
      ...prev,
      [field]: prev[field].includes(value) ? prev[field].filter(x => x !== value) : [...prev[field], value]
    }));
  };

  const toggleCaRole = (caId) => {
    setForm(prev => {
      const currentRoles = { ...prev.selectedCaRoles };
      if (!currentRoles[caId]) currentRoles[caId] = "DOC_KQ";
      else if (currentRoles[caId] === "DOC_KQ") currentRoles[caId] = "THUC_HIEN";
      else delete currentRoles[caId];
      return { ...prev, selectedCaRoles: currentRoles };
    });
  };

  const handleBulkSubmit = async () => {
    const { selectedDvktIds, selectedThus, selectedCaRoles } = form;
    const caIds = Object.keys(selectedCaRoles);
    if (!selectedBacSi || !selectedDvktIds.length || !selectedThus.length || !caIds.length) return alert("Thiếu thông tin!");
    try {
      setLoading(true);
      const reqs = selectedDvktIds.flatMap(dvId => 
        selectedThus.flatMap(thu => 
          caIds.map(caId => phanCongService.create({
            bacSiId: selectedBacSi.id, dvktId: Number(dvId), thu, caLamViecId: Number(caId), vaiTro: selectedCaRoles[caId]
          }))
        )
      );
      await Promise.all(reqs);
      alert("Thành công!");
      loadDoctorData(selectedBacSi.id);
    } catch (err) { alert("Lỗi lưu!"); } finally { setLoading(false); }
  };

  const handleDelete = async (id) => {
    if (window.confirm("Xóa phân công?")) {
      await phanCongService.remove(id);
      loadDoctorData(selectedBacSi.id);
    }
  };

  const styles = {
    colCard: { background: '#fff', borderRadius: '12px', padding: '15px', border: '1px solid #e0e0e0', display: 'flex', flexDirection: 'column', height: '650px' },
    paginationBtn: { padding: '5px 12px', cursor: 'pointer', background: '#fff', border: '1px solid #ddd', borderRadius: '4px', fontSize: '12px' },
    tag: (v) => ({
      background: v === "DOC_KQ" ? "#e7f5ff" : "#ebfbee", color: v === "DOC_KQ" ? "#1971c2" : "#2f9e44",
      border: `1px solid ${v === "DOC_KQ" ? "#a5d8ff" : "#b2f2bb"}`, padding: '2px 8px', borderRadius: '6px', fontSize: '11px', fontWeight: 'bold'
    })
  };

  return (
    <div style={{ padding: '20px', backgroundColor: '#f8f9fa', minHeight: '100vh', fontFamily: 'Segoe UI' }}>
      <h2 style={{ textAlign: 'center', marginBottom: '20px' }}>🗓️ Quản lý Phân công Bác sĩ</h2>

      <div style={{ display: "grid", gridTemplateColumns: "280px 1fr 340px", gap: '15px' }}>
        
        {/* CỘT 1: BÁC SĨ */}
        <div style={styles.colCard}>
          <h4>👨‍⚕️ Bác sĩ</h4>
          <input type="text" placeholder="Tìm kiếm..." style={{padding:'8px', marginBottom:'10px'}} onChange={e => {setSearch(e.target.value); setBsPage(1);}} />
          <div style={{ flex: 1, overflowY: 'auto' }}>
            {pagedBacSi.map(bs => (
              <div key={bs.id} onClick={() => setSelectedBacSi(bs)} style={{ padding: '10px', cursor: 'pointer', background: selectedBacSi?.id === bs.id ? "#409eff" : "#fff", color: selectedBacSi?.id === bs.id ? "#fff" : "#333", borderRadius: '6px', marginBottom: '5px', border: '1px solid #eee' }}>
                <b>{bs.hoTen}</b>
              </div>
            ))}
          </div>
          {/* PHÂN TRANG CỘT 1 */}
          <div style={{ display: 'flex', justifyContent: 'center', gap: '5px', paddingTop: '10px', borderTop: '1px solid #eee' }}>
            <button style={styles.paginationBtn} disabled={bsPage === 1} onClick={() => setBsPage(p => p - 1)}>◀</button>
            <span style={{ fontSize: '13px', alignSelf: 'center' }}>{bsPage}/{totalBsPages}</span>
            <button style={styles.paginationBtn} disabled={bsPage === totalBsPages} onClick={() => setBsPage(p => p + 1)}>▶</button>
          </div>
        </div>

        {/* CỘT 2: LỊCH TRỰC */}
        <div style={styles.colCard}>
          <div style={{ display: 'flex', justifyContent: 'space-between', marginBottom: '10px' }}>
            <h4 style={{margin:0}}>📋 Lịch: {selectedBacSi?.hoTen}</h4>
            <input type="text" placeholder="Lọc..." style={{padding:'4px 8px'}} onChange={e => {setPcSearch(e.target.value); setPcPage(1);}} />
          </div>
          <div style={{ flex: 1, overflowY: 'auto' }}>
            {Object.entries(pagedGroups).map(([ten, byThu]) => (
              <div key={ten} style={{ border: '1px solid #eee', borderRadius: '8px', marginBottom: '10px' }}>
                <div style={{ background: '#f8f9fa', padding: '6px 10px', fontWeight: 'bold', fontSize: '13px' }}>💉 {ten}</div>
                <div style={{ padding: '8px' }}>
                  {Object.entries(byThu).map(([thu, items]) => (
                    <div key={thu} style={{ display: 'flex', gap: '10px', marginBottom: '5px' }}>
                      <div style={{ width: '35px', fontWeight: 'bold', color: '#409eff', fontSize: '12px' }}>{thu}</div>
                      <div style={{ display: 'flex', gap: '5px', flexWrap: 'wrap' }}>
                        {items.map(pc => (
                          <div key={pc.id} style={styles.tag(pc.vaiTro)}>
                            {pc.tenCa}: {pc.vaiTro === "DOC_KQ" ? "Đọc" : "Làm"}
                            <span onClick={() => handleDelete(pc.id)} style={{ marginLeft: '5px', cursor: 'pointer' }}>×</span>
                          </div>
                        ))}
                      </div>
                    </div>
                  ))}
                </div>
              </div>
            ))}
          </div>
          {/* PHÂN TRANG CỘT 2 */}
          <div style={{ display: 'flex', justifyContent: 'center', gap: '10px', paddingTop: '10px', borderTop: '1px solid #eee' }}>
            <button style={styles.paginationBtn} disabled={pcPage === 1} onClick={() => setPcPage(p => p - 1)}>Trang trước</button>
            <span style={{ fontWeight: 'bold', alignSelf: 'center' }}>{pcPage} / {totalPcPages}</span>
            <button style={styles.paginationBtn} disabled={pcPage === totalPcPages} onClick={() => setPcPage(p => p + 1)}>Trang sau</button>
          </div>
        </div>

        {/* CỘT 3: FORM GÁN NHANH */}
        <div style={styles.colCard}>
          <h4 style={{color: '#d46b08'}}>➕ Gán theo Năng lực</h4>
          <div style={{ background: '#fffbe6', padding: '8px', borderRadius: '8px', marginBottom: '10px', border: '1px solid #ffe58f' }}>
            <select style={{ width: '100%', padding: '5px', marginBottom: '5px' }} onChange={e => setFilterDvktChaId(e.target.value)}>
              <option value="">-- Tất cả nhóm --</option>
              {dvktGroups.map(g => <option key={g.id} value={g.id}>{g.tenDvkt}</option>)}
            </select>
            <input type="text" placeholder="Tìm dịch vụ..." style={{width:'100%', padding:'5px', boxSizing:'border-box'}} onChange={e => setDvktSearch(e.target.value)} />
          </div>

          <div style={{ flex: 1, overflowY: 'auto', border: '1px solid #ddd', padding: '5px', borderRadius: '6px' }}>
            {uniqueNangLucOptions.map(d => (
              <div key={d.id} style={{ padding: '3px 0', borderBottom: '1px solid #f0f0f0', fontSize: '12px' }}>
                <input type="checkbox" checked={form.selectedDvktIds.includes(d.id)} onChange={() => toggleSelection('selectedDvktIds', d.id)} />
                <span style={{ marginLeft: '5px' }}>{d.ten}</span>
              </div>
            ))}
          </div>

          <div style={{ display: 'flex', flexWrap: 'wrap', gap: '4px', margin: '10px 0' }}>
            {["T2", "T3", "T4", "T5", "T6", "T7", "CN"].map(t => (
              <button key={t} onClick={() => toggleSelection('selectedThus', t)} style={{ padding: '4px 8px', borderRadius: '4px', border: '1px solid #ddd', background: form.selectedThus.includes(t) ? "#409eff" : "#fff", color: form.selectedThus.includes(t) ? "#fff" : "#333", fontSize: '11px' }}>{t}</button>
            ))}
          </div>

          <div style={{ marginBottom: '10px' }}>
            {caLamViecList.map(c => {
              const role = form.selectedCaRoles[c.id];
              return (
                <div key={c.id} onClick={() => toggleCaRole(c.id)} style={{ padding: '8px', borderRadius: '6px', cursor: 'pointer', marginBottom: '5px', border: '1px solid', background: role === "DOC_KQ" ? "#e7f5ff" : role === "THUC_HIEN" ? "#ebfbee" : "#fff", borderColor: role === "DOC_KQ" ? "#1971c2" : role === "THUC_HIEN" ? "#2f9e44" : "#ddd", display: 'flex', justifyContent: 'space-between', fontSize: '12px' }}>
                  <b>{c.tenCa}</b>
                  <span>{role === "DOC_KQ" ? "📖 Đọc" : role === "THUC_HIEN" ? "🛠️ Làm" : "Chọn..."}</span>
                </div>
              );
            })}
          </div>

          <button onClick={handleBulkSubmit} disabled={loading} style={{ width: '100%', padding: '12px', background: '#40c057', color: '#fff', border: 'none', borderRadius: '8px', fontWeight: 'bold', cursor: 'pointer' }}>
            {loading ? "ĐANG LƯU..." : "XÁC NHẬN GÁN"}
          </button>
        </div>

      </div>
    </div>
  );
};

export default PhanCongPage;