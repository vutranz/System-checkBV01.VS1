import { useEffect, useState, useMemo } from "react";
import * as service from "../services/lichLamViecTheoThuService";
import * as bacSiService from "../services/bacSiService";
import * as caLamViecService from "../services/caLamViecService";

const THU_OPTIONS = ["CN", "T2", "T3", "T4", "T5", "T6", "T7"];
const PAGE_SIZE = 6;

const initialForm = {
  bacSiId: "",
  thu: "",
  caLamViecId: "",
};

const LichLamViecTheoThuPage = () => {
  const [list, setList] = useState([]);
  const [bacSiList, setBacSiList] = useState([]);
  const [caList, setCaList] = useState([]);
  const [expandedBacSi, setExpandedBacSi] = useState(null);
  const [currentPage, setCurrentPage] = useState(1);
  const [search, setSearch] = useState("");
  const [form, setForm] = useState(initialForm);
  const [editingId, setEditingId] = useState(null);

  const loadData = async () => {
    const res = await service.getAll();
    setList(res || []);
  };

  const loadOptions = async () => {
    const [bs, ca] = await Promise.all([bacSiService.getAll(), caLamViecService.getAll()]);
    setBacSiList(bs || []);
    setCaList(ca || []);
  };

  useEffect(() => {
    loadData();
    loadOptions();
  }, []);

  const groupedData = useMemo(() => {
    const result = {};
    list.forEach((item) => {
      if (!result[item.bacSiId]) {
        result[item.bacSiId] = { bacSiId: item.bacSiId, tenBacSi: item.tenBacSi, thuMap: {} };
      }
      if (!result[item.bacSiId].thuMap[item.thu]) {
        result[item.bacSiId].thuMap[item.thu] = [];
      }
      result[item.bacSiId].thuMap[item.thu].push(item);
    });
    return Object.values(result);
  }, [list]);

  const filteredDoctors = useMemo(() => {
    return groupedData.filter((bs) => bs.tenBacSi.toLowerCase().includes(search.toLowerCase()));
  }, [groupedData, search]);

  useEffect(() => { setCurrentPage(1); }, [search]);

  const totalPages = Math.ceil(filteredDoctors.length / PAGE_SIZE);
  const paginatedData = useMemo(() => {
    const start = (currentPage - 1) * PAGE_SIZE;
    return filteredDoctors.slice(start, start + PAGE_SIZE);
  }, [filteredDoctors, currentPage]);

  const handleSubmit = async () => {
    if (!form.bacSiId || !form.thu || !form.caLamViecId) {
      alert("Vui lòng nhập đầy đủ thông tin");
      return;
    }
    const payload = { bacSiId: Number(form.bacSiId), thu: form.thu, caLamViecId: Number(form.caLamViecId) };
    if (editingId) await service.update(editingId, payload);
    else await service.create(payload);
    handleReset();
    loadData();
  };

  const handleReset = () => { setForm(initialForm); setEditingId(null); };
  const handleDelete = async (id) => { if (window.confirm("Xoá lịch này?")) { await service.remove(id); loadData(); } };
  const handleEdit = (item) => { setForm({ bacSiId: item.bacSiId, thu: item.thu, caLamViecId: item.caLamViecId }); setEditingId(item.id); window.scrollTo({ top: 0, behavior: 'smooth' }); };
  const toggleExpand = (id) => { setExpandedBacSi((prev) => (prev === id ? null : id)); };

  // --- Theme Styles ---
  const styles = {
    container: { padding: '40px', maxWidth: '1100px', margin: '0 auto', fontFamily: "'Inter', sans-serif", color: '#2c3e50' },
    headerSection: { borderBottom: '2px solid #edf2f7', marginBottom: '30px', paddingBottom: '15px', display: 'flex', justifyContent: 'space-between', alignItems: 'flex-end' },
    title: { margin: 0, fontSize: '24px', color: '#1a202c', fontWeight: '800' },
    formCard: { background: '#ffffff', borderRadius: '12px', padding: '24px', boxShadow: '0 4px 6px -1px rgba(0,0,0,0.1)', border: '1px solid #e2e8f0', marginBottom: '30px' },
    inputRow: { display: 'flex', gap: '12px', marginBottom: '15px' },
    select: { flex: 1, padding: '12px', borderRadius: '8px', border: '1px solid #cbd5e0', outline: 'none', backgroundColor: '#fff' },
    btnPrimary: { background: '#2563eb', color: '#fff', padding: '12px 24px', borderRadius: '8px', border: 'none', fontWeight: 'bold', cursor: 'pointer' },
    searchInput: { padding: '10px 15px', borderRadius: '8px', border: '1px solid #cbd5e0', width: '250px', outline: 'none' },
    
    // Grid & Card
    grid: { display: "grid", gridTemplateColumns: "repeat(auto-fill, minmax(300px, 1fr))", gap: '20px' },
    docCard: { background: '#fff', borderRadius: '12px', border: '1px solid #e2e8f0', padding: '20px', transition: '0.3s' },
    docName: { fontSize: '18px', fontWeight: '700', color: '#1e293b', cursor: 'pointer', display: 'flex', justifyContent: 'space-between' },
    
    // Badge & List
    thuRow: { marginTop: '12px', padding: '10px', background: '#f8fafc', borderRadius: '8px' },
    thuLabel: { fontWeight: '700', color: '#64748b', fontSize: '13px', marginRight: '8px' },
    caBadge: { 
      display: 'inline-flex', alignItems: 'center', background: '#eff6ff', color: '#2563eb', 
      padding: '4px 10px', borderRadius: '6px', fontSize: '13px', fontWeight: '600', margin: '2px' 
    },
    actionIcon: { marginLeft: '6px', cursor: 'pointer', opacity: 0.6 },
    
    // Pagination
    pageBtn: { padding: '8px 16px', borderRadius: '6px', border: '1px solid #e2e8f0', cursor: 'pointer', fontWeight: 'bold' }
  };

  return (
    <div style={styles.container}>
      <div style={styles.headerSection}>
        <div>
          <h2 style={styles.title}>📅 Lịch Làm Việc Theo Thứ</h2>
          <p style={{ color: '#718096', marginTop: '5px' }}>Quản lý phân ca trực cố định cho Bác sĩ</p>
        </div>
        <input 
          style={styles.searchInput} 
          placeholder="🔍 Tìm tên bác sĩ..." 
          value={search} 
          onChange={(e) => setSearch(e.target.value)} 
        />
      </div>

      {/* FORM */}
      <div style={styles.formCard}>
        <div style={styles.inputRow}>
          <select style={styles.select} value={form.bacSiId} onChange={(e) => setForm({ ...form, bacSiId: e.target.value })}>
            <option value="">-- Chọn bác sĩ --</option>
            {bacSiList.map((bs) => <option key={bs.id} value={bs.id}>{bs.hoTen}</option>)}
          </select>

          <select style={styles.select} value={form.thu} onChange={(e) => setForm({ ...form, thu: e.target.value })}>
            <option value="">-- Chọn thứ --</option>
            {THU_OPTIONS.map((t) => <option key={t} value={t}>{t}</option>)}
          </select>

          <select style={styles.select} value={form.caLamViecId} onChange={(e) => setForm({ ...form, caLamViecId: e.target.value })}>
            <option value="">-- Chọn ca trực --</option>
            {caList.map((ca) => <option key={ca.id} value={ca.id}>{ca.tenCa}</option>)}
          </select>
        </div>

        <div style={{ display: 'flex', gap: '10px', justifyContent: 'flex-end' }}>
          <button style={{ ...styles.btnPrimary, background: '#f8fafc', color: '#475569', border: '1px solid #cbd5e0' }} onClick={handleReset}>Làm mới</button>
          <button style={{ ...styles.btnPrimary, background: editingId ? '#ed8936' : '#2563eb' }} onClick={handleSubmit}>
            {editingId ? "Cập nhật lịch" : "Thêm vào lịch"}
          </button>
        </div>
      </div>

      {/* GRID CARDS */}
      <div style={styles.grid}>
        {paginatedData.map((bacSi) => (
          <div key={bacSi.bacSiId} style={{ ...styles.docCard, borderColor: expandedBacSi === bacSi.bacSiId ? '#2563eb' : '#e2e8f0' }}>
            <div style={styles.docName} onClick={() => toggleExpand(bacSi.bacSiId)}>
              <span>👨‍⚕️ {bacSi.tenBacSi}</span>
              <span style={{ fontSize: '12px', color: '#94a3b8' }}>{expandedBacSi === bacSi.bacSiId ? '▲' : '▼'}</span>
            </div>

            {expandedBacSi === bacSi.bacSiId && (
              <div style={{ marginTop: '15px' }}>
                {Object.entries(bacSi.thuMap).sort().map(([thu, items]) => (
                  <div key={thu} style={styles.thuRow}>
                    <span style={styles.thuLabel}>{thu}:</span>
                    {items.map((ca) => (
                      <span key={ca.id} style={styles.caBadge}>
                        {ca.tenCa}
                        <b style={{...styles.actionIcon, color: '#ed8936'}} onClick={() => handleEdit(ca)}>✎</b>
                        <b style={{...styles.actionIcon, color: '#e11d48'}} onClick={() => handleDelete(ca.id)}>×</b>
                      </span>
                    ))}
                  </div>
                ))}
              </div>
            )}
          </div>
        ))}
      </div>

      {/* PAGINATION */}
      {totalPages > 1 && (
        <div style={{ marginTop: '30px', display: 'flex', gap: '8px', justifyContent: 'center' }}>
          {Array.from({ length: totalPages }).map((_, i) => (
            <button
              key={i}
              onClick={() => setCurrentPage(i + 1)}
              style={{ 
                ...styles.pageBtn, 
                backgroundColor: currentPage === i + 1 ? '#2563eb' : '#fff',
                color: currentPage === i + 1 ? '#fff' : '#475569'
              }}
            >
              {i + 1}
            </button>
          ))}
        </div>
      )}
    </div>
  );
};

export default LichLamViecTheoThuPage;