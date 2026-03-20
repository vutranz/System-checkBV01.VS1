import { Outlet, NavLink } from "react-router-dom";

const MainLayout = () => {
  return (
    <div style={styles.container}>
      
      {/* SIDEBAR */}
      <aside style={styles.sidebar}>
        <div style={styles.brand}>
          <div style={styles.brandLogo}>
            <span style={styles.brandIcon}>🏥</span>
          </div>
          <h2 style={styles.brandText}>BV01 <span style={{fontWeight: '400', fontSize: '14px'}}>PRO</span></h2>
        </div>
        
        <nav style={styles.nav}>
          <NavLink to="/" style={styles.link}>Trang chủ</NavLink>
          <NavLink to="/ho-so" style={styles.link}>Thêm File XMl</NavLink>

          {/* NHÓM 1 */}
          <p style={styles.navLabel}>DANH MỤC QUẢN LÝ</p>
          <NavLink to="/bac-si" style={styles.link}>Quản lý Bác sĩ</NavLink>
          <NavLink to="/chuyen-khoa" style={styles.link}>Quản lý Chuyên khoa</NavLink>
          <NavLink to="/bacsi-chuyenkhoa" style={styles.link}>Bác Sĩ - Chuyên Khoa</NavLink>
          
          {/* NHÓM 2 */}
          <p style={styles.navLabel}>DỊCH VỤ & LỊCH TRỰC</p>
          <NavLink to="/nhom-dvkt" style={styles.link}>Nhóm Dịch Vụ Kỹ Thuật</NavLink>
          <NavLink to="/dich-vu-ky-thuat" style={styles.link}>Dịch Vụ Kỹ Thuật</NavLink>
          <NavLink to="/ca-lam-viec" style={styles.link}>Ca Làm Việc</NavLink>
          <NavLink to="/lich-lam-viec-theo-thu" style={styles.link}>Lịch Làm Việc Theo Thứ</NavLink>
          
          {/* NHÓM 3 */}
          <p style={styles.navLabel}>NĂNG LỰC CHUYÊN MÔN</p>
          <NavLink to="/nang-luc-chuyen-mon" style={styles.link}>Năng Lực Chuyên Môn</NavLink>
          <NavLink to="/phan-cong-nang-luc" style={styles.link}>Phân Công Năng Lực</NavLink>
        </nav>
      </aside>

      {/* RIGHT CONTENT AREA */}
      <div style={styles.contentWrapper}>
        <header style={styles.header}>
          <div style={styles.headerTitle}>Hệ Thống Quản Trị Nhân Sự & Dịch Vụ</div>
          <div style={styles.userProfile}>
            <div style={styles.userInfo}>
              <span style={styles.userName}>Admin BV01</span>
              <small style={{color: '#94a3b8', display: 'block', textAlign: 'right', fontSize: '11px'}}>Quản trị viên</small>
            </div>
            <div style={styles.avatar}>A</div>
          </div>
        </header>

        <main style={styles.main}>
          <Outlet />
        </main>

        <footer style={styles.footer}>
          <b>Hệ Thống Kiểm Tra BV01</b> © 2026 - Thiết kế bởi <span style={{color: '#6366f1'}}>VuIT</span>
        </footer>
      </div>
    </div>
  );
};

const styles = {
  container: {
    display: "flex",
    height: "100vh",
    backgroundColor: "#f1f5f9",
    fontFamily: "'Inter', sans-serif",
  },
  sidebar: {
    width: "280px",
    background: "#ffffff",
    display: "flex",
    flexDirection: "column",
    borderRight: "1px solid #e2e8f0",
    zIndex: 10,
  },
  brand: {
    padding: "30px 24px",
    display: "flex",
    alignItems: "center",
    gap: "12px",
  },
  brandLogo: {
    width: "40px",
    height: "40px",
    background: "linear-gradient(135deg, #6366f1 0%, #a855f7 100%)",
    borderRadius: "12px",
    display: "flex",
    alignItems: "center",
    justifyContent: "center",
    boxShadow: "0 4px 12px rgba(99, 102, 241, 0.35)",
  },
  brandIcon: { fontSize: "20px" },
  brandText: { 
    margin: 0, 
    fontSize: "20px", 
    fontWeight: "900", 
    color: "#1e293b",
    letterSpacing: "-0.5px"
  },
  nav: {
    padding: "10px 16px",
    flex: 1,
    overflowY: "auto",
  },
  // --- PHẦN TIÊU ĐỀ MỤC ĐÃ ĐƯỢC LÀM NỔI BẬT ---
  navLabel: {
    fontSize: "12px",
    fontWeight: "800",
    color: "#475569", // Màu đậm hơn một chút (Slate 600)
    padding: "30px 12px 8px 4px",
    textTransform: "uppercase",
    letterSpacing: "1px",
    display: "flex",
    alignItems: "center",
    // Thêm một đường gạch mờ bên cạnh để tạo cảm giác chuyên nghiệp
    borderBottom: "1px solid #f1f5f9",
    marginBottom: "8px",
  },
  // --- PHẦN MỤC CON ---
  link: ({ isActive }) => ({
    display: "block",
    padding: "10px 16px",
    marginBottom: "4px",
    marginLeft: "4px", // Thụt lề nhẹ để phân biệt với tiêu đề
    textDecoration: "none",
    fontSize: "14px",
    fontWeight: isActive ? "600" : "500",
    color: isActive ? "#ffffff" : "#64748b",
    background: isActive 
      ? "linear-gradient(135deg, #6366f1 0%, #8b5cf6 100%)"
      : "transparent",
    borderRadius: "10px",
    transition: "all 0.2s ease",
    boxShadow: isActive ? "0 4px 12px rgba(99, 102, 241, 0.2)" : "none",
  }),
  contentWrapper: {
    flex: 1,
    display: "flex",
    flexDirection: "column",
    overflowY: "auto",
  },
  header: {
    background: "rgba(255, 255, 255, 0.8)",
    backdropFilter: "blur(10px)",
    height: "70px",
    padding: "0 40px",
    display: "flex",
    alignItems: "center",
    justifyContent: "space-between",
    borderBottom: "1px solid #e2e8f0",
    position: "sticky",
    top: 0,
    zIndex: 9,
  },
  headerTitle: {
    fontSize: "15px",
    fontWeight: "600",
    color: "#1e293b",
  },
  userProfile: {
    display: "flex",
    alignItems: "center",
    gap: "15px",
  },
  avatar: {
    width: "40px",
    height: "40px",
    background: "#f1f5f9",
    color: "#6366f1",
    borderRadius: "12px",
    display: "flex",
    alignItems: "center",
    justifyContent: "center",
    fontWeight: "800",
    border: "1px solid #e2e8f0",
  },
  userName: {
    fontSize: "14px",
    fontWeight: "700",
    color: "#1e293b",
  },
  main: {
    flex: 1,
    padding: "30px",
  },
  footer: {
    padding: "20px 40px",
    fontSize: "12px",
    color: "#94a3b8",
    textAlign: "center",
    background: "transparent",
  },
};

export default MainLayout;