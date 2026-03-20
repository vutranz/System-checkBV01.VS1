import { Routes, Route } from "react-router-dom";
import MainLayout from "./layouts/MainLayout";
import HomePage from "./pages/HomePage";
import BacSiPage from "./pages/BacSiPage";
import ChuyenKhoaPage from "./pages/ChuyenKhoaPage";
import BacSiChuyenKhoaPage from "./pages/BacSiChuyenKhoaPage";
import NhomDVKTPage from "./pages/NhomDVKTPage";
import CaLamViecPage from "./pages/CaLamViecPage";
import DichVuKyThuatPage from "./pages/DichVuKyThuatPage";
import LichLamViecTheoThuPage from "./pages/LichLamViecTheoThuPage";
import NangLucPage from "./pages/NangLucPage";
import PhanCongNangLucPage from "./pages/PhanCongNangLucPage";
import ImportHoSoPage from "./pages/ImportHoSoPage";

function App() {
  return (
    <Routes>
      <Route path="/" element={<MainLayout />}>
        
        {/* Trang chủ */}
        <Route index element={<HomePage />} />

        {/* Quản lý bác sĩ */}
        <Route path="bac-si" element={<BacSiPage />} />

        <Route path="chuyen-khoa" element={<ChuyenKhoaPage />} />
        
        <Route path="bacsi-chuyenkhoa" element={<BacSiChuyenKhoaPage />} />
        <Route path="nhom-dvkt" element={<NhomDVKTPage />} />
        <Route path="ca-lam-viec" element={<CaLamViecPage />} />
        <Route path="dich-vu-ky-thuat" element={<DichVuKyThuatPage />} />
        <Route path="lich-lam-viec-theo-thu" element={< LichLamViecTheoThuPage/>} />
        <Route path="nang-luc-chuyen-mon" element={< NangLucPage/>} />
        <Route path="phan-cong-nang-luc" element={< PhanCongNangLucPage/>} />
         <Route path="ho-so" element={< ImportHoSoPage/>} />
      </Route>
    </Routes>
  );
}

export default App;