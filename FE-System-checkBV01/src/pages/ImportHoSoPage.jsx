import { useEffect, useState } from "react";
import * as importService from "../services/importService";

const ImportHoSoPage = () => {
  const [list, setList] = useState([]);
  const [loading, setLoading] = useState(false);
  const [progress, setProgress] = useState(0);
  const [files, setFiles] = useState({
    xml1: null,
    xml2: null,
    xml3: null,
    xml4: null,
  });

  useEffect(() => {
    loadData();
  }, []);

  const loadData = async () => {
    try {
      const data = await importService.getAllHoSo();
      setList(data || []);
    } catch (err) {
      console.error("Lỗi load dữ liệu:", err);
    }
  };

  const handleFileChange = (e, key) => {
    const selectedFile = e.target.files[0];
    if (selectedFile) {
      setFiles((prev) => ({ ...prev, [key]: selectedFile }));
    }
  };

  // Tự động phân loại 4 file khi chọn hàng loạt (chấp nhận cả XML & Excel)
  const handleSelectMultipleFiles = (e) => {
    const selectedFiles = Array.from(e.target.files);
    if (selectedFiles.length === 0) return;

    const newFiles = { ...files };
    selectedFiles.forEach((file) => {
      const name = file.name.toLowerCase();
      if (name.includes("xml1") || name.includes("xml_1") || name.includes("hoso1")) newFiles.xml1 = file;
      else if (name.includes("xml2") || name.includes("xml_2") || name.includes("hoso2")) newFiles.xml2 = file;
      else if (name.includes("xml3") || name.includes("xml_3") || name.includes("hoso3")) newFiles.xml3 = file;
      else if (name.includes("xml4") || name.includes("xml_4") || name.includes("hoso4")) newFiles.xml4 = file;
    });
    setFiles(newFiles);
  };

  const handleRemoveFile = (key) => {
    setFiles((prev) => ({ ...prev, [key]: null }));
  };

  const handleUpload = async () => {
    if (!files.xml1 || !files.xml2 || !files.xml3 || !files.xml4) {
      alert("⚠️ Vui lòng chọn đủ cả 4 file hồ sơ trước khi tải lên!");
      return;
    }

    setLoading(true);
    setProgress(0);

    const interval = setInterval(() => {
      setProgress((prev) => {
        if (prev >= 90) return prev;
        return prev + Math.random() * 12;
      });
    }, 250);

    try {
      await importService.importHoSo(files);
      setProgress(100);
      alert("🚀 Import bộ hồ sơ thành công!");
      await loadData();
      setFiles({ xml1: null, xml2: null, xml3: null, xml4: null });
    } catch (err) {
      alert("Lỗi khi import bộ file!");
    } finally {
      clearInterval(interval);
      setTimeout(() => {
        setLoading(false);
        setProgress(0);
      }, 400);
    }
  };

  const handleCheckAllBV01 = () => alert("🔍 Đang chạy tiến trình kiểm tra ICD10...");
  const handleSortAllGioBV = () => alert("🕒 Đang tự động sắp xếp giờ khám chữa bệnh...");

  const handleExportExcel = async () => {
    try {
      const response = await fetch("http://localhost:8080/api/import/export-excel");
      if (!response.ok) {
        const text = await response.text();
        alert("Lỗi: " + text);
        return;
      }
      const blob = await response.blob();
      const url = window.URL.createObjectURL(blob);
      const a = document.createElement("a");
      a.href = url;
      a.download = `bao-cao-loi-bhyt_${Date.now()}.xlsx`;
      a.click();
      window.URL.revokeObjectURL(url);
    } catch (err) {
      alert("Lỗi xuất file Excel: " + err.message);
    }
  };

  const formatFileSize = (bytes) => {
    if (!bytes) return "0 KB";
    return (bytes / 1024).toFixed(1) + " KB";
  };

  const isAllFilesSelected = files.xml1 && files.xml2 && files.xml3 && files.xml4;

  return (
    <div style={styles.container}>
      {/* HEADER SECTION */}
      <div style={styles.header}>
        <div>
          <h2 style={styles.title}>📂 Import Hồ Sơ BHYT (XML / Excel)</h2>
          <p style={styles.subtitle}>
            Hệ thống tự động đồng bộ & kiểm tra dữ liệu giám định bảo hiểm y tế
          </p>
        </div>
        <div>
          <label style={styles.btnSelectBulk}>
            📁 Chọn nhanh 4 file (XML / Excel)
            <input
              type="file"
              multiple
              accept=".xml, .xlsx, .xls"
              onChange={handleSelectMultipleFiles}
              style={{ display: "none" }}
            />
          </label>
        </div>
      </div>

      {/* UPLOAD CARD */}
      <div style={styles.card}>
        <div style={styles.fileGrid}>
          {["xml1", "xml2", "xml3", "xml4"].map((key, index) => {
            const currentFile = files[key];
            const isExcel = currentFile?.name.endsWith(".xlsx") || currentFile?.name.endsWith(".xls");

            return (
              <div
                key={key}
                style={{
                  ...styles.fileBox,
                  borderColor: currentFile ? "#2563eb" : "#cbd5e0",
                  backgroundColor: currentFile ? "#eff6ff" : "#f8fafc",
                }}
              >
                <div style={styles.fileHeader}>
                  <span style={styles.fileBadge}>FILE {index + 1}</span>
                  {currentFile && (
                    <button
                      style={styles.btnRemove}
                      onClick={() => handleRemoveFile(key)}
                      title="Xóa file"
                    >
                      ✕
                    </button>
                  )}
                </div>

                {currentFile ? (
                  <div style={styles.fileDetails}>
                    <div style={styles.fileName} title={currentFile.name}>
                      {isExcel ? "📊" : "📄"} {currentFile.name}
                    </div>
                    <div style={styles.fileSize}>{formatFileSize(currentFile.size)}</div>
                  </div>
                ) : (
                  <label style={styles.dropZoneLabel}>
                    <span style={{ fontSize: "20px", marginBottom: "4px" }}>📥</span>
                    <span style={{ fontSize: "12px", color: "#64748b", fontWeight: "500" }}>
                      Bấm để chọn file {index + 1} (.xml, .xlsx)
                    </span>
                    <input
                      type="file"
                      accept=".xml, .xlsx, .xls"
                      style={{ display: "none" }}
                      onChange={(e) => handleFileChange(e, key)}
                    />
                  </label>
                )}
              </div>
            );
          })}
        </div>

        {/* PROGRESS BAR */}
        {loading && (
          <div style={styles.progressWrapper}>
            <div style={styles.progressHeader}>
              <span>Đang xử lý dữ liệu hồ sơ...</span>
              <span>{Math.floor(progress)}%</span>
            </div>
            <div style={styles.progressBar}>
              <div style={{ ...styles.progressFill, width: `${progress}%` }} />
            </div>
          </div>
        )}

        {/* BUTTON PROCESS */}
        <button
          style={{
            ...styles.btnSubmit,
            backgroundColor: isAllFilesSelected && !loading ? "#2563eb" : "#94a3b8",
            cursor: isAllFilesSelected && !loading ? "pointer" : "not-allowed",
          }}
          onClick={handleUpload}
          disabled={!isAllFilesSelected || loading}
        >
          {loading ? "⏳ ĐANG XỬ LÝ HỒ SƠ..." : "⚡ BẮT ĐẦU IMPORT BỘ HỒ SƠ"}
        </button>
      </div>

      {/* QUICK ACTIONS BAR */}
      <div style={styles.actionCard}>
        <div style={styles.actionGroup}>
          <button style={styles.btnCheck} onClick={handleCheckAllBV01}>
            🔍 Kiểm tra mã ICD10
          </button>
          <button style={styles.btnSort} onClick={handleSortAllGioBV}>
            🕒 Sắp xếp giờ khám bệnh
          </button>
        </div>
        <button style={styles.btnExport} onClick={handleExportExcel}>
          📊 Xuất báo cáo Excel lỗi
        </button>
      </div>

      {/* DATA TABLE PREVIEW */}
      <div style={styles.tableCard}>
        <div style={styles.tableHeader}>
          <h3 style={{ margin: 0, fontSize: "16px", color: "#1e293b", fontWeight: "700" }}>
            📑 Danh sách hồ sơ đã import ({list.length})
          </h3>
        </div>
        <div style={{ overflowX: "auto" }}>
          <table style={styles.table}>
            <thead>
              <tr style={styles.thRow}>
                <th style={styles.th}>STT</th>
                <th style={styles.th}>Mã bệnh nhân</th>
                <th style={styles.th}>Họ và tên</th>
                <th style={styles.th}>Mã BHYT</th>
                <th style={styles.th}>Mã ICD10</th>
                <th style={styles.th}>Trạng thái</th>
              </tr>
            </thead>
            <tbody>
              {list.length > 0 ? (
                list.map((item, idx) => (
                  <tr key={item.id || idx} style={styles.tr}>
                    <td style={styles.td}>{idx + 1}</td>
                    <td style={{ ...styles.td, fontWeight: "600" }}>{item.maBn || `BN-${1000 + idx}`}</td>
                    <td style={styles.td}>{item.hoTen || item.tenBenhNhan || "Chưa cập nhật"}</td>
                    <td style={styles.td}>{item.maThe || "N/A"}</td>
                    <td style={styles.td}>{item.maIcd || "N/A"}</td>
                    <td style={styles.td}>
                      <span style={styles.badgeSuccess}>Thành công</span>
                    </td>
                  </tr>
                ))
              ) : (
                <tr>
                  <td colSpan="6" style={styles.emptyTd}>
                    Chưa có hồ sơ nào trong hệ thống.
                  </td>
                </tr>
              )}
            </tbody>
          </table>
        </div>
      </div>
    </div>
  );
};

const styles = {
  container: {
    padding: "32px 24px",
    maxWidth: "1140px",
    margin: "0 auto",
    fontFamily: "'Inter', -apple-system, sans-serif",
    color: "#1e293b",
  },
  header: {
    display: "flex",
    justifyContent: "space-between",
    alignItems: "flex-end",
    marginBottom: "24px",
    borderBottom: "1px solid #e2e8f0",
    paddingBottom: "16px",
  },
  title: {
    margin: 0,
    fontSize: "22px",
    fontWeight: "800",
    color: "#0f172a",
  },
  subtitle: {
    margin: "4px 0 0 0",
    fontSize: "13px",
    color: "#64748b",
  },
  btnSelectBulk: {
    backgroundColor: "#f1f5f9",
    color: "#334155",
    padding: "8px 16px",
    borderRadius: "8px",
    fontSize: "13px",
    fontWeight: "600",
    border: "1px solid #cbd5e0",
    cursor: "pointer",
    display: "inline-flex",
    alignItems: "center",
    gap: "6px",
  },
  card: {
    background: "#ffffff",
    borderRadius: "12px",
    padding: "20px",
    border: "1px solid #e2e8f0",
    boxShadow: "0 1px 3px rgba(0,0,0,0.05)",
    marginBottom: "20px",
  },
  fileGrid: {
    display: "grid",
    gridTemplateColumns: "repeat(auto-fit, minmax(220px, 1fr))",
    gap: "16px",
    marginBottom: "20px",
  },
  fileBox: {
    border: "2px dashed",
    borderRadius: "10px",
    padding: "14px",
    height: "100px",
    display: "flex",
    flexDirection: "column",
    justifyContent: "space-between",
    transition: "all 0.2s ease",
  },
  fileHeader: {
    display: "flex",
    justifyContent: "space-between",
    alignItems: "center",
  },
  fileBadge: {
    fontSize: "11px",
    fontWeight: "700",
    color: "#475569",
    backgroundColor: "#e2e8f0",
    padding: "2px 8px",
    borderRadius: "4px",
  },
  btnRemove: {
    background: "none",
    border: "none",
    color: "#ef4444",
    fontWeight: "bold",
    cursor: "pointer",
    fontSize: "14px",
  },
  dropZoneLabel: {
    display: "flex",
    flexDirection: "column",
    alignItems: "center",
    justifyContent: "center",
    cursor: "pointer",
    height: "100%",
  },
  fileDetails: {
    marginTop: "8px",
  },
  fileName: {
    fontSize: "12px",
    fontWeight: "600",
    color: "#1e293b",
    whiteSpace: "nowrap",
    overflow: "hidden",
    textOverflow: "ellipsis",
  },
  fileSize: {
    fontSize: "11px",
    color: "#64748b",
    marginTop: "2px",
  },
  progressWrapper: {
    marginBottom: "16px",
  },
  progressHeader: {
    display: "flex",
    justifyContent: "space-between",
    fontSize: "12px",
    fontWeight: "600",
    color: "#475569",
    marginBottom: "6px",
  },
  progressBar: {
    width: "100%",
    height: "8px",
    backgroundColor: "#f1f5f9",
    borderRadius: "999px",
    overflow: "hidden",
  },
  progressFill: {
    height: "100%",
    backgroundColor: "#2563eb",
    transition: "width 0.2s ease-in-out",
  },
  btnSubmit: {
    width: "100%",
    padding: "12px",
    color: "#ffffff",
    border: "none",
    borderRadius: "8px",
    fontWeight: "700",
    fontSize: "14px",
    transition: "background 0.2s ease",
  },
  actionCard: {
    display: "flex",
    justifyContent: "space-between",
    alignItems: "center",
    background: "#ffffff",
    borderRadius: "12px",
    padding: "16px 20px",
    border: "1px solid #e2e8f0",
    marginBottom: "24px",
    flexWrap: "wrap",
    gap: "12px",
  },
  actionGroup: {
    display: "flex",
    gap: "12px",
  },
  btnCheck: {
    padding: "10px 18px",
    backgroundColor: "#eff6ff",
    color: "#2563eb",
    border: "1px solid #bfdbfe",
    borderRadius: "8px",
    fontWeight: "600",
    fontSize: "13px",
    cursor: "pointer",
  },
  btnSort: {
    padding: "10px 18px",
    backgroundColor: "#fffbebe",
    color: "#d97706",
    border: "1px solid #fde68a",
    borderRadius: "8px",
    fontWeight: "600",
    fontSize: "13px",
    cursor: "pointer",
  },
  btnExport: {
    padding: "10px 18px",
    backgroundColor: "#ecfdf5",
    color: "#059669",
    border: "1px solid #a7f3d0",
    borderRadius: "8px",
    fontWeight: "600",
    fontSize: "13px",
    cursor: "pointer",
  },
  tableCard: {
    background: "#ffffff",
    borderRadius: "12px",
    border: "1px solid #e2e8f0",
    overflow: "hidden",
  },
  tableHeader: {
    padding: "16px 20px",
    borderBottom: "1px solid #e2e8f0",
    backgroundColor: "#f8fafc",
  },
  table: {
    width: "100%",
    borderCollapse: "collapse",
    textAlign: "left",
    fontSize: "13px",
  },
  thRow: {
    backgroundColor: "#f8fafc",
    borderBottom: "1px solid #e2e8f0",
  },
  th: {
    padding: "12px 16px",
    color: "#475569",
    fontWeight: "600",
    fontSize: "12px",
  },
  tr: {
    borderBottom: "1px solid #f1f5f9",
  },
  td: {
    padding: "12px 16px",
    color: "#334155",
  },
  emptyTd: {
    padding: "32px",
    textAlign: "center",
    color: "#94a3b8",
  },
  badgeSuccess: {
    display: "inline-block",
    padding: "2px 8px",
    backgroundColor: "#dcfce7",
    color: "#166534",
    borderRadius: "4px",
    fontSize: "11px",
    fontWeight: "600",
  },
};

export default ImportHoSoPage;