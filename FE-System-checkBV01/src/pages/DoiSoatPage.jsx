import React, { useState } from "react";
import doiSoatService from "../services/doiSoatService";

const DoiSoatPage = () => {
  const [winSoftFile, setWinSoftFile] = useState(null);
  const [gateBHYTFile, setGateBHYTFile] = useState(null);
  const [loading, setLoading] = useState(false);
  const [message, setMessage] = useState("");

  const handleCompareAndDownload = async (e) => {
    e.preventDefault();
    if (!winSoftFile || !gateBHYTFile) {
      setMessage("Lỗi: Vui lòng chọn đầy đủ cả 2 file Excel (WinSoft và Gate BHYT)!");
      return;
    }

    setLoading(true);
    setMessage("");

    try {
      const blobData = await doiSoatService.exportCompareTxt(winSoftFile, gateBHYTFile);
      
      const url = window.URL.createObjectURL(new Blob([blobData], { type: "text/plain;charset=utf-8" }));
      const a = document.createElement("a");
      a.href = url;
      a.download = "ketqua_doisoat.txt";
      document.body.appendChild(a);
      a.click();
      a.remove();
      window.URL.revokeObjectURL(url);

      setMessage("Đối chiếu thành công! File báo cáo 'ketqua_doisoat.txt' đã được tải xuống.");
    } catch (error) {
      console.error(error);
      setMessage("Lỗi: Không thể xử lý phản hồi từ Server.");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div style={styles.container}>
      <div style={styles.card}>
        <div style={styles.headerBadge}>Hệ thống chuyên dụng BHYT</div>
        <h2 style={styles.title}>Đối Chiếu Dữ Liệu BHYT</h2>
        <p style={styles.subtitle}>
          Tải lên file Excel từ hệ thống WinSoft và Cổng BHYT để kiểm tra mã bệnh nhân, rà soát lệch số liệu và xuất file báo cáo chi tiết tự động.
        </p>

        <form onSubmit={handleCompareAndDownload} style={styles.form}>
          {/* Ô chọn File WinSoft */}
          <div style={styles.inputGroup}>
            <label style={styles.label}>1. File Excel WinSoft</label>
            <div style={{
              ...styles.dropZone,
              borderColor: winSoftFile ? "#d97706" : "#cbd5e1",
              background: winSoftFile ? "#fffbeb" : "#f8fafc"
            }}>
              <input 
                type="file" 
                accept=".xls, .xlsx" 
                onChange={(e) => setWinSoftFile(e.target.files[0])} 
                style={styles.fileInputHidden}
              />
              <div style={styles.dropZoneContent}>
                <span style={styles.uploadIcon}>📄</span>
                <span style={styles.fileMainText}>
                  {winSoftFile ? winSoftFile.name : "Chọn hoặc kéo thả file WinSoft vào đây"}
                </span>
                <span style={styles.fileSubText}>Hỗ trợ định dạng .xls, .xlsx</span>
              </div>
            </div>
          </div>

          {/* Ô chọn File Gate BHYT */}
          <div style={styles.inputGroup}>
            <label style={styles.label}>2. File Excel Gate BHYT</label>
            <div style={{
              ...styles.dropZone,
              borderColor: gateBHYTFile ? "#d97706" : "#cbd5e1",
              background: gateBHYTFile ? "#fffbeb" : "#f8fafc"
            }}>
              <input 
                type="file" 
                accept=".xls, .xlsx" 
                onChange={(e) => setGateBHYTFile(e.target.files[0])} 
                style={styles.fileInputHidden}
              />
              <div style={styles.dropZoneContent}>
                <span style={styles.uploadIcon}>📊</span>
                <span style={styles.fileMainText}>
                  {gateBHYTFile ? gateBHYTFile.name : "Chọn hoặc kéo thả file Gate BHYT vào đây"}
                </span>
                <span style={styles.fileSubText}>Hỗ trợ định dạng .xls, .xlsx</span>
              </div>
            </div>
          </div>

          <button 
            type="submit" 
            disabled={loading} 
            style={{
              ...styles.button,
              opacity: loading ? 0.7 : 1,
              cursor: loading ? "not-allowed" : "pointer"
            }}
          >
            {loading ? (
              <span style={styles.loadingWrapper}>
                <span style={styles.spinner}></span> Đang tiến hành đối chiếu dữ liệu...
              </span>
            ) : (
              "Chạy Đối Chiếu & Tải Báo Cáo (.txt)"
            )}
          </button>
        </form>

        {message && (
          <div style={{
            ...styles.message, 
            color: message.includes("Lỗi") ? "#991b1b" : "#065f46",
            background: message.includes("Lỗi") ? "#fef2f2" : "#ecfdf5",
            borderColor: message.includes("Lỗi") ? "#fecaca" : "#a7f3d0"
          }}>
            {message.includes("Lỗi") ? "⚠️ " : "✅ "} {message}
          </div>
        )}
      </div>
    </div>
  );
};

const styles = {
  container: { 
    maxWidth: "850px", 
    margin: "40px auto", 
    padding: "0 20px",
    fontFamily: "'Montserrat', sans-serif" 
  },
  card: { 
    background: "#ffffff", 
    padding: "40px", 
    borderRadius: "20px", 
    boxShadow: "0 10px 30px rgba(15, 23, 42, 0.08)", 
    border: "1px solid #e2e8f0",
    position: "relative",
    overflow: "hidden"
  },
  headerBadge: {
    display: "inline-block",
    padding: "6px 14px",
    background: "#f8fafc",
    color: "#b45309",
    borderRadius: "30px",
    fontSize: "12px",
    fontWeight: "700",
    textTransform: "uppercase",
    letterSpacing: "0.5px",
    marginBottom: "15px",
    border: "1px solid #fef3c7"
  },
  title: { 
    margin: "0 0 10px 0", 
    fontSize: "26px", 
    fontWeight: "700", 
    color: "#0f172a",
    fontFamily: "'Playfair Display', serif"
  },
  subtitle: { 
    margin: "0 0 30px 0", 
    fontSize: "14px", 
    color: "#475569", 
    lineHeight: "1.6" 
  },
  form: { 
    display: "flex", 
    flexDirection: "column", 
    gap: "24px" 
  },
  inputGroup: { 
    display: "flex", 
    flexDirection: "column", 
    gap: "8px" 
  },
  label: { 
    fontSize: "14px", 
    fontWeight: "600", 
    color: "#1e293b" 
  },
  dropZone: {
    position: "relative",
    border: "2px dashed #cbd5e1",
    borderRadius: "12px",
    padding: "20px",
    textAlign: "center",
    transition: "all 0.3s ease",
    cursor: "pointer"
  },
  fileInputHidden: {
    position: "absolute",
    top: 0,
    left: 0,
    width: "100%",
    height: "100%",
    opacity: 0,
    cursor: "pointer",
    zIndex: 2
  },
  dropZoneContent: {
    display: "flex",
    flexDirection: "column",
    alignItems: "center",
    gap: "6px",
    pointerEvents: "none"
  },
  uploadIcon: {
    fontSize: "28px",
    marginBottom: "2px"
  },
  fileMainText: {
    fontSize: "14px",
    fontWeight: "600",
    color: "#0f172a",
    wordBreak: "break-all"
  },
  fileSubText: {
    fontSize: "12px",
    color: "#64748b"
  },
  button: { 
    padding: "16px", 
    background: "linear-gradient(135deg, #1e293b 0%, #334155 100%)", 
    color: "#ffffff", 
    border: "none", 
    borderRadius: "12px", 
    fontSize: "15px", 
    fontWeight: "600", 
    boxShadow: "0 4px 14px rgba(30, 41, 59, 0.2)", 
    marginTop: "10px",
    transition: "transform 0.2s ease, box-shadow 0.2s ease"
  },
  loadingWrapper: {
    display: "flex",
    alignItems: "center",
    justifyContent: "center",
    gap: "10px"
  },
  spinner: {
    width: "18px",
    height: "18px",
    border: "3px solid rgba(255,255,255,0.3)",
    borderRadius: "50%",
    borderTopColor: "#fff",
    animation: "spin 0.8s linear infinite"
  },
  message: { 
    marginTop: "24px", 
    padding: "14px 18px", 
    borderRadius: "10px", 
    fontSize: "14px", 
    fontWeight: "500", 
    textAlign: "center",
    border: "1px solid",
    display: "flex",
    alignItems: "center",
    justifyContent: "center",
    gap: "8px"
  }
};

export default DoiSoatPage;