import { useEffect, useState } from "react";
import * as service from "../services/chuyenKhoaService";

const initialForm = {
  maChuyenKhoa: "",
  tenChuyenKhoa: "",
  hoatDong: true,
  moTa: "", // Thêm mô tả để khớp với bảng
};

const ChuyenKhoaPage = () => {
  const [list, setList] = useState([]);
  const [form, setForm] = useState(initialForm);
  const [editingId, setEditingId] = useState(null);
  const [loading, setLoading] = useState(false);

  const loadData = async () => {
    try {
      const data = await service.getAll();
      setList(data || []);
    } catch (err) {
      console.error("Lỗi load dữ liệu:", err);
    }
  };

  useEffect(() => {
    loadData();
  }, []);

  const handleSubmit = async () => {
    if (!form.maChuyenKhoa || !form.tenChuyenKhoa) {
      return alert("Vui lòng điền Mã và Tên chuyên khoa");
    }
    setLoading(true);
    try {
      if (editingId) {
        await service.update(editingId, form);
        alert("Cập nhật thành công!");
      } else {
        await service.create(form);
        alert("Thêm mới thành công!");
      }
      handleReset();
      loadData();
    } catch (err) {
      alert("Có lỗi xảy ra, vui lòng kiểm tra lại");
    } finally {
      setLoading(false);
    }
  };

  const handleEdit = async (id) => {
    const data = await service.getById(id);
    setForm(data);
    setEditingId(id);
    window.scrollTo({ top: 0, behavior: "smooth" });
  };

  const handleDelete = async (id) => {
    if (!window.confirm("Xóa chuyên khoa này có thể ảnh hưởng đến dữ liệu liên quan. Bạn chắc chắn chứ?")) return;
    await service.remove(id);
    loadData();
  };

  const handleReset = () => {
    setForm(initialForm);
    setEditingId(null);
  };

  // --- Theme Styles ---
  const styles = {
    container: { padding: "30px", maxWidth: "1100px", margin: "0 auto", fontFamily: "'Inter', sans-serif" },
    title: { color: "#2d3748", fontSize: "24px", fontWeight: "700", marginBottom: "20px", display: "flex", alignItems: "center", gap: "10px" },
    card: { background: "#fff", borderRadius: "12px", boxShadow: "0 4px 6px -1px rgba(0,0,0,0.1)", padding: "24px", marginBottom: "30px", border: "1px solid #e2e8f0" },
    grid: { display: "grid", gridTemplateColumns: "1fr 1fr 1fr", gap: "20px", marginBottom: "20px" },
    inputGroup: { display: "flex", flexDirection: "column", gap: "8px" },
    label: { fontSize: "14px", fontWeight: "600", color: "#4a5568" },
    input: { padding: "10px 12px", borderRadius: "8px", border: "1px solid #cbd5e0", fontSize: "15px", outline: "none", transition: "border-color 0.2s" },
    btnPrimary: { padding: "10px 24px", borderRadius: "8px", border: "none", color: "#fff", fontWeight: "600", cursor: "pointer", transition: "all 0.2s" },
    btnSecondary: { padding: "10px 24px", borderRadius: "8px", border: "1px solid #cbd5e0", background: "#fff", color: "#4a5568", fontWeight: "600", cursor: "pointer" },
    table: { width: "100%", borderCollapse: "separate", borderSpacing: "0", background: "#fff", borderRadius: "12px", overflow: "hidden", border: "1px solid #e2e8f0" },
    th: { background: "#f8fafc", padding: "14px 20px", textAlign: "left", fontSize: "13px", color: "#64748b", textTransform: "uppercase", letterSpacing: "0.05em", borderBottom: "2px solid #e2e8f0" },
    td: { padding: "16px 20px", fontSize: "14px", color: "#334155", borderBottom: "1px solid #f1f5f9" },
    actionBtn: { border: "none", background: "none", fontWeight: "600", cursor: "pointer", fontSize: "13px", padding: "4px 8px" },
    badge: (isActive) => ({
      padding: "4px 10px", borderRadius: "20px", fontSize: "12px", fontWeight: "600",
      background: isActive ? "#dcfce7" : "#fee2e2", color: isActive ? "#166534" : "#991b1b"
    })
  };

  return (
    <div style={styles.container}>
      <h2 style={styles.title}>🏥 Quản lý Chuyên khoa</h2>

      {/* FORM NHẬP LIỆU */}
      <div style={styles.card}>
        <div style={styles.grid}>
          <div style={styles.inputGroup}>
            <label style={styles.label}>Mã chuyên khoa</label>
            <input
              style={styles.input}
              placeholder="VD: NOI, NGOAI..."
              value={form.maChuyenKhoa}
              onChange={(e) => setForm({ ...form, maChuyenKhoa: e.target.value })}
            />
          </div>

          <div style={styles.inputGroup}>
            <label style={styles.label}>Tên chuyên khoa</label>
            <input
              style={styles.input}
              placeholder="VD: Nội tổng quát"
              value={form.tenChuyenKhoa}
              onChange={(e) => setForm({ ...form, tenChuyenKhoa: e.target.value })}
            />
          </div>

          <div style={styles.inputGroup}>
            <label style={styles.label}>Mô tả / Ghi chú</label>
            <input
              style={styles.input}
              placeholder="Thông tin thêm..."
              value={form.moTa || ""}
              onChange={(e) => setForm({ ...form, moTa: e.target.value })}
            />
          </div>
        </div>

        <div style={{ display: "flex", justifyContent: "space-between", alignItems: "center" }}>
          <label style={{ display: "flex", alignItems: "center", gap: "8px", cursor: "pointer", fontSize: "14px" }}>
            <input
              type="checkbox"
              checked={form.hoatDong}
              onChange={(e) => setForm({ ...form, hoatDong: e.target.checked })}
              style={{ width: "18px", height: "18px" }}
            />
            Đang hoạt động
          </label>

          <div style={{ display: "flex", gap: "12px" }}>
            <button style={styles.btnSecondary} onClick={handleReset}>Làm mới</button>
            <button
              style={{ ...styles.btnPrimary, background: editingId ? "#f59e0b" : "#3b82f6" }}
              onClick={handleSubmit}
              disabled={loading}
            >
              {loading ? "Đang lưu..." : editingId ? "Cập nhật thay đổi" : "Lưu chuyên khoa"}
            </button>
          </div>
        </div>
      </div>

      {/* DANH SÁCH BẢNG */}
      <div style={{ overflowX: "auto" }}>
        <table style={styles.table}>
          <thead>
            <tr>
              <th style={styles.th}>Mã</th>
              <th style={styles.th}>Tên chuyên khoa</th>
              <th style={styles.th}>Mô tả</th>
              <th style={styles.th}>Trạng thái</th>
              <th style={{ ...styles.th, textAlign: "right" }}>Thao tác</th>
            </tr>
          </thead>
          <tbody>
            {list.map((item) => (
              <tr key={item.id} style={{ transition: "0.2s" }}>
                <td style={{ ...styles.td, fontWeight: "700", color: "#1e293b" }}>{item.maChuyenKhoa}</td>
                <td style={styles.td}>{item.tenChuyenKhoa}</td>
                <td style={{ ...styles.td, color: "#64748b", fontStyle: "italic" }}>{item.moTa || "---"}</td>
                <td style={styles.td}>
                  <span style={styles.badge(item.hoatDong)}>
                    {item.hoatDong ? "Hoạt động" : "Tạm ngưng"}
                  </span>
                </td>
                <td style={{ ...styles.td, textAlign: "right" }}>
                  <button
                    style={{ ...styles.actionBtn, color: "#3b82f6" }}
                    onClick={() => handleEdit(item.id)}
                  >
                    Sửa
                  </button>
                  <button
                    style={{ ...styles.actionBtn, color: "#ef4444", marginLeft: "10px" }}
                    onClick={() => handleDelete(item.id)}
                  >
                    Xoá
                  </button>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  );
};

export default ChuyenKhoaPage;