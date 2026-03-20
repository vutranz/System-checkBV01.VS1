import { useEffect, useState, useMemo } from "react";
import * as service from "../services/dichVuKyThuatService";
import * as nhomService from "../services/nhomDvktService";
import * as chuyenKhoaService from "../services/chuyenKhoaService";

const initialForm = {
  maDvkt: "",
  tenDvkt: "",
  thoiGianMin: "",
  thoiGianMax: "",
  hoatDong: true,
  nhomDvktId: "",
  chuyenKhoaId: "",
  dvktChaId: "",
};

const PAGE_SIZE = 20;

const DichVuKyThuatPage = () => {
  const [list, setList] = useState([]);
  const [form, setForm] = useState(initialForm);
  const [editingId, setEditingId] = useState(null);
  const [currentPage, setCurrentPage] = useState(1);
  const [loading, setLoading] = useState(false);

  const [nhomList, setNhomList] = useState([]);
  const [chuyenKhoaList, setChuyenKhoaList] = useState([]);
  const [dvktList, setDvktList] = useState([]);
  
  // --- STATE TÌM KIẾM & LỌC ---
  const [filterDvktChaId, setFilterDvktChaId] = useState("");
  const [searchText, setSearchText] = useState(""); // Ô tìm kiếm văn bản

  const loadData = async () => {
    const data = await service.getAll();
    setList(data || []);
    setCurrentPage(1);
  };

  const loadOptions = async () => {
    const [nhom, ck, dvkt] = await Promise.all([
      nhomService.getAll(),
      chuyenKhoaService.getAll(),
      service.getRoot()
    ]);
    setNhomList(nhom || []);
    setChuyenKhoaList(ck || []);
    setDvktList(dvkt || []);
  };

  useEffect(() => {
    loadData();
    loadOptions();
  }, []);

  // --- LOGIC TÌM KIẾM & LỌC KẾT HỢP ---
  const filteredList = useMemo(() => {
    return list.filter((item) => {
      // 1. Lọc theo văn bản (Mã hoặc Tên)
      const matchesSearch = 
        item.maDvkt.toLowerCase().includes(searchText.toLowerCase()) ||
        item.tenDvkt.toLowerCase().includes(searchText.toLowerCase());

      // 2. Lọc theo danh mục cha
      let matchesCategory = true;
      if (filterDvktChaId === "null") {
        matchesCategory = !item.dvktChaId;
      } else if (filterDvktChaId !== "") {
        matchesCategory = item.dvktChaId === Number(filterDvktChaId);
      }

      return matchesSearch && matchesCategory;
    });
  }, [list, filterDvktChaId, searchText]);

  const totalPages = Math.ceil(filteredList.length / PAGE_SIZE);
  const startIndex = (currentPage - 1) * PAGE_SIZE;
  const currentData = filteredList.slice(startIndex, startIndex + PAGE_SIZE);

  const handleSubmit = async () => {
    setLoading(true);
    const payload = {
      ...form,
      thoiGianMin: Number(form.thoiGianMin),
      thoiGianMax: Number(form.thoiGianMax),
      nhomDvktId: Number(form.nhomDvktId),
      chuyenKhoaId: Number(form.chuyenKhoaId),
      dvktChaId: form.dvktChaId ? Number(form.dvktChaId) : null,
    };

    try {
      if (editingId) {
        await service.update(editingId, payload);
      } else {
        await service.create(payload);
      }
      setForm(initialForm);
      setEditingId(null);
      loadData();
    } catch (err) {
      alert("Lỗi khi lưu dữ liệu!");
    } finally {
      setLoading(false);
    }
  };

  const handleEdit = async (id) => {
    const data = await service.getById(id);
    setForm({ ...data, dvktChaId: data.dvktChaId || "" });
    setEditingId(id);
    window.scrollTo({ top: 0, behavior: 'smooth' });
  };

  const handleDelete = async (id) => {
    if (window.confirm("Bạn có chắc chắn muốn xoá?")) {
      await service.remove(id);
      loadData();
    }
  };

  // --- Styles ---
  const styles = {
    container: { padding: '25px', maxWidth: '1400px', margin: '0 auto', fontFamily: "'Segoe UI', sans-serif", backgroundColor: '#f4f7f9', minHeight: '100vh' },
    card: { background: '#fff', padding: '20px', borderRadius: '12px', boxShadow: '0 2px 12px rgba(0,0,0,0.05)', marginBottom: '20px' },
    formGrid: { display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(220px, 1fr))', gap: '15px' },
    input: { padding: '10px', borderRadius: '6px', border: '1px solid #dcdfe6', fontSize: '14px', outline: 'none' },
    label: { fontSize: '13px', fontWeight: '600', color: '#606266', marginBottom: '5px', display: 'block' },
    filterBar: { display: 'flex', alignItems: 'center', gap: '15px', background: '#fff', padding: '15px 20px', borderRadius: '12px', marginBottom: '20px', borderLeft: '5px solid #409eff' },
    searchInput: { padding: '10px 15px', borderRadius: '6px', border: '1px solid #409eff', fontSize: '14px', width: '300px', outline: 'none' },
    table: { width: '100%', borderCollapse: 'collapse', backgroundColor: '#fff', borderRadius: '8px', overflow: 'hidden' },
    th: { background: '#f5f7fa', color: '#909399', padding: '12px 15px', textAlign: 'left', fontSize: '13px', borderBottom: '1px solid #ebeef5' },
    td: { padding: '12px 15px', fontSize: '14px', borderBottom: '1px solid #ebeef5', color: '#606266' },
    statusActive: { color: '#67c23a', background: '#f0f9eb', padding: '2px 8px', borderRadius: '4px', fontSize: '12px' },
    statusInactive: { color: '#f56c6c', background: '#fef0f0', padding: '2px 8px', borderRadius: '4px', fontSize: '12px' },
    btnAction: { border: 'none', background: 'none', cursor: 'pointer', fontWeight: 'bold', fontSize: '13px', padding: '5px' }
  };

  return (
    <div style={styles.container}>
      <h2 style={{ color: '#303133', marginBottom: '20px' }}>⚙️ Hệ thống Dịch vụ Kỹ thuật</h2>

      {/* FORM CARD */}
      <div style={styles.card}>
        <div style={{ marginBottom: '15px', borderBottom: '1px solid #eee', paddingBottom: '10px', fontWeight: 'bold', color: '#409eff' }}>
          {editingId ? "📝 Chỉnh sửa dịch vụ" : "➕ Thêm dịch vụ mới"}
        </div>
        <div style={styles.formGrid}>
          {/* Các input form giữ nguyên như cũ */}
          <div>
            <label style={styles.label}>Mã DVKT</label>
            <input style={{ ...styles.input, width: '90%' }} value={form.maDvkt} onChange={(e) => setForm({ ...form, maDvkt: e.target.value })} />
          </div>
          <div>
            <label style={styles.label}>Tên dịch vụ</label>
            <input style={{ ...styles.input, width: '90%' }} value={form.tenDvkt} onChange={(e) => setForm({ ...form, tenDvkt: e.target.value })} />
          </div>
          <div>
            <label style={styles.label}>TG tối thiểu</label>
            <input type="number" style={{ ...styles.input, width: '90%' }} value={form.thoiGianMin} onChange={(e) => setForm({ ...form, thoiGianMin: e.target.value })} />
          </div>
          <div>
            <label style={styles.label}>TG tối đa</label>
            <input type="number" style={{ ...styles.input, width: '90%' }} value={form.thoiGianMax} onChange={(e) => setForm({ ...form, thoiGianMax: e.target.value })} />
          </div>
          <div>
            <label style={styles.label}>Nhóm DVKT</label>
            <select style={{ ...styles.input, width: '98%' }} value={form.nhomDvktId} onChange={(e) => setForm({ ...form, nhomDvktId: e.target.value })}>
              <option value="">-- Chọn nhóm --</option>
              {nhomList.map(item => <option key={item.id} value={item.id}>{item.tenNhom}</option>)}
            </select>
          </div>
          <div>
            <label style={styles.label}>Chuyên khoa</label>
            <select style={{ ...styles.input, width: '98%' }} value={form.chuyenKhoaId} onChange={(e) => setForm({ ...form, chuyenKhoaId: e.target.value })}>
              <option value="">-- Chọn khoa --</option>
              {chuyenKhoaList.map(item => <option key={item.id} value={item.id}>{item.tenChuyenKhoa}</option>)}
            </select>
          </div>
          <div>
            <label style={styles.label}>Dịch vụ cha</label>
            <select style={{ ...styles.input, width: '98%' }} value={form.dvktChaId} onChange={(e) => setForm({ ...form, dvktChaId: e.target.value })}>
              <option value="">-- Cấp cao nhất --</option>
              {dvktList.filter(d => d.id !== editingId).map(item => <option key={item.id} value={item.id}>{item.tenDvkt}</option>)}
            </select>
          </div>
          <div style={{ display: 'flex', alignItems: 'flex-end', gap: '10px' }}>
            <label style={{ ...styles.label, display: 'flex', alignItems: 'center', marginBottom: '12px' }}>
              <input type="checkbox" checked={form.hoatDong} onChange={(e) => setForm({ ...form, hoatDong: e.target.checked })} /> Hoạt động
            </label>
            <button 
              onClick={handleSubmit} 
              style={{ padding: '10px 20px', backgroundColor: editingId ? '#e6a23c' : '#409eff', color: '#fff', border: 'none', borderRadius: '6px', cursor: 'pointer', fontWeight: 'bold' }}
            >
              {editingId ? "Cập nhật" : "Lưu lại"}
            </button>
          </div>
        </div>
      </div>

      {/* FILTER BAR CÓ Ô TÌM KIẾM */}
      <div style={styles.filterBar}>
        <div style={{ display: 'flex', alignItems: 'center', gap: '10px' }}>
          <span style={{ fontWeight: 'bold' }}>Tìm kiếm:</span>
          <input 
            style={styles.searchInput} 
            placeholder="Nhập tên hoặc mã DVKT..." 
            value={searchText}
            onChange={(e) => { setSearchText(e.target.value); setCurrentPage(1); }}
          />
        </div>

        <div style={{ display: 'flex', alignItems: 'center', gap: '10px', marginLeft: '20px' }}>
          <span style={{ fontWeight: 'bold' }}>Phân loại:</span>
          <select 
            style={{ ...styles.input, minWidth: '220px' }} 
            value={filterDvktChaId} 
            onChange={(e) => { setFilterDvktChaId(e.target.value); setCurrentPage(1); }}
          >
            <option value="">Tất cả danh mục</option>
            <option value="null">⭐ Chỉ dịch vụ Gốc</option>
            {dvktList.map(item => <option key={item.id} value={item.id}>Con của: {item.tenDvkt}</option>)}
          </select>
        </div>

        <div style={{ marginLeft: 'auto', color: '#909399', fontSize: '14px' }}>
          Hiển thị: <b>{filteredList.length}</b> kết quả
        </div>
      </div>

      {/* TABLE */}
      <div style={{ ...styles.card, padding: '0', overflow: 'hidden' }}>
        <table style={styles.table}>
          <thead>
            <tr>
              <th style={styles.th}>Mã</th>
              <th style={styles.th}>Tên Dịch vụ kỹ thuật</th>
              <th style={styles.th}>Thời gian</th>
              <th style={styles.th}>Trạng thái</th>
              <th style={{ ...styles.th, textAlign: 'center' }}>Thao tác</th>
            </tr>
          </thead>
          <tbody>
            {currentData.length > 0 ? currentData.map((item) => (
              <tr key={item.id} onMouseOver={(e) => e.currentTarget.style.backgroundColor = '#f9fafc'} onMouseOut={(e) => e.currentTarget.style.backgroundColor = 'transparent'}>
                <td style={{ ...styles.td, fontWeight: 'bold' }}>{item.maDvkt}</td>
                <td style={styles.td}>
                  {item.dvktChaId && <span style={{ color: '#c0c4cc', marginRight: '5px' }}>↳</span>}
                  {item.tenDvkt}
                </td>
                <td style={styles.td}>{item.thoiGianMin}-{item.thoiGianMax}p</td>
                <td style={styles.td}>
                  <span style={item.hoatDong ? styles.statusActive : styles.statusInactive}>
                    {item.hoatDong ? "Sử dụng" : "Ngưng"}
                  </span>
                </td>
                <td style={{ ...styles.td, textAlign: 'center' }}>
                  <button style={{ ...styles.btnAction, color: '#409eff' }} onClick={() => handleEdit(item.id)}>Sửa</button>
                  <button style={{ ...styles.btnAction, color: '#f56c6c', marginLeft: '10px' }} onClick={() => handleDelete(item.id)}>Xoá</button>
                </td>
              </tr>
            )) : (
              <tr><td colSpan="5" style={{ ...styles.td, textAlign: 'center' }}>Không tìm thấy dữ liệu phù hợp</td></tr>
            )}
          </tbody>
        </table>
      </div>

      {/* PAGINATION */}
      {totalPages > 1 && (
        <div style={{ display: 'flex', justifyContent: 'center', gap: '5px', marginTop: '20px' }}>
          {Array.from({ length: totalPages }, (_, i) => (
            <button
              key={i}
              onClick={() => setCurrentPage(i + 1)}
              style={{
                padding: '8px 14px', border: '1px solid #dcdfe6', borderRadius: '4px',
                backgroundColor: currentPage === i + 1 ? '#409eff' : '#fff',
                color: currentPage === i + 1 ? '#fff' : '#606266', cursor: 'pointer'
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

export default DichVuKyThuatPage;