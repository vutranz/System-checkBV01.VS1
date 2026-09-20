import { useEffect, useState, useMemo } from "react";
import * as utils from "xlsx"; // Import thư viện Excel
import * as nangLucService from "../services/nangLucService";
import * as dvktService from "../services/dichVuKyThuatService";
import * as bacSiService from "../services/bacSiService";

const BS_PAGE_SIZE = 5;
const NL_PAGE_SIZE = 5; 
const DVKT_PAGE_SIZE = 6;

const NangLucPage = () => {
  const [bacSiList, setBacSiList] = useState([]);
  const [selectedBacSi, setSelectedBacSi] = useState(null);
  const [nangLucList, setNangLucList] = useState([]);
  const [dvktList, setDvktList] = useState([]);

  const [search, setSearch] = useState(""); // Tìm bác sĩ
  const [nlSearch, setNlSearch] = useState(""); // Tìm năng lực đã gán
  const [dvktSearch, setDvktSearch] = useState(""); // Tìm DVKT để gán
  const [selectedParentId, setSelectedParentId] = useState("");

  const [bsPage, setBsPage] = useState(1);
  const [nlPage, setNlPage] = useState(1);
  const [dvktPage, setDvktPage] = useState(1);

  const [loading, setLoading] = useState(false);

  // Form hỗ trợ chọn nhiều DVKT cùng lúc
  const [selectedDvktIds, setSelectedDvktIds] = useState([]);
  const [vaiTro, setVaiTro] = useState("DOC_KQ");

  // ================= LOAD DATA =================
  useEffect(() => {
    const load = async () => {
      try {
        const bsRes = await bacSiService.getAll();
        const dvktRes = await dvktService.getAll();
        setBacSiList(Array.isArray(bsRes) ? bsRes : bsRes?.data || []);
        setDvktList(Array.isArray(dvktRes) ? dvktRes : dvktRes?.data || []);
      } catch (err) {
        console.error("Lỗi load dữ liệu:", err);
      }
    };
    load();
  }, []);

  const loadNangLuc = async (bacSiId) => {
    try {
      const res = await nangLucService.getByBacSi(bacSiId);
      const list = Array.isArray(res) ? res : res?.data || [];
      setNangLucList(list);
      setNlPage(1);
      setNlSearch(""); 
      setSelectedDvktIds([]);
      setVaiTro("DOC_KQ");
    } catch (err) {
      setNangLucList([]);
    }
  };

  const handleSelectBacSi = async (bacSi) => {
    if (selectedBacSi?.id === bacSi.id) {
      setSelectedBacSi(null);
      setNangLucList([]);
    } else {
      setSelectedBacSi(bacSi);
      await loadNangLuc(bacSi.id);
    }
  };

  // ================= LOGIC GỘP & LỌC NĂNG LỰC (CỘT 2) =================
  const filteredGroupedNangLuc = useMemo(() => {
    const groups = nangLucList.reduce((acc, item) => {
      if (!acc[item.dvktId]) {
        acc[item.dvktId] = {
          dvktId: item.dvktId,
          tenDvkt: item.tenDvkt,
          roles: []
        };
      }
      acc[item.dvktId].roles.push({ id: item.id, vaiTro: item.vaiTro });
      return acc;
    }, {});

    const result = Object.values(groups);

    if (!nlSearch.trim()) return result;
    return result.filter(item => 
      item.tenDvkt?.toLowerCase().includes(nlSearch.toLowerCase())
    );
  }, [nangLucList, nlSearch]);

  // ================= PHÂN TRANG & TÌM KIẾM BÁC SĨ (CỘT 1) =================
  const filteredBacSi = useMemo(() => {
    return bacSiList.filter((b) =>
      b.hoTen?.toLowerCase().includes(search.toLowerCase())
    );
  }, [bacSiList, search]);

  useEffect(() => { setBsPage(1); }, [search]);
  useEffect(() => { setNlPage(1); }, [nlSearch]);
  useEffect(() => { setDvktPage(1); }, [dvktSearch, selectedParentId]);

  const totalBsPages = Math.ceil(filteredBacSi.length / BS_PAGE_SIZE) || 1;
  const pagedBacSi = filteredBacSi.slice((bsPage - 1) * BS_PAGE_SIZE, bsPage * BS_PAGE_SIZE);

  const totalNlPages = Math.ceil(filteredGroupedNangLuc.length / NL_PAGE_SIZE) || 1;
  const pagedGroupedNl = filteredGroupedNangLuc.slice((nlPage - 1) * NL_PAGE_SIZE, nlPage * NL_PAGE_SIZE);

  // ================= XỬ LÝ TREE DVKT (CỘT 3) =================
  const dvktTree = useMemo(() => {
    const map = {};
    const roots = [];
    dvktList.forEach((item) => { map[item.id] = { ...item, children: [] }; });
    dvktList.forEach((item) => {
      if (item.dvktChaId) map[item.dvktChaId]?.children.push(map[item.id]);
      else roots.push(map[item.id]);
    });
    return roots;
  }, [dvktList]);

  const allChildren = useMemo(() => {
    return dvktTree.flatMap((parent) =>
      parent.children.map((child) => ({
        ...child,
        parentName: parent.tenDvkt,
        parentCode: parent.maDvkt,
      }))
    );
  }, [dvktTree]);

  const availableChildren = useMemo(() => {
    if (!selectedBacSi) return [];
    const roleMap = nangLucList.reduce((acc, item) => {
      if (!acc[item.dvktId]) acc[item.dvktId] = new Set();
      acc[item.dvktId].add(item.vaiTro);
      return acc;
    }, {});

    let source = selectedParentId 
      ? (dvktTree.find(p => p.id === Number(selectedParentId))?.children || [])
      : allChildren;

    return source
      .filter((child) => {
        const roles = roleMap[child.id];
        return !(roles?.has("DOC_KQ") && roles?.has("THUC_HIEN"));
      })
      .filter((child) =>
        (`${child.tenDvkt} ${child.maDvkt}`).toLowerCase().includes(dvktSearch.toLowerCase())
      )
      .sort((a, b) => a.tenDvkt.localeCompare(b.tenDvkt));
  }, [selectedParentId, dvktTree, allChildren, nangLucList, dvktSearch, selectedBacSi]);

  const totalDvktPages = Math.ceil(availableChildren.length / DVKT_PAGE_SIZE) || 1;
  const pagedDvkt = availableChildren.slice((dvktPage - 1) * DVKT_PAGE_SIZE, dvktPage * DVKT_PAGE_SIZE);

  // Toggle chọn nhiều DVKT
  const handleToggleSelectDvkt = (id) => {
    setSelectedDvktIds((prev) =>
      prev.includes(id) ? prev.filter((item) => item !== id) : [...prev, id]
    );
  };

  const handleSelectAllCurrentPageDvkt = () => {
    const pageIds = pagedDvkt.map((c) => c.id);
    const isAllSelected = pageIds.every((id) => selectedDvktIds.includes(id));

    if (isAllSelected) {
      setSelectedDvktIds((prev) => prev.filter((id) => !pageIds.includes(id)));
    } else {
      setSelectedDvktIds((prev) => [...new Set([...prev, ...pageIds])]);
    }
  };

  // ================= LOGIC XUẤT FILE EXCEL =================
  
  // 1. Xuất Excel cho Bác sĩ đang chọn
  const handleExportSelectedBacSi = () => {
    if (!selectedBacSi || filteredGroupedNangLuc.length === 0) {
      return alert("Không có dữ liệu năng lực để xuất!");
    }

    const dataToExport = filteredGroupedNangLuc.map((item, index) => ({
      "STT": index + 1,
      "Tên Bác Sĩ": selectedBacSi.hoTen,
      "Tên Dịch Vụ Kỹ Thuật": item.tenDvkt,
      "Vai Trò": item.roles.map(r => r.vaiTro === "DOC_KQ" ? "Đọc KQ" : "Thực hiện").join(", ")
    }));

    const worksheet = utils.utils.json_to_sheet(dataToExport);
    const workbook = utils.utils.book_new();
    utils.utils.book_append_sheet(workbook, worksheet, "Năng Lực Bác Sĩ");

    const fileName = `NangLuc_${selectedBacSi.hoTen.replace(/\s+/g, "_")}.xlsx`;
    utils.writeFile(workbook, fileName);
  };

  // 2. Xuất Excel toàn bộ hệ thống
  const handleExportAll = async () => {
    try {
      setLoading(true);
      let list = [];
      if (typeof nangLucService.getAll === "function") {
        const res = await nangLucService.getAll();
        list = Array.isArray(res) ? res : res?.data || [];
      } else {
        alert("Chưa cấu hình API getAll cho nangLucService!");
        return;
      }

      if (list.length === 0) {
        return alert("Không có dữ liệu tổng hợp để xuất file!");
      }

      const dataToExport = list.map((item, index) => ({
        "STT": index + 1,
        "Tên Bác Sĩ": item.tenBacSi || item.bacSiHoTen || item.bacSiId,
        "Dịch Vụ Kỹ Thuật": item.tenDvkt || item.dvktId,
        "Vai Trò": item.vaiTro === "DOC_KQ" ? "Đọc KQ" : "Thực hiện"
      }));

      const worksheet = utils.utils.json_to_sheet(dataToExport);
      const workbook = utils.utils.book_new();
      utils.utils.book_append_sheet(workbook, worksheet, "TongHopNangLuc");

      utils.writeFile(workbook, "Bao_Cao_Tong_Hop_Nang_Luc.xlsx");
    } catch (err) {
      console.error("Lỗi xuất Excel:", err);
      alert("Xảy ra lỗi khi xuất file Excel!");
    } finally {
      setLoading(false);
    }
  };

  // ================= HÀNH ĐỘNG CRUD =================
  const handleSubmitBulk = async () => {
    if (!selectedBacSi || selectedDvktIds.length === 0) {
      return alert("Vui lòng chọn bác sĩ và ít nhất 1 dịch vụ kỹ thuật!");
    }

    try {
      setLoading(true);
      if (typeof nangLucService.createBulk === "function") {
        await nangLucService.createBulk({
          bacSiId: selectedBacSi.id,
          dvktIds: selectedDvktIds,
          vaiTro: vaiTro,
        });
      } else {
        const requests = selectedDvktIds.map((dvktId) =>
          nangLucService.create({
            bacSiId: selectedBacSi.id,
            dvktId: Number(dvktId),
            vaiTro: vaiTro,
          })
        );
        await Promise.all(requests);
      }
      setSelectedDvktIds([]);
      await loadNangLuc(selectedBacSi.id);
    } catch (err) {
      console.error(err);
      alert("Có lỗi xảy ra khi gán năng lực!");
    } finally {
      setLoading(false);
    }
  };

  const handleDelete = async (id) => {
    if (!window.confirm("Xóa vai trò này?")) return;
    try {
      setLoading(true);
      await nangLucService.remove(id);
      await loadNangLuc(selectedBacSi.id);
    } catch (err) { console.error(err); }
    finally { setLoading(false); }
  };

  const handleDeleteGroup = async (group) => {
    if (!window.confirm(`Bạn có chắc chắn muốn xóa tất cả năng lực thuộc dịch vụ "${group.tenDvkt}"?`)) return;
    try {
      setLoading(true);
      if (typeof nangLucService.removeByDvkt === "function") {
        await nangLucService.removeByDvkt(selectedBacSi.id, group.dvktId);
      } else {
        const deleteRequests = group.roles.map((r) => nangLucService.remove(r.id));
        await Promise.all(deleteRequests);
      }
      await loadNangLuc(selectedBacSi.id);
    } catch (err) {
      console.error(err);
      alert("Có lỗi xảy ra khi xóa dịch vụ!");
    } finally {
      setLoading(false);
    }
  };

  const handleDeleteAllByBacSi = async () => {
    if (!selectedBacSi) return;
    if (!window.confirm(`⚠️ CẢNH BÁO: Bạn có chắc chắn muốn xóa TẤT CẢ năng lực chuyên môn của bác sĩ ${selectedBacSi.hoTen}?`)) return;

    try {
      setLoading(true);
      if (typeof nangLucService.removeAllByBacSi === "function") {
        await nangLucService.removeAllByBacSi(selectedBacSi.id);
      } else {
        const deleteRequests = nangLucList.map((nl) => nangLucService.remove(nl.id));
        await Promise.all(deleteRequests);
      }
      await loadNangLuc(selectedBacSi.id);
    } catch (err) {
      console.error(err);
      alert("Có lỗi xảy ra khi xóa danh sách năng lực của bác sĩ!");
    } finally {
      setLoading(false);
    }
  };

  const handleToggleRole = async (item) => {
    const newRole = item.vaiTro === "DOC_KQ" ? "THUC_HIEN" : "DOC_KQ";
    const isConflict = nangLucList.some(n => n.dvktId === item.dvktId && n.vaiTro === newRole);
    if (isConflict) return alert("Dịch vụ này đã được gán vai trò tương ứng!");
    try {
      setLoading(true);
      await nangLucService.update(item.id, { ...item, vaiTro: newRole });
      await loadNangLuc(selectedBacSi.id);
    } catch (err) { console.error(err); }
    finally { setLoading(false); }
  };

  return (
    <div style={{ padding: '24px', maxWidth: '1280px', margin: '0 auto', fontFamily: "'Inter', system-ui, sans-serif", color: '#2c3e50' }}>
      
      {/* HEADER & NÚT XUẤT EXCEL TOÀN BỘ */}
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '24px' }}>
        <h2 style={{ color: '#1a202c', margin: 0, fontWeight: '800' }}>
          🩺 Quản Lý Năng Lực Chuyên Môn
        </h2>
        
        <button
          onClick={handleExportAll}
          disabled={loading}
          style={{
            background: '#10b981',
            color: '#fff',
            border: 'none',
            padding: '8px 14px',
            borderRadius: '6px',
            fontWeight: '600',
            cursor: loading ? 'not-allowed' : 'pointer',
            fontSize: '13px'
          }}
        >
          📊 Xuất Excel Báo Cáo
        </button>
      </div>

      <div style={{ display: "grid", gridTemplateColumns: "1fr 1.3fr 1.2fr", gap: 20 }}>
        
        {/* CỘT 1: BÁC SĨ */}
        <div style={{ border: '1px solid #e2e8f0', padding: '16px', borderRadius: '10px', background: '#fff', boxShadow: '0 1px 3px rgba(0,0,0,0.05)' }}>
          <h3 style={{ marginTop: 0, fontSize: '16px', fontWeight: '700', color: '#334155' }}>👨‍⚕️ Danh sách Bác sĩ</h3>
          <input
            type="text"
            placeholder="🔍 Tìm tên bác sĩ..."
            value={search}
            onChange={(e) => setSearch(e.target.value)}
            style={{ marginBottom: 12, padding: '8px 12px', width: "100%", boxSizing: 'border-box', borderRadius: '6px', border: '1px solid #cbd5e0', outline: 'none' }}
          />
          
          <div style={{ minHeight: '260px' }}>
            {pagedBacSi.map((bs) => (
              <div
                key={bs.id}
                onClick={() => handleSelectBacSi(bs)}
                style={{
                  padding: '10px 12px',
                  marginBottom: '6px',
                  cursor: "pointer",
                  borderRadius: '6px',
                  background: selectedBacSi?.id === bs.id ? "#eff6ff" : "#f8fafc",
                  color: selectedBacSi?.id === bs.id ? "#2563eb" : "#334155",
                  fontWeight: selectedBacSi?.id === bs.id ? "700" : "500",
                  border: selectedBacSi?.id === bs.id ? "1px solid #93c5fd" : "1px solid transparent",
                  transition: 'all 0.15s ease'
                }}
              >
                {bs.hoTen}
              </div>
            ))}
          </div>

          <div style={{ marginTop: 12, display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
            <button disabled={bsPage === 1} onClick={() => setBsPage(p => p - 1)} style={styles.btnPagination}>⬅</button>
            <span style={{ fontSize: '13px', color: '#64748b' }}>{bsPage} / {totalBsPages}</span>
            <button disabled={bsPage === totalBsPages} onClick={() => setBsPage(p => p + 1)} style={styles.btnPagination}>➡</button>
          </div>
        </div>

        {/* CỘT 2: NĂNG LỰC ĐÃ GÁN */}
        <div style={{ border: '1px solid #e2e8f0', padding: '16px', borderRadius: '10px', background: '#f8fafc', boxShadow: '0 1px 3px rgba(0,0,0,0.05)' }}>
          <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '12px' }}>
            <h3 style={{ margin: 0, fontSize: '16px', fontWeight: '700', color: '#334155' }}>
              {selectedBacSi ? `📋 Năng lực: ${selectedBacSi.hoTen}` : "📋 Danh sách năng lực"}
            </h3>
            
            <div style={{ display: 'flex', gap: '6px' }}>
              {/* NÚT XUẤT EXCEL THEO BÁC SĨ */}
              {selectedBacSi && filteredGroupedNangLuc.length > 0 && (
                <button
                  onClick={handleExportSelectedBacSi}
                  style={{
                    background: '#dcfce7',
                    color: '#15803d',
                    border: '1px solid #86efac',
                    padding: '4px 8px',
                    borderRadius: '6px',
                    fontSize: '11px',
                    fontWeight: '700',
                    cursor: 'pointer'
                  }}
                  title="Xuất Excel riêng cho bác sĩ này"
                >
                  📥 Xuất Excel
                </button>
              )}

              {/* NÚT XÓA TẤT CẢ DỊCH VỤ CỦA BÁC SĨ */}
              {selectedBacSi && nangLucList.length > 0 && (
                <button
                  onClick={handleDeleteAllByBacSi}
                  disabled={loading}
                  style={{
                    background: '#fee2e2',
                    color: '#dc2626',
                    border: '1px solid #fca5a5',
                    padding: '4px 8px',
                    borderRadius: '6px',
                    fontSize: '11px',
                    fontWeight: '700',
                    cursor: 'pointer'
                  }}
                  title="Xóa toàn bộ năng lực của bác sĩ này"
                >
                  🗑️ Xóa tất cả
                </button>
              )}
            </div>
          </div>
          
          <input
            type="text"
            placeholder="🔍 Tìm dịch vụ đã gán..."
            value={nlSearch}
            onChange={(e) => setNlSearch(e.target.value)}
            disabled={!selectedBacSi}
            style={{ 
              marginBottom: 12, padding: '8px 12px', width: "100%", 
              boxSizing: 'border-box', borderRadius: '6px', 
              border: '1px solid #cbd5e0', outline: 'none',
              background: !selectedBacSi ? '#f1f5f9' : '#fff'
            }}
          />
          
          <div style={{ minHeight: '260px' }}>
            {pagedGroupedNl.length === 0 && selectedBacSi && (
              <p style={{ color: '#94a3b8', textAlign: 'center', marginTop: '40px', fontSize: '14px' }}>
                {nlSearch ? "Không tìm thấy dịch vụ phù hợp." : "Chưa được gán năng lực nào."}
              </p>
            )}

            {!selectedBacSi && (
              <p style={{ color: '#94a3b8', textAlign: 'center', marginTop: '40px', fontSize: '14px' }}>
                Vui lòng chọn bác sĩ từ cột bên trái.
              </p>
            )}

            {pagedGroupedNl.map((group) => (
              <div key={group.dvktId} style={{
                padding: '10px 12px',
                marginBottom: '8px',
                background: "#fff",
                border: '1px solid #e2e8f0',
                borderRadius: '6px',
              }}>
                <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '6px' }}>
                  <div style={{ fontWeight: "600", color: '#1e293b', fontSize: '13px' }}>
                    {group.tenDvkt}
                  </div>
                  <button
                    onClick={() => handleDeleteGroup(group)}
                    style={{
                      background: 'transparent',
                      border: 'none',
                      color: '#ef4444',
                      cursor: 'pointer',
                      fontSize: '11px',
                      fontWeight: '600',
                      padding: '2px 4px'
                    }}
                    title="Xóa tất cả vai trò thuộc dịch vụ này"
                  >
                    🗑️ Xóa DVKT
                  </button>
                </div>
                
                <div style={{ display: "flex", gap: '6px', flexWrap: 'wrap' }}>
                  {group.roles.map((r) => (
                    <div key={r.id} style={{
                      display: 'inline-flex',
                      alignItems: 'center',
                      padding: '3px 8px',
                      borderRadius: '12px',
                      fontSize: '11px',
                      fontWeight: '600',
                      background: r.vaiTro === "DOC_KQ" ? "#eff6ff" : "#f5f3ff",
                      color: r.vaiTro === "DOC_KQ" ? "#2563eb" : "#7c3aed",
                      border: `1px solid ${r.vaiTro === "DOC_KQ" ? "#bfdbfe" : "#ddd6fe"}`
                    }}>
                      <span 
                        style={{ cursor: 'pointer', marginRight: '4px' }} 
                        onClick={() => handleToggleRole(r)}
                        title="Click để chuyển đổi vai trò"
                      >
                        {r.vaiTro === "DOC_KQ" ? "📖 Đọc KQ" : "🛠️ Thực hiện"}
                      </span>
                      <b 
                        style={{ cursor: 'pointer', color: '#ef4444', marginLeft: '4px', fontSize: '13px' }} 
                        onClick={() => handleDelete(r.id)}
                        title="Xóa vai trò này"
                      >✕</b>
                    </div>
                  ))}
                </div>
              </div>
            ))}
          </div>

          {totalNlPages > 1 && (
            <div style={{ marginTop: 12, display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
              <button disabled={nlPage === 1} onClick={() => setNlPage(p => p - 1)} style={styles.btnPagination}>⬅</button>
              <span style={{ fontSize: '13px', color: '#64748b' }}>{nlPage} / {totalNlPages}</span>
              <button disabled={nlPage === totalNlPages} onClick={() => setNlPage(p => p + 1)} style={styles.btnPagination}>➡</button>
            </div>
          )}
        </div>

        {/* CỘT 3: GÁN NĂNG LỰC HÀNG LOẠT */}
        <div style={{ border: '1px solid #e2e8f0', padding: '16px', borderRadius: '10px', background: '#fff', boxShadow: '0 1px 3px rgba(0,0,0,0.05)' }}>
          <h3 style={{ marginTop: 0, fontSize: '16px', fontWeight: '700', color: '#334155' }}>➕ Gán năng lực mới</h3>
          
          <select
            value={selectedParentId}
            onChange={(e) => setSelectedParentId(e.target.value)}
            style={{ width: "100%", marginBottom: 8, padding: '8px', borderRadius: '6px', border: '1px solid #cbd5e0', outline: 'none' }}
            disabled={!selectedBacSi}
          >
            <option value="">-- Tất cả nhóm DVKT --</option>
            {dvktTree.map((p) => <option key={p.id} value={p.id}>{p.maDvkt} - {p.tenDvkt}</option>)}
          </select>

          <input
            type="text"
            placeholder="🔍 Tìm nhanh DVKT..."
            value={dvktSearch}
            onChange={(e) => setDvktSearch(e.target.value)}
            style={{ width: "100%", marginBottom: 8, padding: '8px 12px', boxSizing: 'border-box', borderRadius: '6px', border: '1px solid #cbd5e0', outline: 'none' }}
            disabled={!selectedBacSi}
          />

          {/* CHECKBOX CHỌN TẤT CẢ TRANG HIỆN TẠI */}
          {selectedBacSi && pagedDvkt.length > 0 && (
            <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: 6 }}>
              <label style={{ fontSize: '12px', fontWeight: '600', color: '#2563eb', cursor: 'pointer' }}>
                <input
                  type="checkbox"
                  onChange={handleSelectAllCurrentPageDvkt}
                  checked={pagedDvkt.every((c) => selectedDvktIds.includes(c.id))}
                  style={{ marginRight: 5 }}
                />
                Chọn tất cả trang này
              </label>
              <span style={{ fontSize: '11px', color: '#64748b' }}>Đã chọn: {selectedDvktIds.length}</span>
            </div>
          )}

          {/* DANH SÁCH CHECKBOX DVKT */}
          <div style={{ 
            border: '1px solid #e2e8f0', 
            borderRadius: '6px', 
            padding: '6px', 
            minHeight: '180px',
            maxHeight: '210px', 
            overflowY: 'auto',
            background: !selectedBacSi ? '#f1f5f9' : '#fff',
            marginBottom: 8
          }}>
            {pagedDvkt.map((c) => {
              const isChecked = selectedDvktIds.includes(c.id);
              return (
                <label
                  key={c.id}
                  style={{
                    display: 'flex',
                    alignItems: 'center',
                    padding: '6px 8px',
                    borderRadius: '4px',
                    fontSize: '12px',
                    cursor: selectedBacSi ? 'pointer' : 'not-allowed',
                    background: isChecked ? '#eff6ff' : 'transparent',
                    marginBottom: '2px'
                  }}
                >
                  <input
                    type="checkbox"
                    checked={isChecked}
                    onChange={() => handleToggleSelectDvkt(c.id)}
                    disabled={!selectedBacSi}
                    style={{ marginRight: 8 }}
                  />
                  <span>{selectedParentId ? "" : `${c.parentCode} > `}{c.maDvkt} - {c.tenDvkt}</span>
                </label>
              );
            })}
          </div>

          {/* PHÂN TRANG DVKT */}
          <div style={{ marginBottom: 12, display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
            <button disabled={dvktPage === 1} onClick={() => setDvktPage(p => p - 1)} style={styles.btnPagination}>⬅</button>
            <span style={{ fontSize: '12px', color: '#64748b' }}>{dvktPage} / {totalDvktPages}</span>
            <button disabled={dvktPage === totalDvktPages} onClick={() => setDvktPage(p => p + 1)} style={styles.btnPagination}>➡</button>
          </div>

          {/* CHỌN VAI TRÒ */}
          <select
            value={vaiTro}
            onChange={(e) => setVaiTro(e.target.value)}
            style={{ width: "100%", marginBottom: 12, padding: '8px', borderRadius: '6px', border: '1px solid #cbd5e0' }}
            disabled={!selectedBacSi}
          >
            <option value="DOC_KQ">📖 Đọc kết quả</option>
            <option value="THUC_HIEN">🛠️ Thực hiện</option>
          </select>

          {/* BUTTON SUBMIT */}
          <button
            onClick={handleSubmitBulk}
            style={{ 
              width: "100%", padding: '10px', cursor: "pointer", 
              background: loading || !selectedBacSi || selectedDvktIds.length === 0 ? '#94a3b8' : '#2563eb', 
              color: '#fff', border: 'none', borderRadius: '6px', fontWeight: 'bold', fontSize: '13px',
              transition: 'background 0.2s'
            }}
            disabled={!selectedBacSi || loading || selectedDvktIds.length === 0}
          >
            {loading ? "Đang xử lý..." : `XÁC NHẬN GÁN (${selectedDvktIds.length})`}
          </button>
        </div>

      </div>
    </div>
  );
};

const styles = {
  btnPagination: {
    padding: '4px 10px',
    borderRadius: '4px',
    border: '1px solid #cbd5e0',
    background: '#fff',
    cursor: 'pointer',
    fontSize: '12px'
  }
};

export default NangLucPage;