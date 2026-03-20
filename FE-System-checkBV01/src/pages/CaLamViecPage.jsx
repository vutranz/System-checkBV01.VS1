import { useEffect, useState } from "react";
import * as caLamViecService from "../services/caLamViecService";

const initialForm = {
  tenCa: "",
  gioBatDau: "",
  gioKetThuc: "",
  hoatDong: true,
};

const CaLamViecPage = () => {
  const [list, setList] = useState([]);
  const [form, setForm] = useState(initialForm);
  const [editingId, setEditingId] = useState(null);
  const [loading, setLoading] = useState(false);

  const loadData = async () => {
    try {
      const data = await caLamViecService.getAll();
      setList(data || []);
    } catch (err) {
      console.error(err);
    }
  };

  useEffect(() => {
    loadData();
  }, []);

  const handleSubmit = async () => {
    if (!form.tenCa || !form.gioBatDau || !form.gioKetThuc) {
      alert("Vui lòng nhập đầy đủ thông tin!");
      return;
    }
    setLoading(true);
    try {
      if (editingId) {
        await caLamViecService.update(editingId, form);
      } else {
        await caLamViecService.create(form);
      }
      handleReset();
      loadData();
    } catch (err) {
      alert("Có lỗi xảy ra");
    } finally {
      setLoading(false);
    }
  };

  const handleEdit = async (id) => {
    const data = await caLamViecService.getById(id);
    setForm(data);
    setEditingId(id);
    window.scrollTo({ top: 0, behavior: 'smooth' });
  };

  const handleDelete = async (id) => {
    if (!window.confirm("Bạn có chắc chắn muốn xóa ca này?")) return;
    await caLamViecService.remove(id);
    loadData();
  };

  const handleReset = () => {
    setForm(initialForm);
    setEditingId(null);
  };

  // --- Theme Styles (Giữ nguyên phong cách bạn thích) ---
  const styles = {
    container: { padding: '40px', maxWidth: '1000px', margin: '0 auto', fontFamily: "'Inter', sans-serif", color: '#2c3e50' },
    headerSection: { borderBottom: '2px solid #edf2f7', marginBottom: '30px', paddingBottom: '15px' },
    title: { margin: 0, fontSize: '24px', color: '#1a202c', fontWeight: '800' },
    formCard: { background: '#ffffff', borderRadius: '12px', padding: '24px', boxShadow: '0 4px 6px -1px rgba(0,0,0,0.1)', border: '1px solid #e2e8f0', marginBottom: '30px' },
    inputRow: { display: 'flex', gap: '15px', marginBottom: '15px', flexWrap: 'wrap' },
    inputGroup: { flex: 1, minWidth: '200px', display: 'flex', flexDirection: 'column', gap: '6px' },
    label: { fontSize: '13px', fontWeight: '600', color: '#718096' },
    input: { padding: '12px', borderRadius: '8px', border: '1px solid #cbd5e0', fontSize: '14px', outline: 'none' },
    
    // Nổi bật các nút
    btnPrimary: { background: '#2563eb', color: '#fff', padding: '12px 24px', borderRadius: '8px', border: 'none', fontWeight: 'bold', cursor: 'pointer' },
    btnReset: { background: '#f8fafc', color: '#475569', padding: '12px 24px', borderRadius: '8px', border: '1px solid #e2e8f0', fontWeight: 'bold', cursor: 'pointer' },
    
    table: { width: '100%', borderCollapse: 'collapse', background: '#fff' },
    th: { background: '#f8fafc', padding: '15px', textAlign: 'left', fontSize: '13px', color: '#4a5568', textTransform: 'uppercase', borderBottom: '2px solid #edf2f7' },
    td: { padding: '15px', borderBottom: '1px solid #edf2f7', fontSize: '15px' },
    
    // Nổi bật phần chữ dữ liệu
    timeText: { fontWeight: '700', color: '#1e293b', background: '#f1f5f9', padding: '4px 8px', borderRadius: '6px' },
    statusActive: { color: '#16a34a', fontWeight: '700', fontSize: '13px' },
    statusInactive: { color: '#dc2626', fontWeight: '700', fontSize: '13px' },
    
    editLink: { color: '#2563eb', fontWeight: '600', marginRight: '15px', cursor: 'pointer' },
    deleteLink: { color: '#e11d48', fontWeight: '600', cursor: 'pointer' }
  };

  return (
    <div style={styles.container}>
      <div style={styles.headerSection}>
        <h2 style={styles.title}>🕓 Quản lý Ca Làm Việc</h2>
        <p style={{ color: '#718096', marginTop: '5px' }}>Thiết lập khung giờ làm việc cho hệ thống</p>
      </div>

      {/* FORM NHẬP LIỆU */}
      <div style={styles.formCard}>
        <div style={styles.inputRow}>
          <div style={styles.inputGroup}>
            <label style={styles.label}>Tên ca</label>
            <input
              style={styles.input}
              placeholder="VD: Ca sáng hành chính"
              value={form.tenCa}
              onChange={(e) => setForm({ ...form, tenCa: e.target.value })}
            />
          </div>
          <div style={styles.inputGroup}>
            <label style={styles.label}>Giờ bắt đầu</label>
            <input
              type="time"
              style={styles.input}
              value={form.gioBatDau}
              onChange={(e) => setForm({ ...form, gioBatDau: e.target.value })}
            />
          </div>
          <div style={styles.inputGroup}>
            <label style={styles.label}>Giờ kết thúc</label>
            <input
              type="time"
              style={styles.input}
              value={form.gioKetThuc}
              onChange={(e) => setForm({ ...form, gioKetThuc: e.target.value })}
            />
          </div>
        </div>

        <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
          <label style={{ fontSize: '14px', fontWeight: '600', display: 'flex', alignItems: 'center', gap: '8px', cursor: 'pointer' }}>
            <input
              type="checkbox"
              style={{ width: '16px', height: '16px' }}
              checked={form.hoatDong}
              onChange={(e) => setForm({ ...form, hoatDong: e.target.checked })}
            />
            Đang hoạt động
          </label>
          
          <div style={{ display: 'flex', gap: '10px' }}>
            <button style={styles.btnReset} onClick={handleReset}>Làm mới</button>
            <button 
              style={{ ...styles.btnPrimary, background: editingId ? '#f59e0b' : '#2563eb' }} 
              onClick={handleSubmit}
              disabled={loading}
            >
              {loading ? "Đang xử lý..." : editingId ? "Cập nhật Ca" : "Thêm Ca làm việc"}
            </button>
          </div>
        </div>
      </div>

      {/* DANH SÁCH BẢNG */}
      <div style={{ borderRadius: '12px', overflow: 'hidden', border: '1px solid #e2e8f0', boxShadow: '0 2px 4px rgba(0,0,0,0.05)' }}>
        <table style={styles.table}>
          <thead>
            <tr>
              <th style={{ ...styles.th, width: '60px' }}>ID</th>
              <th style={styles.th}>Tên ca làm việc</th>
              <th style={styles.th}>Khung giờ</th>
              <th style={styles.th}>Trạng thái</th>
              <th style={{ ...styles.th, textAlign: 'right' }}>Thao tác</th>
            </tr>
          </thead>
          <tbody>
            {list.length > 0 ? list.map((item) => (
              <tr key={item.id} onMouseOver={(e) => e.currentTarget.style.backgroundColor = '#f8fafc'} onMouseOut={(e) => e.currentTarget.style.backgroundColor = 'transparent'}>
                <td style={styles.td}><span style={{ color: '#94a3b8' }}>#{item.id}</span></td>
                <td style={{ ...styles.td, fontWeight: '700', color: '#1e293b' }}>{item.tenCa}</td>
                <td style={styles.td}>
                  <span style={styles.timeText}>{item.gioBatDau}</span>
                  <span style={{ margin: '0 8px', color: '#cbd5e0' }}>→</span>
                  <span style={styles.timeText}>{item.gioKetThuc}</span>
                </td>
                <td style={styles.td}>
                  <span style={item.hoatDong ? styles.statusActive : styles.statusInactive}>
                    {item.hoatDong ? "● Đang sử dụng" : "○ Tạm ngưng"}
                  </span>
                </td>
                <td style={{ ...styles.td, textAlign: 'right' }}>
                  <span style={styles.editLink} onClick={() => handleEdit(item.id)}>Sửa</span>
                  <span style={styles.deleteLink} onClick={() => handleDelete(item.id)}>Xóa</span>
                </td>
              </tr>
            )) : (
              <tr>
                <td colSpan="5" style={{ ...styles.td, textAlign: 'center', padding: '40px', color: '#94a3b8' }}>
                  Chưa có dữ liệu ca làm việc.
                </td>
              </tr>
            )}
          </tbody>
        </table>
      </div>
    </div>
  );
};

export default CaLamViecPage;