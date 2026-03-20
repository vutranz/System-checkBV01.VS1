import { useEffect, useState } from "react";
import * as nhomDVKTService from "../services/nhomDVKTService";

const initialForm = {
  maNhom: "",
  tenNhom: "",
};

const NhomDVKTPage = () => {
  const [list, setList] = useState([]);
  const [form, setForm] = useState(initialForm);
  const [editingId, setEditingId] = useState(null);
  const [loading, setLoading] = useState(false);

  const loadData = async () => {
    try {
      const data = await nhomDVKTService.getAll();
      setList(data || []);
    } catch (err) {
      console.error(err);
    }
  };

  useEffect(() => {
    loadData();
  }, []);

  const handleSubmit = async () => {
    if (!form.maNhom || !form.tenNhom) {
      alert("Vui lòng điền đầy đủ thông tin!");
      return;
    }
    setLoading(true);
    try {
      if (editingId) {
        await nhomDVKTService.update(editingId, form);
      } else {
        await nhomDVKTService.create(form);
      }
      handleReset();
      loadData();
    } catch (err) {
      alert("Có lỗi xảy ra trong quá trình xử lý");
    } finally {
      setLoading(false);
    }
  };

  const handleEdit = async (id) => {
    const data = await nhomDVKTService.getById(id);
    setForm(data);
    setEditingId(id);
    window.scrollTo({ top: 0, behavior: 'smooth' });
  };

  const handleDelete = async (id) => {
    if (!window.confirm("Bạn có chắc chắn muốn xóa nhóm này không?")) return;
    await nhomDVKTService.remove(id);
    loadData();
  };

  const handleReset = () => {
    setForm(initialForm);
    setEditingId(null);
  };

  // --- Theme Styles ---
  const styles = {
    container: { padding: '40px', maxWidth: '900px', margin: '0 auto', fontFamily: "'Inter', sans-serif", color: '#2c3e50' },
    headerSection: { borderBottom: '2px solid #edf2f7', marginBottom: '30px', paddingBottom: '15px' },
    title: { margin: 0, fontSize: '24px', color: '#1a202c', fontWeight: '800' },
    formCard: { background: '#ffffff', borderRadius: '12px', padding: '24px', boxShadow: '0 4px 6px -1px rgba(0,0,0,0.1)', border: '1px solid #e2e8f0', marginBottom: '30px' },
    inputRow: { display: 'flex', gap: '15px', marginBottom: '15px' },
    inputGroup: { flex: 1, display: 'flex', flexDirection: 'column', gap: '6px' },
    label: { fontSize: '13px', fontWeight: '600', color: '#718096' },
    input: { padding: '12px', borderRadius: '8px', border: '1px solid #cbd5e0', fontSize: '14px', outline: 'none', transition: 'border-color 0.2s' },
    btnPrimary: { background: '#4a90e2', color: '#fff', padding: '12px 24px', borderRadius: '8px', border: 'none', fontWeight: 'bold', cursor: 'pointer', transition: '0.3s' },
    btnReset: { background: '#f7fafc', color: '#4a5568', padding: '12px 24px', borderRadius: '8px', border: '1px solid #e2e8f0', fontWeight: 'bold', cursor: 'pointer' },
    table: { width: '100%', borderCollapse: 'collapse', background: '#fff', borderRadius: '12px', overflow: 'hidden' },
    th: { background: '#f8fafc', padding: '15px', textAlign: 'left', fontSize: '13px', color: '#4a5568', textTransform: 'uppercase', letterSpacing: '0.05em', borderBottom: '2px solid #edf2f7' },
    td: { padding: '15px', borderBottom: '1px solid #edf2f7', fontSize: '15px' },
    editLink: { color: '#4a90e2', textDecoration: 'none', fontWeight: '600', marginRight: '15px', cursor: 'pointer' },
    deleteLink: { color: '#e53e3e', textDecoration: 'none', fontWeight: '600', cursor: 'pointer' }
  };

  return (
    <div style={styles.container}>
      <div style={styles.headerSection}>
        <h2 style={styles.title}>📁 Danh mục Nhóm DVKT</h2>
        <p style={{ color: '#718096', marginTop: '5px' }}>Quản lý các nhóm dịch vụ kỹ thuật trong hệ thống</p>
      </div>

      {/* FORM QUẢN LÝ */}
      <div style={styles.formCard}>
        <div style={styles.inputRow}>
          <div style={styles.inputGroup}>
            <label style={styles.label}>Mã định danh nhóm</label>
            <input
              style={styles.input}
              placeholder="Ví dụ: N01"
              value={form.maNhom}
              onChange={(e) => setForm({ ...form, maNhom: e.target.value })}
            />
          </div>
          <div style={styles.inputGroup}>
            <label style={styles.label}>Tên hiển thị nhóm</label>
            <input
              style={styles.input}
              placeholder="Ví dụ: Nhóm xét nghiệm máu"
              value={form.tenNhom}
              onChange={(e) => setForm({ ...form, tenNhom: e.target.value })}
            />
          </div>
        </div>
        
        <div style={{ display: 'flex', gap: '10px', justifyContent: 'flex-end' }}>
          <button style={styles.btnReset} onClick={handleReset}>Làm mới</button>
          <button 
            style={{ ...styles.btnPrimary, background: editingId ? '#ed8936' : '#4a90e2' }} 
            onClick={handleSubmit}
            disabled={loading}
          >
            {loading ? "Đang lưu..." : editingId ? "Cập nhật Nhóm" : "Tạo Nhóm mới"}
          </button>
        </div>
      </div>

      {/* DANH SÁCH BẢNG */}
      <div style={{ borderRadius: '12px', overflow: 'hidden', border: '1px solid #e2e8f0', boxShadow: '0 2px 4px rgba(0,0,0,0.05)' }}>
        <table style={styles.table}>
          <thead>
            <tr>
              <th style={{ ...styles.th, width: '80px' }}>ID</th>
              <th style={{ ...styles.th, width: '150px' }}>Mã nhóm</th>
              <th style={styles.th}>Tên nhóm dịch vụ</th>
              <th style={{ ...styles.th, textAlign: 'right' }}>Hành động</th>
            </tr>
          </thead>
          <tbody>
            {list.length > 0 ? list.map((item) => (
              <tr key={item.id} onMouseOver={(e) => e.currentTarget.style.backgroundColor = '#fbfcfd'} onMouseOut={(e) => e.currentTarget.style.backgroundColor = 'transparent'}>
                <td style={styles.td}><span style={{ color: '#cbd5e0' }}>#{item.id}</span></td>
                <td style={{ ...styles.td, fontWeight: 'bold' }}>{item.maNhom}</td>
                <td style={styles.td}>{item.tenNhom}</td>
                <td style={{ ...styles.td, textAlign: 'right' }}>
                  <span style={styles.editLink} onClick={() => handleEdit(item.id)}>Sửa</span>
                  <span style={styles.deleteLink} onClick={() => handleDelete(item.id)}>Xóa</span>
                </td>
              </tr>
            )) : (
              <tr>
                <td colSpan="4" style={{ ...styles.td, textAlign: 'center', padding: '40px', color: '#a0aec0' }}>
                  Chưa có dữ liệu nhóm dịch vụ nào được tạo.
                </td>
              </tr>
            )}
          </tbody>
        </table>
      </div>
    </div>
  );
};

export default NhomDVKTPage;