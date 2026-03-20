import { useEffect, useState } from "react";
import * as bacSiService from "../services/bacSiService";

const initialForm = { maBacSi: "", hoTen: "", cchn: "", hoatDong: true };

const BacSiPage = () => {
  const [list, setList] = useState([]);
  const [form, setForm] = useState(initialForm);
  const [editingId, setEditingId] = useState(null);
  const [loading, setLoading] = useState(false);
  
  const [searchTerm, setSearchTerm] = useState("");
  const [currentPage, setCurrentPage] = useState(1);
  const itemsPerPage = 10;

  const loadData = async () => {
    try {
      const data = await bacSiService.getAll();
      setList(data || []);
    } catch (err) { console.error(err); }
  };

  useEffect(() => { loadData(); }, []);

  const filteredList = list.filter(item => 
    item.maBacSi.toLowerCase().includes(searchTerm.toLowerCase()) ||
    item.hoTen.toLowerCase().includes(searchTerm.toLowerCase())
  );

  const indexOfLastItem = currentPage * itemsPerPage;
  const indexOfFirstItem = indexOfLastItem - itemsPerPage;
  const currentItems = filteredList.slice(indexOfFirstItem, indexOfLastItem);
  const totalPages = Math.ceil(filteredList.length / itemsPerPage);

  const paginate = (pageNumber) => setCurrentPage(pageNumber);

  const handleSubmit = async () => {
    if (!form.maBacSi || !form.hoTen) return alert("Vui lòng nhập đủ Mã và Tên");
    setLoading(true);
    try {
      editingId ? await bacSiService.update(editingId, form) : await bacSiService.create(form);
      setForm(initialForm); setEditingId(null); loadData();
    } catch (err) { alert("Lỗi xử lý"); } finally { setLoading(false); }
  };

  const handleDelete = async (id) => {
    if (window.confirm("Xác nhận xóa bác sĩ này?")) {
      await bacSiService.remove(id);
      loadData();
    }
  };

  const styles = {
    container: { padding: '30px', maxWidth: '1200px', margin: '0 auto', fontFamily: "'Inter', sans-serif" },
    header: { textAlign: 'left', marginBottom: '25px', color: '#1e293b', fontWeight: '800', fontSize: '24px' },
    card: { background: '#fff', padding: '25px', borderRadius: '16px', border: '1px solid #e2e8f0', boxShadow: '0 4px 6px -1px rgba(0,0,0,0.05)', marginBottom: '30px' },
    formGrid: { display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(180px, 1fr))', gap: '20px', alignItems: 'end' },
    input: { padding: '12px', borderRadius: '10px', border: '1px solid #cbd5e0', fontSize: '14px', outline: 'none' },
    
    searchContainer: { marginBottom: '20px', display: 'flex', justifyContent: 'flex-end' },
    searchInput: { padding: '10px 15px', width: '300px', borderRadius: '10px', border: '1px solid #6366f1', outline: 'none', fontSize: '14px' },
    
    table: { width: '100%', borderCollapse: 'collapse', background: '#fff' },
    th: { background: '#f8fafc', padding: '15px', textAlign: 'left', color: '#64748b', fontSize: '13px', borderBottom: '2px solid #f1f5f9' },
    td: { padding: '15px', borderBottom: '1px solid #f1f5f9', fontSize: '14px' },
    
    btnAction: { background: 'none', border: 'none', cursor: 'pointer', fontWeight: '600', fontSize: '13px', padding: '5px 10px', borderRadius: '6px' },
    btnEdit: { color: '#6366f1', marginRight: '5px' },
    btnDelete: { color: '#f43f5e' },
    
    pagination: { display: 'flex', justifyContent: 'center', gap: '8px', marginTop: '25px' },
    pageBtn: { padding: '8px 15px', borderRadius: '8px', border: '1px solid #e2e8f0', cursor: 'pointer', background: '#fff' }
  };

  return (
    <div style={styles.container}>
      <h2 style={styles.header}>👨‍⚕️ Danh mục Bác sĩ</h2>

      {/* FORM NHẬP LIỆU */}
      <div style={styles.card}>
        <div style={styles.formGrid}>
          <input style={styles.input} placeholder="Mã Bác sĩ" value={form.maBacSi} onChange={e => setForm({...form, maBacSi: e.target.value})} />
          <input style={styles.input} placeholder="Họ tên" value={form.hoTen} onChange={e => setForm({...form, hoTen: e.target.value})} />
          <input style={styles.input} placeholder="Số CCHN" value={form.cchn} onChange={e => setForm({...form, cchn: e.target.value})} />
          <button 
            style={{ padding: '12px', borderRadius: '10px', border: 'none', background: 'linear-gradient(135deg, #6366f1 0%, #8b5cf6 100%)', color: '#fff', fontWeight: 'bold', cursor: 'pointer' }}
            onClick={handleSubmit}
          >
            {editingId ? "Cập nhật" : "Thêm bác sĩ"}
          </button>
        </div>
      </div>

      <div style={styles.searchContainer}>
        <input 
          style={styles.searchInput}
          placeholder="🔍 Tìm mã hoặc tên bác sĩ..."
          value={searchTerm}
          onChange={(e) => { setSearchTerm(e.target.value); setCurrentPage(1); }}
        />
      </div>

      {/* BẢNG DỮ LIỆU */}
      <div style={{ background: '#fff', borderRadius: '16px', border: '1px solid #e2e8f0', overflow: 'hidden' }}>
        <table style={styles.table}>
          <thead>
            <tr>
              <th style={styles.th}>STT</th>
              <th style={styles.th}>Mã Bác sĩ</th>
              <th style={styles.th}>Họ và Tên</th>
              <th style={styles.th}>Số CCHN</th>
              <th style={styles.th}>Trạng thái</th>
              <th style={{ ...styles.th, textAlign: 'center' }}>Thao tác</th>
            </tr>
          </thead>
          <tbody>
            {currentItems.map((item, index) => (
              <tr key={item.id} onMouseOver={(e) => e.currentTarget.style.backgroundColor = '#f8fafc'} onMouseOut={(e) => e.currentTarget.style.backgroundColor = 'transparent'}>
                <td style={styles.td}>{indexOfFirstItem + index + 1}</td>
                <td style={{ ...styles.td, fontWeight: '700' }}>{item.maBacSi}</td>
                <td style={styles.td}>{item.hoTen}</td>
                <td style={styles.td}>{item.cchn || "---"}</td>
                <td style={styles.td}>
                   <span style={{ color: item.hoatDong ? '#10b981' : '#f43f5e', fontWeight: 'bold' }}>
                    {item.hoatDong ? "● Đang làm" : "○ Nghỉ"}
                  </span>
                </td>
                <td style={{ ...styles.td, textAlign: 'center' }}>
                  <button 
                    style={{ ...styles.btnAction, ...styles.btnEdit }} 
                    onClick={() => { setForm(item); setEditingId(item.id); window.scrollTo({top:0, behavior:'smooth'}); }}
                  >
                    Sửa
                  </button>
                  <button 
                    style={{ ...styles.btnAction, ...styles.btnDelete }} 
                    onClick={() => handleDelete(item.id)}
                  >
                    Xóa
                  </button>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>

      {/* PHÂN TRANG */}
      {totalPages > 1 && (
        <div style={styles.pagination}>
          {[...Array(totalPages)].map((_, i) => (
            <button 
              key={i+1} 
              style={{ ...styles.pageBtn, background: currentPage === i+1 ? '#6366f1' : '#fff', color: currentPage === i+1 ? '#fff' : '#1e293b' }}
              onClick={() => paginate(i+1)}
            >{i+1}</button>
          ))}
        </div>
      )}
    </div>
  );
};

export default BacSiPage;