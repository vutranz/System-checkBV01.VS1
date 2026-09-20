import { useEffect, useState, useMemo, useRef } from "react";
import * as XLSX from "xlsx";
import * as service from "../services/lichLamViecTheoThuService";
import * as bacSiService from "../services/bacSiService";
import * as caLamViecService from "../services/caLamViecService";

const THU_OPTIONS = [
  { value: "T2", label: "Thứ 2" },
  { value: "T3", label: "Thứ 3" },
  { value: "T4", label: "Thứ 4" },
  { value: "T5", label: "Thứ 5" },
  { value: "T6", label: "Thứ 6" },
  { value: "T7", label: "Thứ 7" },
  { value: "CN", label: "Chủ Nhật" },
];

const PAGE_SIZE = 10;

const initialForm = {
  bacSiId: "",
  danhSachThu: [],
  caLamViecId: "",
};

const LichLamViecTheoThuPage = () => {
  const [list, setList] = useState([]);
  const [bacSiList, setBacSiList] = useState([]);
  const [caList, setCaList] = useState([]);
  const [currentPage, setCurrentPage] = useState(1);
  const [search, setSearch] = useState("");
  const [form, setForm] = useState(initialForm);
  const [editingId, setEditingId] = useState(null);
  const [loading, setLoading] = useState(false);

  const fileInputRef = useRef(null);

  const loadData = async () => {
    try {
      const res = await service.getAll();
      setList(res || []);
    } catch (err) {
      console.error("Lỗi tải lịch làm việc:", err);
    }
  };

  const loadOptions = async () => {
    try {
      const [bs, ca] = await Promise.all([
        bacSiService.getAll(),
        caLamViecService.getAll(),
      ]);
      setBacSiList(bs || []);
      setCaList(ca || []);
    } catch (err) {
      console.error("Lỗi tải danh mục bác sĩ / ca làm việc:", err);
    }
  };

  useEffect(() => {
    loadData();
    loadOptions();
  }, []);

  // Map ID -> Tên/Đối tượng
  const caMap = useMemo(() => new Map(caList.map((c) => [c.id, c.tenCa])), [caList]);
  const bacSiMap = useMemo(() => new Map(bacSiList.map((b) => [b.id, b.hoTen || b.tenBacSi])), [bacSiList]);

  // --- Logic toggle & chọn nhanh Thứ ---
  const toggleThu = (value) => {
    setForm((prev) => {
      const isSelected = prev.danhSachThu.includes(value);
      return {
        ...prev,
        danhSachThu: isSelected
          ? prev.danhSachThu.filter((t) => t !== value)
          : [...prev.danhSachThu, value],
      };
    });
  };

  const selectT2ToT6 = () => {
    setForm((prev) => ({
      ...prev,
      danhSachThu: ["T2", "T3", "T4", "T5", "T6"],
    }));
  };

  const selectAllThu = () => {
    setForm((prev) => ({
      ...prev,
      danhSachThu:
        prev.danhSachThu.length === THU_OPTIONS.length
          ? []
          : THU_OPTIONS.map((t) => t.value),
    }));
  };

  // --- Gom nhóm dữ liệu dạng Ma Trận Tuần (Bác sĩ x 7 Thứ) ---
  const matrixData = useMemo(() => {
    const map = new Map();

    // Khởi tạo hàng cho tất cả Bác sĩ (hoặc bác sĩ đã được gán lịch)
    bacSiList.forEach((bs) => {
      const name = bs.hoTen || bs.tenBacSi || `Bác sĩ #${bs.id}`;
      map.set(bs.id, {
        bacSiId: bs.id,
        tenBacSi: name,
        schedules: { T2: [], T3: [], T4: [], T5: [], T6: [], T7: [], CN: [] },
      });
    });

    // Đổ lịch làm việc vào từng ô tương ứng
    list.forEach((item) => {
      if (!map.has(item.bacSiId)) {
        map.set(item.bacSiId, {
          bacSiId: item.bacSiId,
          tenBacSi: item.tenBacSi || item.hoTen || `Bác sĩ #${item.bacSiId}`,
          schedules: { T2: [], T3: [], T4: [], T5: [], T6: [], T7: [], CN: [] },
        });
      }
      const doctorRow = map.get(item.bacSiId);
      if (doctorRow.schedules[item.thu]) {
        doctorRow.schedules[item.thu].push(item);
      }
    });

    return Array.from(map.values());
  }, [list, bacSiList]);

  // Lọc theo từ khóa tìm kiếm
  const filteredDoctors = useMemo(() => {
    return matrixData.filter((bs) =>
      bs.tenBacSi.toLowerCase().includes(search.toLowerCase())
    );
  }, [matrixData, search]);

  useEffect(() => {
    setCurrentPage(1);
  }, [search]);

  const totalPages = Math.ceil(filteredDoctors.length / PAGE_SIZE);
  const paginatedData = useMemo(() => {
    const start = (currentPage - 1) * PAGE_SIZE;
    return filteredDoctors.slice(start, start + PAGE_SIZE);
  }, [filteredDoctors, currentPage]);

  // --- SUBMIT FORM ---
  const handleSubmit = async () => {
    if (!form.bacSiId || !form.caLamViecId || form.danhSachThu.length === 0) {
      alert("Vui lòng chọn đầy đủ Bác sĩ, Ca trực và ít nhất 1 Thứ!");
      return;
    }

    setLoading(true);
    try {
      if (editingId) {
        const payload = {
          bacSiId: Number(form.bacSiId),
          thu: form.danhSachThu[0],
          caLamViecId: Number(form.caLamViecId),
        };
        await service.update(editingId, payload);
      } else {
        if (typeof service.createBulk === "function") {
          await service.createBulk({
            bacSiId: Number(form.bacSiId),
            caLamViecId: Number(form.caLamViecId),
            danhSachThu: form.danhSachThu,
          });
        } else {
          const requests = form.danhSachThu.map((thu) =>
            service.create({
              bacSiId: Number(form.bacSiId),
              thu: thu,
              caLamViecId: Number(form.caLamViecId),
            })
          );
          await Promise.all(requests);
        }
      }
      handleReset();
      await loadData();
    } catch (err) {
      console.error(err);
      alert("Có lỗi xảy ra khi lưu lịch làm việc!");
    } finally {
      setLoading(false);
    }
  };

  const handleReset = () => {
    setForm(initialForm);
    setEditingId(null);
  };

  const handleDelete = async (id) => {
    if (window.confirm("Xoá ca làm việc này?")) {
      try {
        await service.remove(id);
        await loadData();
      } catch (err) {
        alert("Xóa thất bại!");
      }
    }
  };

  const handleEdit = (item) => {
    setForm({
      bacSiId: item.bacSiId,
      danhSachThu: [item.thu],
      caLamViecId: item.caLamViecId,
    });
    setEditingId(item.id);
    window.scrollTo({ top: 0, behavior: "smooth" });
  };

  // ================= 1. XỬ LÝ XUẤT EXCEL MA TRẬN =================
  const handleExportExcel = () => {
    if (filteredDoctors.length === 0) {
      return alert("Không có dữ liệu lịch làm việc để xuất Excel!");
    }

    const dataToExport = filteredDoctors.map((bs, index) => {
      const row = {
        STT: index + 1,
        "Tên Bác Sĩ": bs.tenBacSi,
      };

      THU_OPTIONS.forEach((t) => {
        const items = bs.schedules[t.value] || [];
        row[t.label] = items.map((item) => caMap.get(item.caLamViecId) || item.tenCa || "Ca trực").join(", ") || "-";
      });

      return row;
    });

    const worksheet = XLSX.utils.json_to_sheet(dataToExport);

    worksheet["!cols"] = [
      { wch: 6 },  // STT
      { wch: 25 }, // Tên Bác Sĩ
      { wch: 18 }, // T2
      { wch: 18 }, // T3
      { wch: 18 }, // T4
      { wch: 18 }, // T5
      { wch: 18 }, // T6
      { wch: 18 }, // T7
      { wch: 18 }, // CN
    ];

    const workbook = XLSX.utils.book_new();
    XLSX.utils.book_append_sheet(workbook, worksheet, "LichLamViecTheoThu");

    const fileName = `Lich_Lam_Viec_Theo_Thu_${new Date().toISOString().slice(0, 10)}.xlsx`;
    XLSX.writeFile(workbook, fileName);
  };

  // ================= 2. XỬ LÝ NHẬP EXCEL =================
  const handleImportExcel = (e) => {
    const file = e.target.files[0];
    if (!file) return;

    const reader = new FileReader();
    reader.onload = async (evt) => {
      try {
        setLoading(true);
        const bstr = evt.target.result;
        const workbook = XLSX.read(bstr, { type: "binary" });
        const sheetName = workbook.SheetNames[0];
        const worksheet = workbook.Sheets[sheetName];
        const jsonData = XLSX.utils.sheet_to_json(worksheet);

        if (jsonData.length === 0) {
          alert("File Excel không có dữ liệu!");
          return;
        }

        // Map Tên Bác sĩ & Tên Ca -> ID
        const bsNameToId = new Map(bacSiList.map((b) => [(b.hoTen || b.tenBacSi)?.trim().toLowerCase(), b.id]));
        const caNameToId = new Map(caList.map((c) => [c.tenCa?.trim().toLowerCase(), c.id]));

        let successCount = 0;
        let failCount = 0;

        for (const row of jsonData) {
          const bsName = (row["Bác Sĩ"] || row["Tên Bác Sĩ"] || row["bacSi"] || "").toString().trim().toLowerCase();
          const thuVal = (row["Thứ"] || row["thu"] || "").toString().trim().toUpperCase();
          const caName = (row["Ca Trực"] || row["Tên Ca"] || row["caLamViec"] || "").toString().trim().toLowerCase();

          const bacSiId = bsNameToId.get(bsName);
          const caLamViecId = caNameToId.get(caName);

          if (!bacSiId || !caLamViecId || !thuVal) {
            failCount++;
            continue;
          }

          try {
            await service.create({
              bacSiId: Number(bacSiId),
              thu: thuVal,
              caLamViecId: Number(caLamViecId),
            });
            successCount++;
          } catch (err) {
            failCount++;
          }
        }

        alert(`Nhập lịch làm việc hoàn tất!\n- Thành công: ${successCount}\n- Thất bại: ${failCount}`);
        await loadData();
      } catch (error) {
        console.error(error);
        alert("Lỗi khi đọc file Excel!");
      } finally {
        setLoading(false);
        if (fileInputRef.current) fileInputRef.current.value = "";
      }
    };

    reader.readAsBinaryString(file);
  };

  // --- UI Styles ---
  const styles = {
    container: {
      padding: "30px",
      maxWidth: "1400px",
      margin: "0 auto",
      fontFamily: "'Inter', sans-serif",
      color: "#2c3e50",
      backgroundColor: "#f8fafc",
      minHeight: "100vh",
    },
    headerSection: {
      display: "flex",
      justifyContent: "space-between",
      alignItems: "center",
      marginBottom: "25px",
      flexWrap: "wrap",
      gap: "15px",
    },
    title: { margin: 0, fontSize: "24px", color: "#1e293b", fontWeight: "800" },
    formCard: {
      background: "#ffffff",
      borderRadius: "12px",
      padding: "24px",
      boxShadow: "0 4px 6px -1px rgba(0,0,0,0.05)",
      border: "1px solid #e2e8f0",
      marginBottom: "25px",
      display: "flex",
      flexDirection: "column",
      gap: "20px",
    },
    inputRow: { display: "flex", gap: "15px", flexWrap: "wrap" },
    selectGroup: { flex: 1, minWidth: "220px", display: "flex", flexDirection: "column", gap: "6px" },
    label: { fontSize: "13px", fontWeight: "600", color: "#64748b" },
    select: {
      padding: "11px",
      borderRadius: "8px",
      border: "1px solid #cbd5e0",
      outline: "none",
      backgroundColor: "#fff",
      fontSize: "14px",
    },
    thuSection: { display: "flex", flexDirection: "column", gap: "10px" },
    thuHeader: { display: "flex", justifyContent: "space-between", alignItems: "center" },
    btnQuickGroup: { display: "flex", gap: "8px" },
    btnQuick: {
      background: "#f1f5f9",
      border: "none",
      color: "#2563eb",
      fontSize: "12px",
      fontWeight: "600",
      padding: "6px 12px",
      borderRadius: "6px",
      cursor: "pointer",
    },
    thuContainer: { display: "flex", gap: "8px", flexWrap: "wrap" },
    thuChip: {
      display: "inline-flex",
      alignItems: "center",
      padding: "8px 14px",
      borderRadius: "8px",
      border: "1px solid #cbd5e0",
      background: "#fff",
      color: "#475569",
      fontSize: "13px",
      fontWeight: "600",
      cursor: "pointer",
      userSelect: "none",
    },
    thuChipActive: {
      background: "#eff6ff",
      borderColor: "#2563eb",
      color: "#2563eb",
      boxShadow: "0 2px 4px rgba(37, 99, 235, 0.12)",
    },
    btnPrimary: {
      background: editingId ? "#ed8936" : "#2563eb",
      color: "#fff",
      padding: "11px 22px",
      borderRadius: "8px",
      border: "none",
      fontWeight: "bold",
      cursor: "pointer",
    },
    btnReset: {
      background: "#f8fafc",
      color: "#475569",
      padding: "11px 22px",
      borderRadius: "8px",
      border: "1px solid #cbd5e0",
      fontWeight: "bold",
      cursor: "pointer",
    },
    btnExcel: {
      padding: "10px 16px",
      border: "none",
      borderRadius: "8px",
      fontWeight: "bold",
      cursor: "pointer",
      fontSize: "13px",
      display: "inline-flex",
      alignItems: "center",
      gap: "6px",
    },
    searchInput: {
      padding: "10px 15px",
      borderRadius: "8px",
      border: "1px solid #cbd5e0",
      width: "260px",
      outline: "none",
      backgroundColor: "#fff",
    },
    // Matrix Table Styling
    matrixContainer: {
      background: "#fff",
      borderRadius: "12px",
      border: "1px solid #e2e8f0",
      boxShadow: "0 4px 6px -1px rgba(0,0,0,0.05)",
      overflowX: "auto",
    },
    table: {
      width: "100%",
      borderCollapse: "collapse",
      minWidth: "1000px",
    },
    thDoctor: {
      padding: "14px 16px",
      background: "#f1f5f9",
      color: "#334155",
      textAlign: "left",
      fontWeight: "700",
      fontSize: "14px",
      borderBottom: "2px solid #e2e8f0",
      width: "220px",
      position: "sticky",
      left: 0,
      zIndex: 2,
    },
    thDay: {
      padding: "14px 10px",
      background: "#f8fafc",
      color: "#475569",
      textAlign: "center",
      fontWeight: "700",
      fontSize: "13px",
      borderBottom: "2px solid #e2e8f0",
      borderLeft: "1px solid #edf2f7",
      width: "12%",
    },
    tdDoctor: {
      padding: "14px 16px",
      fontWeight: "600",
      color: "#1e293b",
      borderBottom: "1px solid #e2e8f0",
      background: "#fff",
      position: "sticky",
      left: 0,
      zIndex: 1,
      fontSize: "14px",
    },
    tdDay: {
      padding: "10px 8px",
      borderBottom: "1px solid #e2e8f0",
      borderLeft: "1px solid #edf2f7",
      verticalAlign: "top",
      textAlign: "center",
      minHeight: "60px",
    },
    caBadge: {
      display: "flex",
      alignItems: "center",
      justifyContent: "space-between",
      background: "#eff6ff",
      color: "#2563eb",
      padding: "6px 8px",
      borderRadius: "6px",
      fontSize: "12px",
      fontWeight: "600",
      marginBottom: "6px",
      border: "1px solid #bfdbfe",
    },
    actionIcon: { marginLeft: "4px", cursor: "pointer", fontSize: "13px" },
    pageBtn: {
      padding: "8px 14px",
      borderRadius: "6px",
      border: "1px solid #cbd5e0",
      cursor: "pointer",
      fontWeight: "bold",
    },
  };

  return (
    <div style={styles.container}>
      {/* HEADER */}
      <div style={styles.headerSection}>
        <div>
          <h2 style={styles.title}>📅 Bảng Phân Ca Làm Việc Theo Tuần</h2>
          <p style={{ color: "#64748b", marginTop: "4px", fontSize: "14px" }}>
            Quản lý ma trận phân ca trực cố định từ Thứ 2 đến Chủ Nhật cho Bác sĩ
          </p>
        </div>

        <div style={{ display: "flex", gap: "10px", alignItems: "center" }}>
          <input
            style={styles.searchInput}
            placeholder="🔍 Tìm tên bác sĩ..."
            value={search}
            onChange={(e) => setSearch(e.target.value)}
          />

          <button
            onClick={handleExportExcel}
            style={{ ...styles.btnExcel, backgroundColor: "#16a34a", color: "#fff" }}
          >
            📊 Xuất Excel
          </button>

          <button
            onClick={() => fileInputRef.current?.click()}
            disabled={loading}
            style={{ ...styles.btnExcel, backgroundColor: "#d97706", color: "#fff" }}
          >
            📥 Nhập Excel
          </button>
          <input
            type="file"
            ref={fileInputRef}
            onChange={handleImportExcel}
            accept=".xlsx, .xls"
            style={{ display: "none" }}
          />
        </div>
      </div>

      {/* FORM PHÂN LỊCH HÀNG LOẠT */}
      <div style={styles.formCard}>
        <div style={styles.inputRow}>
          <div style={styles.selectGroup}>
            <label style={styles.label}>Bác sĩ</label>
            <select
              style={styles.select}
              value={form.bacSiId}
              onChange={(e) => setForm({ ...form, bacSiId: e.target.value })}
            >
              <option value="">-- Chọn bác sĩ --</option>
              {bacSiList.map((bs) => (
                <option key={bs.id} value={bs.id}>
                  {bs.hoTen || bs.tenBacSi}
                </option>
              ))}
            </select>
          </div>

          <div style={styles.selectGroup}>
            <label style={styles.label}>Ca trực</label>
            <select
              style={styles.select}
              value={form.caLamViecId}
              onChange={(e) => setForm({ ...form, caLamViecId: e.target.value })}
            >
              <option value="">-- Chọn ca trực --</option>
              {caList.map((ca) => (
                <option key={ca.id} value={ca.id}>
                  {ca.tenCa}
                </option>
              ))}
            </select>
          </div>
        </div>

        {/* CHỌN NHIỀU THỨ */}
        <div style={styles.thuSection}>
          <div style={styles.thuHeader}>
            <label style={styles.label}>Chọn các thứ phân ca:</label>
            <div style={styles.btnQuickGroup}>
              <button type="button" style={styles.btnQuick} onClick={selectT2ToT6}>
                T2 → T6
              </button>
              <button type="button" style={styles.btnQuick} onClick={selectAllThu}>
                {form.danhSachThu.length === THU_OPTIONS.length ? "Bỏ chọn tất cả" : "Chọn tất cả"}
              </button>
            </div>
          </div>

          <div style={styles.thuContainer}>
            {THU_OPTIONS.map((item) => {
              const active = form.danhSachThu.includes(item.value);
              return (
                <label
                  key={item.value}
                  style={{
                    ...styles.thuChip,
                    ...(active ? styles.thuChipActive : {}),
                  }}
                >
                  <input
                    type="checkbox"
                    checked={active}
                    onChange={() => toggleThu(item.value)}
                    style={{ marginRight: "6px", cursor: "pointer" }}
                  />
                  {item.label}
                </label>
              );
            })}
          </div>
        </div>

        {/* ACTIONS */}
        <div style={{ display: "flex", gap: "10px", justifyContent: "flex-end", marginTop: "5px" }}>
          <button style={styles.btnReset} onClick={handleReset}>
            {editingId ? "Hủy sửa" : "Làm mới"}
          </button>
          <button style={styles.btnPrimary} onClick={handleSubmit} disabled={loading}>
            {loading
              ? "Đang xử lý..."
              : editingId
              ? "Cập nhật lịch"
              : `Thêm lịch (${form.danhSachThu.length} thứ)`}
          </button>
        </div>
      </div>

      {/* BẢNG MA TRẬN 7 NGÀY */}
      <div style={styles.matrixContainer}>
        <table style={styles.table}>
          <thead>
            <tr>
              <th style={styles.thDoctor}>👨‍⚕️ Bác sĩ</th>
              {THU_OPTIONS.map((thu) => (
                <th key={thu.value} style={styles.thDay}>
                  {thu.label}
                </th>
              ))}
            </tr>
          </thead>
          <tbody>
            {paginatedData.length > 0 ? (
              paginatedData.map((bs) => (
                <tr key={bs.bacSiId}>
                  <td style={styles.tdDoctor}>{bs.tenBacSi}</td>
                  {THU_OPTIONS.map((thu) => {
                    const items = bs.schedules[thu.value] || [];
                    return (
                      <td key={thu.value} style={styles.tdDay}>
                        {items.length > 0 ? (
                          items.map((caItem) => (
                            <div key={caItem.id} style={styles.caBadge}>
                              <span>{caMap.get(caItem.caLamViecId) || caItem.tenCa || "Ca trực"}</span>
                              <div>
                                <b
                                  style={{ ...styles.actionIcon, color: "#d97706" }}
                                  onClick={() => handleEdit(caItem)}
                                  title="Sửa"
                                >
                                  ✎
                                </b>
                                <b
                                  style={{ ...styles.actionIcon, color: "#e11d48" }}
                                  onClick={() => handleDelete(caItem.id)}
                                  title="Xóa"
                                >
                                  ×
                                </b>
                              </div>
                            </div>
                          ))
                        ) : (
                          <span style={{ color: "#cbd5e0", fontSize: "12px" }}>-</span>
                        )}
                      </td>
                    );
                  })}
                </tr>
              ))
            ) : (
              <tr>
                <td colSpan="8" style={{ textAlign: "center", padding: "30px", color: "#94a3b8" }}>
                  Không tìm thấy bác sĩ hoặc lịch làm việc nào.
                </td>
              </tr>
            )}
          </tbody>
        </table>
      </div>

      {/* PAGINATION */}
      {totalPages > 1 && (
        <div style={{ marginTop: "25px", display: "flex", gap: "8px", justifyContent: "center" }}>
          {Array.from({ length: totalPages }).map((_, i) => (
            <button
              key={i}
              onClick={() => setCurrentPage(i + 1)}
              style={{
                ...styles.pageBtn,
                backgroundColor: currentPage === i + 1 ? "#2563eb" : "#fff",
                color: currentPage === i + 1 ? "#fff" : "#475569",
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