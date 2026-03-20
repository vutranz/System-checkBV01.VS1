import { useEffect, useState } from "react";
import * as service from "../services/bacSiChuyenKhoaService";
import * as bacSiService from "../services/bacSiService";
import * as chuyenKhoaService from "../services/chuyenKhoaService";

const initialForm = {
  bacSiId: "",
  chuyenKhoaId: "",
  laChuyenKhoaChinh: false,
};

const PAGE_SIZE = 10;

const BacSiChuyenKhoaPage = () => {
  const [list, setList] = useState([]);
  const [bacSiList, setBacSiList] = useState([]);
  const [chuyenKhoaList, setChuyenKhoaList] = useState([]);

  const [form, setForm] = useState(initialForm);
  const [editingId, setEditingId] = useState(null);
  const [currentPage, setCurrentPage] = useState(1);
  const [loading, setLoading] = useState(false);

  // ================= LOAD DATA =================
  const loadData = async () => {
    const res = await service.getAll();
    setList(res || []);
    setCurrentPage(1);
  };

  const loadOptions = async () => {
    const [bs, ck] = await Promise.all([
      bacSiService.getAll(),
      chuyenKhoaService.getAll()
    ]);
    setBacSiList(bs || []);
    setChuyenKhoaList(ck || []);
  };

  useEffect(() => {
    loadData();
    loadOptions();
  }, []);

  // ================= ACTIONS =================
  const handleSubmit = async () => {
    if (!form.bacSiId || !form.chuyenKhoaId) {
      alert("Vui lòng chọn đầy đủ Bác sĩ và Chuyên khoa");
      return;
    }

    setLoading(true);
    const payload = {
      bacSiId: Number(form.bacSiId),
      chuyenKhoaId: Number(form.chuyenKhoaId),
      laChuyenKhoaChinh: form.laChuyenKhoaChinh,
    };

    try {
      if (editingId) {
        await service.update(editingId, payload);
      } else {
        await service.create(payload);
      }
      handleReset();
      loadData();
    } catch (err) {
      alert("Lỗi: Có thể bác sĩ này đã được phân vào chuyên khoa này rồi");
    } finally {
      setLoading(false);
    }
  };

  const handleEdit = (item) => {
    setForm({
      bacSiId: item.bacSiId,
      chuyenKhoaId: item.chuyenKhoaId,
      laChuyenKhoaChinh: item.laChuyenKhoaChinh,
    });
    setEditingId(item.id);
    window.scrollTo({ top: 0, behavior: 'smooth' });
  };

  const handleDelete = async (id) => {
    if (!window.confirm("Gỡ bỏ liên kết chuyên khoa của bác sĩ này?")) return;
    await service.remove(id);
    loadData();
  };

  const handleReset = () => {
    setForm(initialForm);
    setEditingId(null);
  };

  // ================= PAGINATION =================
  const totalPages = Math.ceil(list.length / PAGE_SIZE);
  const startIndex = (currentPage - 1) * PAGE_SIZE;
  const currentData = list.slice(startIndex, startIndex + PAGE_SIZE);

  // ================= STYLES =================
  const styles = {
    container: { padding: '30px', maxWidth: '1000px', margin: '0 auto', fontFamily: "'Inter', sans-serif", color: '#334155' },
    header: { fontSize: '24px', fontWeight: '700', marginBottom: '25px', color: '#1e293b', display: 'flex', alignItems: 'center', gap: '10px' },
    card: { background: '#fff', padding: '24px', borderRadius: '16px', boxShadow: '0 10px 15px -3px rgba(0,0,0,0.1)', marginBottom: '30px', border: '1px solid #f1f5f9' },
    formRow: { display: 'flex', gap: '20px', flexWrap: 'wrap', alignItems: 'flex-end' },
    inputGroup: { display: 'flex', flexDirection: 'column', gap: '8px', flex: '1', minWidth: '200px' },
    label: { fontSize: '14px', fontWeight: '600', color: '#64748b' },
    select: { padding: '12px', borderRadius: '10px', border: '1px solid #e2e8f0', fontSize: '15px', outline: 'none', background: '#f8fafc' },
    checkboxContainer: { display: 'flex', alignItems: 'center', gap: '10px', padding: '12px', cursor: 'pointer', userSelect: 'none' },
    btnSubmit: { padding: '12px 24px', borderRadius: '10px', border: 'none', color: '#fff', fontWeight: '600', cursor: 'pointer', transition: '0.2s', alignSelf: 'flex-end' },
    table: { width: '100%', borderCollapse: 'separate', borderSpacing: '0 8px' },
    th: { padding: '12px 20px', textAlign: 'left', fontSize: '13px', color: '#94a3b8', textTransform: 'uppercase', letterSpacing: '0.05em' },
    tr: { background: '#fff', boxShadow: '0 2px 4px rgba(0,0,0,0.02)', borderRadius: '12px' },
    td: { padding: '16px 20px', fontSize: '15px', borderTop: '1px solid #f1f5f9', borderBottom: '1px solid #f1f5f9' },
    mainBadge: { background: '#eff6ff', color: '#2563eb', padding: '4px 12px', borderRadius: '20px', fontSize: '12px', fontWeight: '700', border: '1px solid #dbeafe' },
    subBadge: { color: '#94a3b8', fontSize: '12px' },
    pageBtn: (active) => ({
      width: '35px', height: '35px', borderRadius: '8px', border: 'none', margin: '0 4px', cursor: 'pointer',
      background: active ? '#2563eb' : '#fff', color: active ? '#fff' : '#64748b', fontWeight: '600', boxShadow: '0 1px 3px rgba(0,0,0,0.1)'
    })
  };

  return (
    <div style={styles.container}>
      <h2 style={styles.header}>🔗 Phân chuyên khoa cho Bác sĩ</h2>

      {/* FORM CARD */}
      <div style={styles.card}>
        <div style={styles.formRow}>
          <div style={styles.inputGroup}>
            <label style={styles.label}>Chọn Bác sĩ</label>
            <select
              style={styles.select}
              value={form.bacSiId}
              onChange={(e) => setForm({ ...form, bacSiId: e.target.value })}
            >
              <option value="">-- Chọn bác sĩ --</option>
              {bacSiList.map((bs) => (
                <option key={bs.id} value={bs.id}>{bs.hoTen}</option>
              ))}
            </select>
          </div>

          <div style={styles.inputGroup}>
            <label style={styles.label}>Chọn Chuyên khoa</label>
            <select
              style={styles.select}
              value={form.chuyenKhoaId}
              onChange={(e) => setForm({ ...form, chuyenKhoaId: e.target.value })}
            >
              <option value="">-- Chọn chuyên khoa --</option>
              {chuyenKhoaList.map((ck) => (
                <option key={ck.id} value={ck.id}>{ck.tenChuyenKhoa}</option>
              ))}
            </select>
          </div>

          <div style={{...styles.inputGroup, flex: '0 0 auto'}}>
            <label style={styles.checkboxContainer}>
              <input
                type="checkbox"
                style={{ width: '18px', height: '18px' }}
                checked={form.laChuyenKhoaChinh}
                onChange={(e) => setForm({ ...form, laChuyenKhoaChinh: e.target.checked })}
              />
              <span style={styles.label}>Chuyên khoa chính</span>
            </label>
          </div>

          <button
            style={{ ...styles.btnSubmit, background: editingId ? '#f59e0b' : '#2563eb' }}
            onClick={handleSubmit}
            disabled={loading}
          >
            {loading ? "..." : editingId ? "Cập nhật" : "Gán liên kết"}
          </button>
          {editingId && (
            <button style={{...styles.btnSubmit, background: '#ef4444'}} onClick={handleReset}>Hủy</button>
          )}
        </div>
      </div>

      {/* DATA TABLE */}
      <table style={styles.table}>
        <thead>
          <tr>
            <th style={styles.th}>Bác sĩ</th>
            <th style={styles.th}>Chuyên khoa</th>
            <th style={styles.th}>Loại</th>
            <th style={{ ...styles.th, textAlign: 'right' }}>Hành động</th>
          </tr>
        </thead>
        <tbody>
          {currentData.map((item) => (
            <tr key={item.id} style={styles.tr}>
              <td style={{ ...styles.td, fontWeight: '600', borderRadius: '12px 0 0 12px' }}>{item.tenBacSi}</td>
              <td style={styles.td}>{item.tenChuyenKhoa}</td>
              <td style={styles.td}>
                {item.laChuyenKhoaChinh ? (
                  <span style={styles.mainBadge}>Chuyên khoa chính</span>
                ) : (
                  <span style={styles.subBadge}>Chuyên khoa phụ</span>
                )}
              </td>
              <td style={{ ...styles.td, textAlign: 'right', borderRadius: '0 12px 12px 0' }}>
                <button 
                  onClick={() => handleEdit(item)}
                  style={{ background: 'none', border: 'none', color: '#2563eb', cursor: 'pointer', fontWeight: '600' }}
                >
                  Sửa
                </button>
                <button 
                  onClick={() => handleDelete(item.id)}
                  style={{ background: 'none', border: 'none', color: '#ef4444', cursor: 'pointer', fontWeight: '600', marginLeft: '15px' }}
                >
                  Xoá
                </button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>

      {/* PAGINATION */}
      {totalPages > 1 && (
        <div style={{ marginTop: '25px', textAlign: 'center' }}>
          {Array.from({ length: totalPages }, (_, index) => (
            <button
              key={index}
              onClick={() => setCurrentPage(index + 1)}
              style={styles.pageBtn(currentPage === index + 1)}
            >
              {index + 1}
            </button>
          ))}
        </div>
      )}
    </div>
  );
};

export default BacSiChuyenKhoaPage;