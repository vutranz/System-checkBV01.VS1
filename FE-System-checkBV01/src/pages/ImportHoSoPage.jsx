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
    xml4: null
  });

  useEffect(() => {
    loadData();
  }, []);

  const loadData = async () => {
    try {
      const data = await importService.getAllHoSo();
      setList(data || []);
    } catch (err) {
      console.error("Lỗi load dữ liệu", err);
    }
  };

  const handleFileChange = (e, key) => {
    setFiles({ ...files, [key]: e.target.files[0] });
  };

  const handleUpload = async () => {
    if (!files.xml1 || !files.xml2 || !files.xml3 || !files.xml4) {
      alert("⚠️ Vui lòng chọn đủ 4 file XML");
      return;
    }

    setLoading(true);
    setProgress(0);

    // 🔥 fake loading
    const interval = setInterval(() => {
      setProgress(prev => {
        if (prev >= 90) return prev;
        return prev + Math.random() * 10;
      });
    }, 300);

    try {
      await importService.importHoSo(files);

      setProgress(100);
      alert("🚀 Import hồ sơ thành công!");
      loadData();

    } catch (err) {
      alert("Lỗi khi import file");
    } finally {
      clearInterval(interval);

      setTimeout(() => {
        setLoading(false);
        setProgress(0);
      }, 500);
    }
  };

  const handleCheckAllBV01 = () => {
    alert("Đang kiểm tra BV01...");
  };

  const handleSortAllGioBV = () => {
    alert("Đang sắp xếp giờ BV...");
  };

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
      a.download = `ket-qua_${Date.now()}.xlsx`;
      a.click();

      window.URL.revokeObjectURL(url);

    } catch (err) {
      alert("Lỗi export: " + err.message);
    }
  };

  const styles = {
    container: {
      padding: '40px',
      maxWidth: '1100px',
      margin: '0 auto',
      fontFamily: "'Inter', sans-serif"
    },
    header: {
      borderBottom: '2px solid #edf2f7',
      marginBottom: '30px',
      paddingBottom: '15px'
    },
    title: {
      margin: 0,
      fontSize: '24px',
      fontWeight: '800'
    },
    uploadCard: {
      background: '#fff',
      borderRadius: '12px',
      padding: '24px',
      border: '1px solid #e2e8f0',
      boxShadow: '0 4px 6px -1px rgba(0,0,0,0.1)',
      marginBottom: '30px'
    },
    fileGrid: {
      display: 'grid',
      gridTemplateColumns: 'repeat(4, 1fr)',
      gap: '15px',
      marginBottom: '20px'
    },
    fileBox: {
      border: '2px dashed #cbd5e0',
      padding: '15px',
      borderRadius: '8px',
      textAlign: 'center'
    },

    actionArea: {
      display: 'flex',
      gap: '15px',
      justifyContent: 'center',
      marginBottom: '25px',
      padding: '20px',
      background: '#f8fafc',
      borderRadius: '12px',
      border: '1px solid #e2e8f0'
    },

    btnCheck: {
      padding: '12px 25px',
      backgroundColor: '#3b82f6',
      color: 'white',
      border: 'none',
      borderRadius: '8px',
      fontWeight: 'bold',
      cursor: 'pointer'
    },

    btnSort: {
      padding: '12px 25px',
      backgroundColor: '#f59e0b',
      color: 'white',
      border: 'none',
      borderRadius: '8px',
      fontWeight: 'bold',
      cursor: 'pointer'
    },

    btnExport: {
      padding: '12px 25px',
      backgroundColor: '#10b981',
      color: 'white',
      border: 'none',
      borderRadius: '8px',
      fontWeight: 'bold',
      cursor: 'pointer'
    },

    progressWrapper: {
      marginTop: '15px'
    },

    progressBar: {
      width: '100%',
      height: '10px',
      background: '#e5e7eb',
      borderRadius: '999px',
      overflow: 'hidden'
    },

    progressFill: {
      height: '100%',
      background: 'linear-gradient(90deg, #10b981, #34d399)',
      transition: 'width 0.3s ease'
    },

    progressText: {
      marginTop: '5px',
      fontSize: '12px',
      fontWeight: 'bold',
      textAlign: 'right'
    }
  };

  return (
    <div style={styles.container}>
      <div style={styles.header}>
        <h2 style={styles.title}>📥 Import Hồ Sơ BHYT (XML 1-2-3-4)</h2>
      </div>

      {/* Upload */}
      <div style={styles.uploadCard}>
        <div style={styles.fileGrid}>
          {['xml1', 'xml2', 'xml3', 'xml4'].map((key, index) => (
            <div key={key} style={styles.fileBox}>
              <label style={{ fontSize: '11px', fontWeight: '800' }}>
                FILE XML {index + 1}
              </label>
              <input
                type="file"
                style={{ marginTop: '10px' }}
                onChange={(e) => handleFileChange(e, key)}
              />
            </div>
          ))}
        </div>

        <button
          style={{
            width: '100%',
            padding: '12px',
            background: '#10b981',
            color: '#fff',
            border: 'none',
            borderRadius: '8px',
            fontWeight: 'bold',
            cursor: 'pointer'
          }}
          onClick={handleUpload}
          disabled={loading}
        >
          {loading ? "⌛ ĐANG XỬ LÝ..." : "⚡ BẮT ĐẦU IMPORT"}
        </button>

        {/* 🔥 PROGRESS */}
        {loading && (
          <div style={styles.progressWrapper}>
            <div style={styles.progressBar}>
              <div
                style={{
                  ...styles.progressFill,
                  width: `${progress}%`
                }}
              />
            </div>
            <div style={styles.progressText}>
              {Math.floor(progress)}%
            </div>
          </div>
        )}
      </div>

      {/* Action */}
      <div style={styles.actionArea}>
        <button style={styles.btnCheck} onClick={handleCheckAllBV01}>
          🔍 Kiểm tra ICD10
        </button>

        <button style={styles.btnSort} onClick={handleSortAllGioBV}>
          🕒 Sắp xếp giờ BV
        </button>

        <button style={styles.btnExport} onClick={handleExportExcel}>
          📥 Xuất Excel lỗi
        </button>
      </div>
    </div>
  );
};

export default ImportHoSoPage;