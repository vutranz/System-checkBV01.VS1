import { useEffect, useState, useMemo, useCallback } from "react";
import * as bacSiService from "../services/bacSiService";
import * as phanCongService from "../services/phanCongService";
import * as dvktService from "../services/dichVuKyThuatService";
import * as caLamViecService from "../services/caLamViecService";
import * as nangLucService from "../services/nangLucService";

const BS_PAGE_SIZE = 8;
const PC_PAGE_SIZE = 3;

const PhanCongPage = () => {
  const [bacSiList, setBacSiList] = useState([]);
  const [selectedBacSi, setSelectedBacSi] = useState(null);
  const [phanCongList, setPhanCongList] = useState([]);
  const [dvktGroups, setDvktGroups] = useState([]);
  const [caLamViecList, setCaLamViecList] = useState([]);
  const [dvktByNangLuc, setDvktByNangLuc] = useState([]);

  const [search, setSearch] = useState("");
  const [pcSearch, setPcSearch] = useState("");
  const [dvktSearch, setDvktSearch] = useState("");
  const [filterDvktChaId, setFilterDvktChaId] = useState("");

  const [bsPage, setBsPage] = useState(1);
  const [pcPage, setPcPage] = useState(1);
  const [loading, setLoading] = useState(false);

  const [form, setForm] = useState({
    selectedDvktIds: [],
    selectedThus: [],
    selectedCaRoles: {},
  });

  // ================= 1. LOAD MASTER DATA =================
  useEffect(() => {
    const loadMaster = async () => {
      try {
        const [bs, allDvkt, ca] = await Promise.all([
          bacSiService.getAll(),
          dvktService.getAll(),
          caLamViecService.getAll(),
        ]);
        const bsData = Array.isArray(bs) ? bs : bs?.data || [];
        setBacSiList(bsData);
        setCaLamViecList(Array.isArray(ca) ? ca : ca?.data || []);

        const dvktData = Array.isArray(allDvkt) ? allDvkt : allDvkt?.data || [];
        setDvktGroups(dvktData.filter((d) => !d.dvktChaId));

        if (bsData.length > 0) setSelectedBacSi(bsData[0]);
      } catch (err) {
        console.error("Lỗi load master data:", err);
      }
    };
    loadMaster();
  }, []);

  // ================= 2. LOAD DATA THEO BÁC SĨ =================
  const loadDoctorData = useCallback(async (bacSiId) => {
    if (!bacSiId) return;
    try {
      setLoading(true);
      const [pc, nl] = await Promise.all([
        phanCongService.getByBacSi(bacSiId),
        nangLucService.getByBacSi(bacSiId),
      ]);
      setPhanCongList(Array.isArray(pc) ? pc : pc?.data || []);
      setDvktByNangLuc(Array.isArray(nl) ? nl : nl?.data || []);
      setPcPage(1);
      setForm({ selectedDvktIds: [], selectedThus: [], selectedCaRoles: {} });
    } catch (err) {
      console.error("Lỗi load doctor data:", err);
      setPhanCongList([]);
      setDvktByNangLuc([]);
    } finally {
      setLoading(false);
    }
  }, []);

  useEffect(() => {
    if (selectedBacSi?.id) {
      loadDoctorData(selectedBacSi.id);
    }
  }, [selectedBacSi, loadDoctorData]);

  // ================= 3. LOGIC PHÂN TRANG & LỌC =================
  const filteredBacSi = useMemo(
    () => bacSiList.filter((b) => b.hoTen?.toLowerCase().includes(search.toLowerCase())),
    [bacSiList, search]
  );

  const pagedBacSi = useMemo(
    () => filteredBacSi.slice((bsPage - 1) * BS_PAGE_SIZE, bsPage * BS_PAGE_SIZE),
    [filteredBacSi, bsPage]
  );

  const totalBsPages = Math.ceil(filteredBacSi.length / BS_PAGE_SIZE) || 1;

  const uniqueNangLucOptions = useMemo(() => {
    const uniqueMap = new Map();
    dvktByNangLuc.forEach((item) => {
      const id = item.dvktId || item.id || item.dichVuKyThuat?.id;
      const ten = item.tenDvkt || item.dichVuKyThuat?.tenDvkt;
      const ma = item.maDvkt || item.dichVuKyThuat?.maDvkt;
      const chaId = item.dvktChaId || item.dichVuKyThuat?.dvktChaId;
      if (id && !uniqueMap.has(id)) uniqueMap.set(id, { id, ten, ma, chaId });
    });

    return Array.from(uniqueMap.values()).filter((d) => {
      const matchSearch =
        d.ten?.toLowerCase().includes(dvktSearch.toLowerCase()) ||
        d.ma?.toLowerCase().includes(dvktSearch.toLowerCase());
      const matchCha = filterDvktChaId === "" ? true : Number(d.chaId) === Number(filterDvktChaId);
      return matchSearch && matchCha;
    });
  }, [dvktByNangLuc, dvktSearch, filterDvktChaId]);

  const { pagedGroups, totalPcPages } = useMemo(() => {
    const filtered = phanCongList.filter(
      (pc) =>
        pc.tenDvkt?.toLowerCase().includes(pcSearch.toLowerCase()) ||
        pc.thu?.toLowerCase().includes(pcSearch.toLowerCase())
    );

    const grouped = filtered.reduce((acc, pc) => {
      if (!acc[pc.tenDvkt]) acc[pc.tenDvkt] = {};
      if (!acc[pc.tenDvkt][pc.thu]) acc[pc.tenDvkt][pc.thu] = [];
      acc[pc.tenDvkt][pc.thu].push(pc);
      return acc;
    }, {});

    const keys = Object.keys(grouped);
    const pagedKeys = keys.slice((pcPage - 1) * PC_PAGE_SIZE, pcPage * PC_PAGE_SIZE);
    const pagedData = {};
    pagedKeys.forEach((k) => (pagedData[k] = grouped[k]));

    return { pagedGroups: pagedData, totalPcPages: Math.ceil(keys.length / PC_PAGE_SIZE) || 1 };
  }, [phanCongList, pcSearch, pcPage]);

  // ================= 4. HANDLERS =================
  const toggleSelection = (field, value) => {
    setForm((prev) => ({
      ...prev,
      [field]: prev[field].includes(value)
        ? prev[field].filter((x) => x !== value)
        : [...prev[field], value],
    }));
  };

  const handleSelectAllDvkt = (e) => {
    if (e.target.checked) {
      setForm((prev) => ({
        ...prev,
        selectedDvktIds: uniqueNangLucOptions.map((d) => d.id),
      }));
    } else {
      setForm((prev) => ({ ...prev, selectedDvktIds: [] }));
    }
  };

  const toggleCaRole = (caId) => {
    setForm((prev) => {
      const currentRoles = { ...prev.selectedCaRoles };
      if (!currentRoles[caId]) currentRoles[caId] = "DOC_KQ";
      else if (currentRoles[caId] === "DOC_KQ") currentRoles[caId] = "THUC_HIEN";
      else delete currentRoles[caId];
      return { ...prev, selectedCaRoles: currentRoles };
    });
  };

  const handleBulkSubmit = async () => {
    const { selectedDvktIds, selectedThus, selectedCaRoles } = form;
    const caIds = Object.keys(selectedCaRoles);

    if (!selectedBacSi || !selectedDvktIds.length || !selectedThus.length || !caIds.length) {
      return alert("⚠️ Vui lòng chọn đầy đủ: Dịch vụ, Thứ trong tuần và Ca làm việc!");
    }

    try {
      setLoading(true);
      const reqs = selectedDvktIds.flatMap((dvId) =>
        selectedThus.flatMap((thu) =>
          caIds.map((caId) =>
            phanCongService.create({
              bacSiId: selectedBacSi.id,
              dvktId: Number(dvId),
              thu,
              caLamViecId: Number(caId),
              vaiTro: selectedCaRoles[caId],
            })
          )
        )
      );

      await Promise.all(reqs);
      alert("🚀 Gán phân công thành công!");
      await loadDoctorData(selectedBacSi.id);
    } catch (err) {
      console.error(err);
      alert("Lỗi khi lưu dữ liệu phân công!");
    } finally {
      setLoading(false);
    }
  };

  const handleDelete = async (id) => {
    if (window.confirm("Xóa phân công này?")) {
      try {
        setLoading(true);
        await phanCongService.remove(id);
        await loadDoctorData(selectedBacSi.id);
      } catch (err) {
        alert("Lỗi khi xóa!");
      } finally {
        setLoading(false);
      }
    }
  };

  const handleDeleteDvktGroup = async (tenDvkt, byThuMap) => {
    if (window.confirm(`⚠️ Xóa toàn bộ phân công dịch vụ: "${tenDvkt}"?`)) {
      try {
        setLoading(true);
        const idsToDelete = Object.values(byThuMap).flatMap((items) => items.map((pc) => pc.id));
        await Promise.all(idsToDelete.map((id) => phanCongService.remove(id)));
        await loadDoctorData(selectedBacSi.id);
      } catch (err) {
        alert("Lỗi khi xóa!");
      } finally {
        setLoading(false);
      }
    }
  };

  const handleDeleteAllPhanCong = async () => {
    if (!phanCongList.length) return;
    if (window.confirm(`🚨 XÓA TOÀN BỘ ${phanCongList.length} lịch trực của bác sĩ?`)) {
      try {
        setLoading(true);
        await Promise.all(phanCongList.map((pc) => phanCongService.remove(pc.id)));
        await loadDoctorData(selectedBacSi.id);
      } catch (err) {
        alert("Lỗi khi xóa!");
      } finally {
        setLoading(false);
      }
    }
  };

  return (
    <div style={styles.pageBackground}>
      {/* HEADER SECTION */}
      <div style={styles.topHeader}>
        <h2 style={styles.mainTitle}>🗓️ Quản Lý Phân Công Lịch Trực Bác Sĩ</h2>
        <p style={styles.subTitle}>
          Điều phối lịch làm việc & phân công nhiệm vụ theo năng lực chuyên môn
        </p>
      </div>

      {/* 3 CỘT KHUNG CHUẨN */}
      <div style={styles.gridContainer}>
        {/* CỘT 1: BÁC SĨ */}
        <div style={styles.colCard}>
          <div style={styles.cardHeader}>
            <h4 style={styles.cardTitle}>👨‍⚕️ Danh Sách Bác Sĩ</h4>
            <span style={styles.badgeCount}>{filteredBacSi.length}</span>
          </div>

          <div style={styles.searchBox}>
            <input
              type="text"
              placeholder="🔍 Tìm tên bác sĩ..."
              value={search}
              style={styles.searchInput}
              onChange={(e) => {
                setSearch(e.target.value);
                setBsPage(1);
              }}
            />
          </div>

          <div style={styles.listContainer}>
            {pagedBacSi.length > 0 ? (
              pagedBacSi.map((bs) => {
                const isSelected = selectedBacSi?.id === bs.id;
                return (
                  <div
                    key={bs.id}
                    onClick={() => setSelectedBacSi(bs)}
                    style={{
                      ...styles.bsItemCard,
                      backgroundColor: isSelected ? "#eff6ff" : "#ffffff",
                      borderColor: isSelected ? "#2563eb" : "#e2e8f0",
                    }}
                  >
                    <div style={styles.avatarCircle}>
                      {bs.hoTen ? bs.hoTen.charAt(0).toUpperCase() : "B"}
                    </div>
                    <div style={{ minWidth: 0 }}>
                      <div style={{ ...styles.bsName, color: isSelected ? "#1d4ed8" : "#1e293b" }}>
                        {bs.hoTen}
                      </div>
                      <div style={styles.bsCode}>{bs.maBacSi || `BS-${bs.id}`}</div>
                    </div>
                  </div>
                );
              })
            ) : (
              <div style={styles.emptyText}>Không tìm thấy</div>
            )}
          </div>

          <div style={styles.paginationArea}>
            <button style={styles.btnNav} disabled={bsPage === 1} onClick={() => setBsPage((p) => p - 1)}>
              ◄
            </button>
            <span style={styles.pageInfo}>
              {bsPage} / {totalBsPages}
            </span>
            <button style={styles.btnNav} disabled={bsPage === totalBsPages} onClick={() => setBsPage((p) => p + 1)}>
              ►
            </button>
          </div>
        </div>

        {/* CỘT 2: LỊCH TRỰC (CỠ CHỮ COMPACT - HIỂN THỊ TRỌN VẸN 7 NGÀY) */}
        <div style={styles.colCard}>
          <div style={styles.cardHeader}>
            <h4 style={styles.cardTitle}>
              📋 Lịch Trực: <span style={{ color: "#2563eb" }}>{selectedBacSi?.hoTen || "Chưa chọn"}</span>
            </h4>
            {phanCongList.length > 0 && (
              <button onClick={handleDeleteAllPhanCong} disabled={loading} style={styles.btnDeleteAll}>
                🗑️ Xóa toàn bộ
              </button>
            )}
          </div>

          <div style={styles.searchBox}>
            <input
              type="text"
              placeholder="🔍 Lọc theo tên dịch vụ hoặc thứ..."
              value={pcSearch}
              style={styles.searchInput}
              onChange={(e) => {
                setPcSearch(e.target.value);
                setPcPage(1);
              }}
            />
          </div>

          <div style={styles.listContainer}>
            {Object.keys(pagedGroups).length > 0 ? (
              Object.entries(pagedGroups).map(([tenDvkt, byThu]) => (
                <div key={tenDvkt} style={styles.scheduleGroupCard}>
                  <div style={styles.dvktGroupHeader}>
                    <span style={styles.dvktTitleText}>💉 {tenDvkt}</span>
                    <button onClick={() => handleDeleteDvktGroup(tenDvkt, byThu)} disabled={loading} style={styles.btnDeleteGroup}>
                      🗑️ Xóa DVKT này
                    </button>
                  </div>
                  <div style={{ padding: "6px 8px" }}>
                    {Object.entries(byThu).map(([thu, items]) => (
                      <div key={thu} style={styles.thuRow}>
                        <div style={styles.thuTag}>{thu}</div>
                        <div style={{ display: "flex", gap: "4px", flexWrap: "wrap", flex: 1 }}>
                          {items.map((pc) => {
                            const isDoc = pc.vaiTro === "DOC_KQ";
                            return (
                              <div
                                key={pc.id}
                                style={{
                                  ...styles.roleTag,
                                  backgroundColor: isDoc ? "#eff6ff" : "#f0fdf4",
                                  color: isDoc ? "#1d4ed8" : "#15803d",
                                  borderColor: isDoc ? "#bfdbfe" : "#bbf7d0",
                                }}
                              >
                                <span>{pc.tenCa}: {isDoc ? "📖 Đọc" : "🛠️ Làm"}</span>
                                <span onClick={() => handleDelete(pc.id)} style={styles.btnDeleteTag}>✕</span>
                              </div>
                            );
                          })}
                        </div>
                      </div>
                    ))}
                  </div>
                </div>
              ))
            ) : (
              <div style={styles.emptyText}>{loading ? "⌛ Đang tải..." : "Chưa có phân công trực"}</div>
            )}
          </div>

          <div style={styles.paginationArea}>
            <button style={styles.btnNavText} disabled={pcPage === 1} onClick={() => setPcPage((p) => p - 1)}>
              Trang trước
            </button>
            <span style={styles.pageInfo}>
              {pcPage} / {totalPcPages}
            </span>
            <button style={styles.btnNavText} disabled={pcPage === totalPcPages} onClick={() => setPcPage((p) => p + 1)}>
              Trang sau
            </button>
          </div>
        </div>

        {/* CỘT 3: FORM GÁN NHANH */}
        <div style={styles.colCard}>
          <div style={styles.cardHeader}>
            <h4 style={{ ...styles.cardTitle, color: "#d97706" }}>➕ Gán Phân Công Nhanh</h4>
          </div>

          <div style={styles.filterSection}>
            <select
              style={styles.selectInput}
              value={filterDvktChaId}
              onChange={(e) => setFilterDvktChaId(e.target.value)}
            >
              <option value="">-- Tất cả nhóm dịch vụ --</option>
              {dvktGroups.map((g) => (
                <option key={g.id} value={g.id}>
                  {g.tenDvkt}
                </option>
              ))}
            </select>
            <input
              type="text"
              placeholder="🔍 Tìm dịch vụ kỹ thuật..."
              value={dvktSearch}
              style={styles.searchInput}
              onChange={(e) => setDvktSearch(e.target.value)}
            />
          </div>

          <div style={styles.checkboxListArea}>
            <div style={styles.selectAllHeader}>
              <label style={styles.checkboxLabel}>
                <input
                  type="checkbox"
                  onChange={handleSelectAllDvkt}
                  checked={
                    uniqueNangLucOptions.length > 0 &&
                    form.selectedDvktIds.length === uniqueNangLucOptions.length
                  }
                />
                <span style={{ fontWeight: "700", color: "#334155" }}>
                  Chọn tất cả ({uniqueNangLucOptions.length})
                </span>
              </label>
            </div>
            <div style={{ flex: 1, overflowY: "auto" }}>
              {uniqueNangLucOptions.length > 0 ? (
                uniqueNangLucOptions.map((d) => (
                  <label key={d.id} style={styles.checkboxRow}>
                    <input
                      type="checkbox"
                      checked={form.selectedDvktIds.includes(d.id)}
                      onChange={() => toggleSelection("selectedDvktIds", d.id)}
                    />
                    <span style={styles.checkboxText}>{d.ten}</span>
                  </label>
                ))
              ) : (
                <div style={styles.emptyText}>Không có DVKT trong năng lực</div>
              )}
            </div>
          </div>

          <div style={{ margin: "8px 0 6px 0" }}>
            <div style={styles.sectionLabel}>📅 Chọn thứ trong tuần:</div>
            <div style={styles.dayGrid}>
              {["T2", "T3", "T4", "T5", "T6", "T7", "CN"].map((t) => {
                const active = form.selectedThus.includes(t);
                return (
                  <button
                    key={t}
                    type="button"
                    onClick={() => toggleSelection("selectedThus", t)}
                    style={{
                      ...styles.dayBtn,
                      backgroundColor: active ? "#2563eb" : "#f1f5f9",
                      color: active ? "#ffffff" : "#475569",
                      borderColor: active ? "#2563eb" : "#cbd5e0",
                    }}
                  >
                    {t}
                  </button>
                );
              })}
            </div>
          </div>

          <div style={{ marginBottom: "10px" }}>
            <div style={styles.sectionLabel}>⏰ Ca làm việc & Vai trò (Bấm để đổi):</div>
            <div style={{ display: "flex", flexDirection: "column", gap: "4px" }}>
              {caLamViecList.map((c) => {
                const role = form.selectedCaRoles[c.id];
                const isDoc = role === "DOC_KQ";
                const isThucHien = role === "THUC_HIEN";

                return (
                  <div
                    key={c.id}
                    onClick={() => toggleCaRole(c.id)}
                    style={{
                      ...styles.caRoleBox,
                      backgroundColor: isDoc ? "#eff6ff" : isThucHien ? "#f0fdf4" : "#f8fafc",
                      borderColor: isDoc ? "#93c5fd" : isThucHien ? "#86efac" : "#e2e8f0",
                    }}
                  >
                    <span style={{ fontWeight: "600", color: "#1e293b" }}>{c.tenCa}</span>
                    <span
                      style={{
                        ...styles.roleBadge,
                        backgroundColor: isDoc ? "#dbeafe" : isThucHien ? "#dcfce7" : "#e2e8f0",
                        color: isDoc ? "#1e40af" : isThucHien ? "#166534" : "#64748b",
                      }}
                    >
                      {isDoc ? "📖 Đọc KQ" : isThucHien ? "🛠️ Thực Hiện" : "Chưa chọn"}
                    </span>
                  </div>
                );
              })}
            </div>
          </div>

          <button
            onClick={handleBulkSubmit}
            disabled={loading}
            style={{
              ...styles.btnSubmit,
              backgroundColor: loading ? "#94a3b8" : "#16a34a",
            }}
          >
            {loading ? "⏳ ĐANG LƯU DỮ LIỆU..." : "🚀 XÁC NHẬN GÁN LỊCH TRỰC"}
          </button>
        </div>
      </div>
    </div>
  );
};

// ================= STYLES TỐI ƯU CỠ CHỮ & SPACING =================
const styles = {
  pageBackground: {
    padding: "16px 20px",
    backgroundColor: "#f8fafc",
    minHeight: "100vh",
    fontFamily: "'Inter', -apple-system, sans-serif",
    color: "#0f172a",
  },
  topHeader: {
    marginBottom: "12px",
    borderBottom: "1px solid #e2e8f0",
    paddingBottom: "8px",
  },
  mainTitle: {
    margin: 0,
    fontSize: "18px",
    fontWeight: "800",
  },
  subTitle: {
    margin: "2px 0 0 0",
    fontSize: "12px",
    color: "#64748b",
  },
  gridContainer: {
    display: "grid",
    gridTemplateColumns: "270px 1fr 310px", // Giữ nguyên 3 cột tỉ lệ hài hòa
    gap: "12px",
    alignItems: "start",
  },
  colCard: {
    background: "#ffffff",
    borderRadius: "10px",
    padding: "12px",
    border: "1px solid #e2e8f0",
    boxShadow: "0 1px 3px rgba(0,0,0,0.03)",
    display: "flex",
    flexDirection: "column",
    height: "680px",
  },
  cardHeader: {
    display: "flex",
    justifyContent: "space-between",
    alignItems: "center",
    marginBottom: "8px",
  },
  cardTitle: {
    margin: 0,
    fontSize: "13px",
    fontWeight: "700",
    color: "#1e293b",
  },
  badgeCount: {
    backgroundColor: "#f1f5f9",
    color: "#475569",
    padding: "1px 6px",
    borderRadius: "999px",
    fontSize: "10px",
    fontWeight: "700",
  },
  searchBox: {
    marginBottom: "8px",
  },
  searchInput: {
    width: "100%",
    padding: "5px 8px",
    borderRadius: "6px",
    border: "1px solid #cbd5e0",
    fontSize: "11px",
    outline: "none",
    boxSizing: "border-box",
  },
  listContainer: {
    flex: 1,
    overflowY: "auto",
    display: "flex",
    flexDirection: "column",
    gap: "6px",
  },
  bsItemCard: {
    padding: "6px 8px",
    borderRadius: "6px",
    border: "1px solid",
    cursor: "pointer",
    display: "flex",
    alignItems: "center",
    gap: "8px",
  },
  avatarCircle: {
    width: "26px",
    height: "26px",
    borderRadius: "50%",
    backgroundColor: "#2563eb",
    color: "#ffffff",
    display: "flex",
    alignItems: "center",
    justifyContent: "center",
    fontWeight: "700",
    fontSize: "11px",
    flexShrink: 0,
  },
  bsName: {
    fontSize: "11px",
    fontWeight: "600",
    whiteSpace: "nowrap",
    overflow: "hidden",
    textOverflow: "ellipsis",
  },
  bsCode: {
    fontSize: "10px",
    color: "#64748b",
  },
  paginationArea: {
    display: "flex",
    justifyContent: "center",
    alignItems: "center",
    gap: "8px",
    paddingTop: "8px",
    marginTop: "4px",
    borderTop: "1px solid #f1f5f9",
  },
  btnNav: {
    padding: "2px 8px",
    border: "1px solid #cbd5e0",
    background: "#ffffff",
    borderRadius: "4px",
    cursor: "pointer",
    fontSize: "10px",
  },
  btnNavText: {
    padding: "2px 8px",
    border: "1px solid #cbd5e0",
    background: "#ffffff",
    borderRadius: "4px",
    cursor: "pointer",
    fontSize: "10px",
    fontWeight: "500",
  },
  pageInfo: {
    fontSize: "10px",
    fontWeight: "600",
    color: "#475569",
  },
  scheduleGroupCard: {
    border: "1px solid #e2e8f0",
    borderRadius: "6px",
    overflow: "hidden",
    backgroundColor: "#ffffff",
  },
  dvktGroupHeader: {
    backgroundColor: "#f8fafc",
    padding: "4px 8px",
    fontWeight: "700",
    fontSize: "11px",
    color: "#334155",
    borderBottom: "1px solid #e2e8f0",
    display: "flex",
    justifyContent: "space-between",
    alignItems: "center",
  },
  dvktTitleText: {
    whiteSpace: "nowrap",
    overflow: "hidden",
    textOverflow: "ellipsis",
    maxWidth: "75%",
  },
  thuRow: {
    display: "flex",
    alignItems: "center",
    gap: "6px",
    marginBottom: "4px",
  },
  thuTag: {
    width: "20px",
    fontWeight: "700",
    color: "#2563eb",
    fontSize: "10px",
    textAlign: "center",
  },
  roleTag: {
    display: "inline-flex",
    alignItems: "center",
    gap: "3px",
    padding: "1px 5px",
    borderRadius: "4px",
    border: "1px solid",
    fontSize: "10px",
    fontWeight: "500",
  },
  btnDeleteTag: {
    cursor: "pointer",
    color: "#ef4444",
    fontWeight: "bold",
    marginLeft: "2px",
    fontSize: "10px",
  },
  btnDeleteGroup: {
    border: "none",
    background: "transparent",
    color: "#ef4444",
    fontSize: "10px",
    fontWeight: "600",
    cursor: "pointer",
  },
  btnDeleteAll: {
    backgroundColor: "#fef2f2",
    color: "#dc2626",
    border: "1px solid #fecaca",
    padding: "2px 6px",
    borderRadius: "4px",
    fontSize: "10px",
    fontWeight: "600",
    cursor: "pointer",
  },
  emptyText: {
    textAlign: "center",
    color: "#94a3b8",
    fontSize: "11px",
    padding: "16px 0",
  },
  filterSection: {
    display: "flex",
    flexDirection: "column",
    gap: "4px",
    marginBottom: "6px",
    backgroundColor: "#fffbeb",
    padding: "6px",
    borderRadius: "6px",
    border: "1px solid #fde68a",
  },
  selectInput: {
    width: "100%",
    padding: "4px 6px",
    borderRadius: "4px",
    border: "1px solid #cbd5e0",
    fontSize: "11px",
  },
  checkboxListArea: {
    flex: 1,
    border: "1px solid #e2e8f0",
    borderRadius: "6px",
    padding: "6px",
    display: "flex",
    flexDirection: "column",
    maxHeight: "150px",
  },
  selectAllHeader: {
    borderBottom: "1px solid #f1f5f9",
    paddingBottom: "4px",
    marginBottom: "4px",
  },
  checkboxRow: {
    display: "flex",
    alignItems: "center",
    gap: "6px",
    padding: "2px 0",
    fontSize: "10px",
    cursor: "pointer",
  },
  checkboxLabel: {
    display: "flex",
    alignItems: "center",
    gap: "6px",
    fontSize: "10px",
    cursor: "pointer",
  },
  checkboxText: {
    color: "#334155",
    whiteSpace: "nowrap",
    overflow: "hidden",
    textOverflow: "ellipsis",
  },
  sectionLabel: {
    fontSize: "10px",
    fontWeight: "700",
    color: "#64748b",
    marginBottom: "4px",
  },
  dayGrid: {
    display: "grid",
    gridTemplateColumns: "repeat(7, 1fr)",
    gap: "3px",
  },
  dayBtn: {
    padding: "4px 0",
    borderRadius: "4px",
    border: "1px solid",
    fontSize: "10px",
    fontWeight: "700",
    cursor: "pointer",
    textAlign: "center",
  },
  caRoleBox: {
    padding: "5px 8px",
    borderRadius: "5px",
    border: "1px solid",
    cursor: "pointer",
    display: "flex",
    justifyContent: "space-between",
    alignItems: "center",
    fontSize: "10px",
  },
  roleBadge: {
    padding: "1px 5px",
    borderRadius: "3px",
    fontSize: "9px",
    fontWeight: "700",
  },
  btnSubmit: {
    width: "100%",
    padding: "9px",
    color: "#ffffff",
    border: "none",
    borderRadius: "6px",
    fontWeight: "700",
    fontSize: "11px",
    cursor: "pointer",
  },
};

export default PhanCongPage;