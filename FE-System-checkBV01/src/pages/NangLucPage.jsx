import { useEffect, useState, useMemo } from "react";
import * as nangLucService from "../services/nangLucService";
import * as dvktService from "../services/dichVuKyThuatService";
import * as bacSiService from "../services/bacSiService";

const BS_PAGE_SIZE = 5;
const NL_PAGE_SIZE = 5; 
const DVKT_PAGE_SIZE = 6;

const NangLucPage = () => {
  const [bacSiList, setBacSiList] = useState([]);
  const [selectedBacSi, setSelectedBacSi] = useState(null);
  const [nangLucList, setNangLucList] = useState([]);
  const [dvktList, setDvktList] = useState([]);

  const [search, setSearch] = useState(""); // Tìm bác sĩ
  const [nlSearch, setNlSearch] = useState(""); // TÌM NĂNG LỰC (DVKT)
  const [dvktSearch, setDvktSearch] = useState(""); // Tìm DVKT để thêm
  const [selectedParentId, setSelectedParentId] = useState("");

  const [bsPage, setBsPage] = useState(1);
  const [nlPage, setNlPage] = useState(1);
  const [dvktPage, setDvktPage] = useState(1);

  const [loading, setLoading] = useState(false);

  const [form, setForm] = useState({
    dvktId: "",
    vaiTro: "DOC_KQ",
  });

  // ================= LOAD DATA =================
  useEffect(() => {
    const load = async () => {
      try {
        const bsRes = await bacSiService.getAll();
        const dvktRes = await dvktService.getAll();
        setBacSiList(Array.isArray(bsRes) ? bsRes : bsRes?.data || []);
        setDvktList(Array.isArray(dvktRes) ? dvktRes : dvktRes?.data || []);
      } catch (err) {
        console.error("Lỗi load dữ liệu:", err);
      }
    };
    load();
  }, []);

  const loadNangLuc = async (bacSiId) => {
    try {
      const res = await nangLucService.getByBacSi(bacSiId);
      const list = Array.isArray(res) ? res : res?.data || [];
      setNangLucList(list);
      setNlPage(1);
      setNlSearch(""); // Reset search khi đổi bác sĩ
      setForm({ dvktId: "", vaiTro: "DOC_KQ" });
    } catch (err) {
      setNangLucList([]);
    }
  };

  const handleSelectBacSi = async (bacSi) => {
    if (selectedBacSi?.id === bacSi.id) {
      setSelectedBacSi(null);
      setNangLucList([]);
    } else {
      setSelectedBacSi(bacSi);
      await loadNangLuc(bacSi.id);
    }
  };

  // ================= LOGIC GỘP & LỌC NĂNG LỰC =================
  const filteredGroupedNangLuc = useMemo(() => {
    // 1. Gộp theo DVKT
    const groups = nangLucList.reduce((acc, item) => {
      if (!acc[item.dvktId]) {
        acc[item.dvktId] = {
          dvktId: item.dvktId,
          tenDvkt: item.tenDvkt,
          roles: []
        };
      }
      acc[item.dvktId].roles.push({ id: item.id, vaiTro: item.vaiTro });
      return acc;
    }, {});

    const result = Object.values(groups);

    // 2. Lọc theo từ khóa tìm kiếm (nlSearch)
    if (!nlSearch.trim()) return result;
    
    return result.filter(item => 
      item.tenDvkt.toLowerCase().includes(nlSearch.toLowerCase())
    );
  }, [nangLucList, nlSearch]);

  // ================= PHÂN TRANG =================
  const filteredBacSi = bacSiList.filter((b) =>
    b.hoTen?.toLowerCase().includes(search.toLowerCase())
  );

  useEffect(() => { setBsPage(1); }, [search]);
  useEffect(() => { setNlPage(1); }, [nlSearch]); // Reset trang năng lực khi tìm kiếm

  const totalBsPages = Math.ceil(filteredBacSi.length / BS_PAGE_SIZE) || 1;
  const pagedBacSi = filteredBacSi.slice((bsPage - 1) * BS_PAGE_SIZE, bsPage * BS_PAGE_SIZE);

  const totalNlPages = Math.ceil(filteredGroupedNangLuc.length / NL_PAGE_SIZE) || 1;
  const pagedGroupedNl = filteredGroupedNangLuc.slice((nlPage - 1) * NL_PAGE_SIZE, nlPage * NL_PAGE_SIZE);

  // ================= XỬ LÝ TREE DVKT (Cột 3) =================
  const dvktTree = useMemo(() => {
    const map = {};
    const roots = [];
    dvktList.forEach((item) => { map[item.id] = { ...item, children: [] }; });
    dvktList.forEach((item) => {
      if (item.dvktChaId) map[item.dvktChaId]?.children.push(map[item.id]);
      else roots.push(map[item.id]);
    });
    return roots;
  }, [dvktList]);

  const allChildren = useMemo(() => {
    return dvktTree.flatMap((parent) =>
      parent.children.map((child) => ({
        ...child,
        parentName: parent.tenDvkt,
        parentCode: parent.maDvkt,
      }))
    );
  }, [dvktTree]);

  const availableChildren = useMemo(() => {
    if (!selectedBacSi) return [];
    const roleMap = nangLucList.reduce((acc, item) => {
      if (!acc[item.dvktId]) acc[item.dvktId] = new Set();
      acc[item.dvktId].add(item.vaiTro);
      return acc;
    }, {});

    let source = selectedParentId 
      ? (dvktTree.find(p => p.id === Number(selectedParentId))?.children || [])
      : allChildren;

    return source
      .filter((child) => {
        const roles = roleMap[child.id];
        return !(roles?.has("DOC_KQ") && roles?.has("THUC_HIEN"));
      })
      .filter((child) =>
        (`${child.tenDvkt} ${child.maDvkt}`).toLowerCase().includes(dvktSearch.toLowerCase())
      )
      .sort((a, b) => a.tenDvkt.localeCompare(b.tenDvkt));
  }, [selectedParentId, dvktTree, allChildren, nangLucList, dvktSearch, selectedBacSi]);

  const totalDvktPages = Math.ceil(availableChildren.length / DVKT_PAGE_SIZE) || 1;
  const pagedDvkt = availableChildren.slice((dvktPage - 1) * DVKT_PAGE_SIZE, dvktPage * DVKT_PAGE_SIZE);

  // ================= HÀNH ĐỘNG CRUD =================
  const handleSubmit = async () => {
    if (!selectedBacSi || !form.dvktId) return alert("Vui lòng chọn đầy đủ thông tin");
    try {
      setLoading(true);
      await nangLucService.create({
        bacSiId: selectedBacSi.id,
        dvktId: Number(form.dvktId),
        vaiTro: form.vaiTro,
      });
      await loadNangLuc(selectedBacSi.id);
    } catch (err) { console.error(err); } 
    finally { setLoading(false); }
  };

  const handleDelete = async (id) => {
    if (!window.confirm("Xóa vai trò này?")) return;
    try {
      setLoading(true);
      await nangLucService.remove(id);
      await loadNangLuc(selectedBacSi.id);
    } catch (err) { console.error(err); }
    finally { setLoading(false); }
  };

  const handleToggleRole = async (item) => {
    const newRole = item.vaiTro === "DOC_KQ" ? "THUC_HIEN" : "DOC_KQ";
    const isConflict = nangLucList.some(n => n.dvktId === item.dvktId && n.vaiTro === newRole);
    if (isConflict) return alert("DVKT này đã có vai trò tương ứng");
    try {
      setLoading(true);
      await nangLucService.update(item.id, { ...item, vaiTro: newRole });
      await loadNangLuc(selectedBacSi.id);
    } catch (err) { console.error(err); }
    finally { setLoading(false); }
  };

  return (
    <div className="container" style={{ padding: '20px', fontFamily: 'sans-serif' }}>
      <h2 style={{ textAlign: 'center', color: '#333', marginBottom: '30px' }}>Quản lý năng lực chuyên môn</h2>

      <div style={{ display: "grid", gridTemplateColumns: "1fr 1.5fr 1fr", gap: 20 }}>
        
        {/* CỘT 1: BÁC SĨ */}
        <div style={{ border: '1px solid #ddd', padding: '15px', borderRadius: '8px', background: '#fff' }}>
          <h3 style={{ marginTop: 0, fontSize: '18px' }}>👨‍⚕️ Bác sĩ</h3>
          <input
            type="text"
            placeholder="Tìm bác sĩ..."
            value={search}
            onChange={(e) => setSearch(e.target.value)}
            style={{ marginBottom: 10, padding: 8, width: "100%", boxSizing: 'border-box', borderRadius: '4px', border: '1px solid #ccc' }}
          />
          {pagedBacSi.map((bs) => (
            <div
              key={bs.id}
              onClick={() => handleSelectBacSi(bs)}
              style={{
                padding: '10px',
                marginBottom: '5px',
                cursor: "pointer",
                borderRadius: '4px',
                background: selectedBacSi?.id === bs.id ? "#d0ebff" : "#f1f3f5",
                fontWeight: selectedBacSi?.id === bs.id ? "bold" : "normal"
              }}
            >
              {bs.hoTen}
            </div>
          ))}
          <div style={{ marginTop: 10 }}>
            <button disabled={bsPage === 1} onClick={() => setBsPage(p => p - 1)}>⬅</button>
            <span style={{ margin: '0 10px' }}>{bsPage}/{totalBsPages}</span>
            <button disabled={bsPage === totalBsPages} onClick={() => setBsPage(p => p + 1)}>➡</button>
          </div>
        </div>

        {/* CỘT 2: NĂNG LỰC (CÓ SEARCH DVKT) */}
        <div style={{ border: '1px solid #ddd', padding: '15px', borderRadius: '8px', background: '#fcfcfc' }}>
          <h3 style={{ marginTop: 0, fontSize: '18px' }}>
            {selectedBacSi ? `Năng lực: ${selectedBacSi.hoTen}` : "📋 Danh sách năng lực"}
          </h3>
          
          {/* THANH TÌM KIẾM NĂNG LỰC */}
          <input
            type="text"
            placeholder="Tìm tên dịch vụ đã gán..."
            value={nlSearch}
            onChange={(e) => setNlSearch(e.target.value)}
            disabled={!selectedBacSi}
            style={{ 
              marginBottom: 15, padding: 8, width: "100%", 
              boxSizing: 'border-box', borderRadius: '4px', 
              border: '1px solid #228be6', outline: 'none' 
            }}
          />
          
          {pagedGroupedNl.length === 0 && selectedBacSi && (
            <p style={{ color: '#888', textAlign: 'center' }}>
              {nlSearch ? "Không tìm thấy dịch vụ nào phù hợp." : "Chưa có dữ liệu năng lực."}
            </p>
          )}

          {pagedGroupedNl.map((group) => (
            <div key={group.dvktId} style={{
              padding: '12px',
              marginBottom: '10px',
              background: "#fff",
              border: '1px solid #e0e0e0',
              borderRadius: '6px',
              boxShadow: '0 2px 4px rgba(0,0,0,0.02)'
            }}>
              <div style={{ fontWeight: "bold", marginBottom: '8px', color: '#2c3e50', fontSize: '14px' }}>
                {group.tenDvkt}
              </div>
              <div style={{ display: "flex", gap: '8px', flexWrap: 'wrap' }}>
                {group.roles.map((r) => (
                  <div key={r.id} style={{
                    display: 'flex',
                    alignItems: 'center',
                    padding: '4px 10px',
                    borderRadius: '20px',
                    fontSize: '11px',
                    background: r.vaiTro === "DOC_KQ" ? "#e7f5ff" : "#f3f0ff",
                    color: r.vaiTro === "DOC_KQ" ? "#1971c2" : "#6741d9",
                    border: '1px solid currentColor'
                  }}>
                    <span 
                      style={{ cursor: 'pointer', marginRight: '5px', fontWeight: '600' }} 
                      onClick={() => handleToggleRole(r)}
                      title="Click để chuyển đổi vai trò"
                    >
                      {r.vaiTro === "DOC_KQ" ? "📖 Đọc KQ" : "🛠️ Thực hiện"}
                    </span>
                    <b 
                      style={{ cursor: 'pointer', color: '#ff6b6b', paddingLeft: '5px', fontSize: '14px' }} 
                      onClick={() => handleDelete(r.id)}
                    >✕</b>
                  </div>
                ))}
              </div>
            </div>
          ))}

          {totalNlPages > 1 && (
            <div style={{ marginTop: 15 }}>
              <button disabled={nlPage === 1} onClick={() => setNlPage(p => p - 1)}>⬅</button>
              <span style={{ margin: '0 10px' }}>{nlPage}/{totalNlPages}</span>
              <button disabled={nlPage === totalNlPages} onClick={() => setNlPage(p => p + 1)}>➡</button>
            </div>
          )}
        </div>

        {/* CỘT 3: FORM THÊM MỚI */}
        <div style={{ border: '1px solid #ddd', padding: '15px', borderRadius: '8px', background: '#fff' }}>
          <h3 style={{ marginTop: 0, fontSize: '18px' }}>➕ Thêm năng lực</h3>
          <select
            value={selectedParentId}
            onChange={(e) => { setSelectedParentId(e.target.value); setForm({ ...form, dvktId: "" }); }}
            style={{ width: "100%", marginBottom: 10, padding: 8, borderRadius: '4px' }}
            disabled={!selectedBacSi}
          >
            <option value="">-- Tất cả nhóm DVKT --</option>
            {dvktTree.map((p) => <option key={p.id} value={p.id}>{p.maDvkt} - {p.tenDvkt}</option>)}
          </select>

          <input
            type="text"
            placeholder="Tìm nhanh DVKT con..."
            value={dvktSearch}
            onChange={(e) => setDvktSearch(e.target.value)}
            style={{ width: "100%", marginBottom: 10, padding: 8, boxSizing: 'border-box', borderRadius: '4px', border: '1px solid #ccc' }}
            disabled={!selectedBacSi}
          />

          <select
            size="6"
            value={form.dvktId}
            onChange={(e) => setForm({ ...form, dvktId: e.target.value })}
            style={{ width: "100%", marginBottom: 10, padding: 5, borderRadius: '4px' }}
            disabled={!selectedBacSi}
          >
            <option value="">-- Chọn DVKT --</option>
            {pagedDvkt.map((c) => (
              <option key={c.id} value={c.id}>
                {selectedParentId ? "" : `${c.parentCode} > `}{c.maDvkt} - {c.tenDvkt}
              </option>
            ))}
          </select>

          <div style={{ marginBottom: 15 }}>
            <button disabled={dvktPage === 1} onClick={() => setDvktPage(p => p - 1)}>⬅</button>
            <span style={{ margin: '0 10px' }}>{dvktPage}/{totalDvktPages}</span>
            <button disabled={dvktPage === totalDvktPages} onClick={() => setDvktPage(p => p + 1)}>➡</button>
          </div>

          <select
            value={form.vaiTro}
            onChange={(e) => setForm({ ...form, vaiTro: e.target.value })}
            style={{ width: "100%", marginBottom: 15, padding: 8, borderRadius: '4px' }}
            disabled={!selectedBacSi}
          >
            <option value="DOC_KQ">Đọc kết quả</option>
            <option value="THUC_HIEN">Thực hiện</option>
          </select>

          <button
            onClick={handleSubmit}
            style={{ 
              width: "100%", padding: 12, cursor: "pointer", 
              background: loading ? '#ccc' : '#228be6', color: '#fff', 
              border: 'none', borderRadius: '4px', fontWeight: 'bold'
            }}
            disabled={!selectedBacSi || loading}
          >
            {loading ? "Đang xử lý..." : "XÁC NHẬN THÊM"}
          </button>
        </div>

      </div>
    </div>
  );
};

export default NangLucPage;