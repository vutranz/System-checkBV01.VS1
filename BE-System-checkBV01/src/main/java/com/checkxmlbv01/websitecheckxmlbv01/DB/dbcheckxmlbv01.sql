-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Máy chủ: 127.0.0.1
-- Thời gian đã tạo: Th2 28, 2026 lúc 04:31 AM
-- Phiên bản máy phục vụ: 10.4.32-MariaDB
-- Phiên bản PHP: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Cơ sở dữ liệu: `dbcheckxmlbv01`
--

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `bacsi`
--

CREATE TABLE `bacsi` (
  `id` bigint(20) NOT NULL,
  `cchn` varchar(100) DEFAULT NULL,
  `ho_ten` varchar(255) NOT NULL,
  `hoat_dong` bit(1) NOT NULL,
  `ma_bac_si` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Đang đổ dữ liệu cho bảng `bacsi`
--

INSERT INTO `bacsi` (`id`, `cchn`, `ho_ten`, `hoat_dong`, `ma_bac_si`) VALUES
(1, '000003/LA-GPHN', 'BS. Phạm Văn Thành', b'1', 'BS001'),
(2, '000139/BTH-CCHN', 'BS. Lưu Ngọc Lễ', b'1', 'BS002'),
(3, '009359/QNA-CCHN', 'BS. Dương Duy Đạt', b'1', 'BS003'),
(4, '002220/BTH-CCHN', 'BS. Nguyễn Thị Giang Hạnh', b'1', 'BS004'),
(5, '767/NT-CCHN', 'BS. Thập Văn Đắc', b'1', 'BS005'),
(6, '4172/BTH-CCHN', 'BS. Trần Ngọc Minh Phượng', b'1', 'BS006'),
(7, '000036/CM-GPHN', 'KTV. Ngô Vũ Thái', b'1', 'BS007'),
(8, '006619/KH-CCHN', 'KTV. Trần Đình Lâm', b'1', 'BS008'),
(9, '6804/BTH-CCHN', 'KTV. Phạm Hoàng Hiếu', b'1', 'BS009'),
(10, '000010/BTH-GPHN', 'KTV. Phạm Minh Thành', b'1', 'BS010'),
(11, '4555/BTH-CCHN', 'KTV. Thông Đinh Hương', b'1', 'BS011'),
(12, '000162/AG-GPHN', 'KTV. Trần Thụy Diễm Trang', b'1', 'BS012'),
(13, '6457/BTH-CCHN', 'KTV. Nguyễn Tiến Vững', b'1', 'BS013'),
(14, '001070/TB-CCHN', 'KTV. Nguyễn Ngọc Hoài', b'1', 'BS014'),
(15, '016852/TH-CCHN', 'KTV. Lương Quốc Anh', b'1', 'BS015'),
(16, '4029/BTH-CCHN', 'KTV. Nguyễn Thị Ngọc Thảo', b'1', 'BS016');

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `bacsi_chuyenkhoa`
--

CREATE TABLE `bacsi_chuyenkhoa` (
  `id` bigint(20) NOT NULL,
  `gan_luc` datetime(6) NOT NULL,
  `la_chuyen_khoa_chinh` bit(1) NOT NULL,
  `bac_si_id` bigint(20) NOT NULL,
  `chuyen_khoa_id` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Đang đổ dữ liệu cho bảng `bacsi_chuyenkhoa`
--

INSERT INTO `bacsi_chuyenkhoa` (`id`, `gan_luc`, `la_chuyen_khoa_chinh`, `bac_si_id`, `chuyen_khoa_id`) VALUES
(1, '2026-02-28 08:43:18.000000', b'1', 1, 7),
(2, '2026-02-28 08:46:25.000000', b'1', 2, 6);

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `calamviec`
--

CREATE TABLE `calamviec` (
  `id` bigint(20) NOT NULL,
  `gio_bat_dau` time NOT NULL,
  `gio_ket_thuc` time NOT NULL,
  `hoat_dong` bit(1) NOT NULL,
  `ten_ca` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Đang đổ dữ liệu cho bảng `calamviec`
--

INSERT INTO `calamviec` (`id`, `gio_bat_dau`, `gio_ket_thuc`, `hoat_dong`, `ten_ca`) VALUES
(1, '07:00:00', '11:30:00', b'1', 'Ca Sáng'),
(2, '13:30:00', '17:00:00', b'1', 'Ca Chiều'),
(3, '11:40:00', '13:20:00', b'1', 'Ca 1');

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `chuyenkhoa`
--

CREATE TABLE `chuyenkhoa` (
  `id` bigint(20) NOT NULL,
  `hoat_dong` bit(1) NOT NULL,
  `ma_chuyen_khoa` varchar(50) NOT NULL,
  `ten_chuyen_khoa` varchar(255) NOT NULL,
  `chuyen_khoa_cha_id` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Đang đổ dữ liệu cho bảng `chuyenkhoa`
--

INSERT INTO `chuyenkhoa` (`id`, `hoat_dong`, `ma_chuyen_khoa`, `ten_chuyen_khoa`, `chuyen_khoa_cha_id`) VALUES
(1, b'1', 'K01', 'Khoa Khám Bệnh', NULL),
(2, b'1', 'K16', 'Y học cổ truyền;Phục hồi chức năng', NULL),
(3, b'1', 'K47', 'Khoa xét nghiệm', NULL),
(4, b'1', '10.19', 'Ngoại tổng hợp', NULL),
(5, b'1', 'K39', 'Chẩn đoán hình ảnh', NULL),
(6, b'1', '03.18', 'Nhi', NULL),
(7, b'1', '02.03', 'Nội tổng hợp', NULL);

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `dichvukythuat`
--

CREATE TABLE `dichvukythuat` (
  `id` bigint(20) NOT NULL,
  `hoat_dong` bit(1) NOT NULL,
  `ma_dvkt` varchar(50) NOT NULL,
  `ten_dvkt` varchar(255) NOT NULL,
  `thoi_gian_max` int(11) DEFAULT NULL,
  `thoi_gian_min` int(11) DEFAULT NULL,
  `chuyen_khoa_id` bigint(20) NOT NULL,
  `dvkt_cha_id` bigint(20) DEFAULT NULL,
  `nhom_dvkt_id` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Đang đổ dữ liệu cho bảng `dichvukythuat`
--

INSERT INTO `dichvukythuat` (`id`, `hoat_dong`, `ma_dvkt`, `ten_dvkt`, `thoi_gian_max`, `thoi_gian_min`, `chuyen_khoa_id`, `dvkt_cha_id`, `nhom_dvkt_id`) VALUES
(1, b'1', 'SA001', 'Siêu âm', 12, 6, 5, NULL, 1),
(2, b'1', 'XQ001', 'X-Quang', 12, 6, 5, NULL, 1),
(3, b'1', 'XN001', 'Xét Nghiệm', 12, 6, 3, NULL, 2),
(4, b'1', 'TT001', 'Thủ Thuật', 12, 6, 2, NULL, 3),
(5, b'1', '22.0120.1370', 'Tổng phân tích tế bào máu ngoại vi (máy đếm tổng trở)', 15, 10, 3, 3, 2),
(6, b'1', '23.0206.1596', 'Tổng phân tích nước tiểu (máy tự động)', 15, 10, 3, 3, 2),
(7, b'1', '22.0138.1362', 'Tìm ký sinh trùng sốt rét trong máu (thủ công)', 30, 23, 3, 3, 2),
(8, b'1', '23.0003.1494', 'Định lượng Acid Uric [Máu]', 30, 23, 3, 3, 2),
(9, b'1', '23.0010.1494', 'Đo hoạt độ Amylase [Máu]', 30, 23, 3, 3, 2),
(10, b'1', '23.0019.1493', 'Đo hoạt độ ALT (GPT) [Máu]', 30, 23, 3, 3, 2),
(11, b'1', '23.0020.1493', 'Đo hoạt độ AST (GOT) [Máu]', 30, 23, 3, 3, 2),
(12, b'1', '23.0025.1493', 'Định lượng Bilirubin trực tiếp [Máu]', 30, 23, 3, 3, 2),
(13, b'1', '23.0027.1493', 'Định lượng Bilirubin toàn phần [Máu]', 30, 23, 3, 3, 2),
(14, b'1', '23.0030.1472', 'Định lượng Canxi ion hóa [Máu]', 30, 23, 3, 3, 2),
(15, b'1', '23.0041.1506', 'Định lượng Cholesterol toàn phần (máu)', 30, 23, 3, 3, 2),
(16, b'1', '23.0051.1494', 'Định lượng Creatinin (máu)', 30, 23, 3, 3, 2),
(17, b'1', '23.0058.1487', 'Điện giải đồ (Na, K, Cl) [Máu]', 30, 23, 3, 3, 2),
(18, b'1', '23.0075.1494', 'Định lượng Glucose [Máu]', 30, 23, 3, 3, 2),
(19, b'1', '23.0077.1518', 'Đo hoạt độ GGT [Máu]', 30, 23, 3, 3, 2),
(20, b'1', '23.0083.1523', 'Định lượng HbA1c [Máu]', 30, 23, 3, 3, 2),
(21, b'1', '23.0084.1506', 'Định lượng HDL-C [Máu]', 30, 23, 3, 3, 2),
(22, b'1', '23.0112.1506', 'Định lượng LDL-C [Máu]', 30, 23, 3, 3, 2),
(23, b'1', '23.0147.1561', 'Định lượng T3 [Máu]', 30, 23, 3, 3, 2),
(24, b'1', '23.0148.1561', 'Định lượng T4 [Máu]', 30, 23, 3, 3, 2),
(25, b'1', '23.0158.1506', 'Định lượng Triglycerid [Máu]', 30, 23, 3, 3, 2),
(26, b'1', '23.0161.1569', 'Định lượng Troponin I [Máu]', 30, 23, 3, 3, 2),
(27, b'1', '23.0162.1570', 'Định lượng TSH [Máu]', 30, 23, 3, 3, 2),
(28, b'1', '23.0166.1494', 'Định lượng Urê máu [Máu]', 30, 23, 3, 3, 2),
(29, b'1', '23.0228.1483', 'Định lượng CRP', 30, 23, 3, 3, 2),
(30, b'1', '23.0244.1544', 'Phản ứng CRP', 30, 23, 3, 3, 2),
(31, b'1', '18.0001.0001', 'Siêu âm tuyến giáp', 15, 6, 5, 1, 1),
(32, b'1', '18.0002.0001', 'Siêu âm các tuyến nước bọt', 15, 6, 5, 1, 1),
(33, b'1', '18.0003.0001', 'Siêu âm cơ phần mềm vùng cổ mặt', 15, 6, 5, 1, 1),
(34, b'1', '18.0004.0001', 'Siêu âm hạch vùng cổ', 15, 6, 5, 1, 1),
(35, b'1', '18.0012.0001', 'Siêu âm thành ngực (cơ, phần mềm thành ngực)', 15, 6, 5, 1, 1),
(36, b'1', '18.0015.0001', 'Siêu âm ổ bụng (gan mật, tụy, lách, thận, bàng quang)', 15, 6, 5, 1, 1),
(37, b'1', '18.0043.0001', 'Siêu âm khớp (gối, háng, khuỷu, cổ tay….)', 15, 6, 5, 1, 1),
(38, b'1', '18.0044.0001', 'Siêu âm phần mềm (da, tổ chức dưới da, cơ….)', 15, 6, 5, 1, 1),
(39, b'1', '18.0068.0028', 'Chụp X-quang mặt thẳng nghiêng [số hóa 1 phim]', 12, 6, 5, 1, 1),
(40, b'1', '18.0068.0029', 'Chụp X-quang mặt thẳng nghiêng [số hóa 2 phim]', 12, 6, 5, 2, 1),
(41, b'1', '18.0069.0028', 'Chụp X-quang mặt thấp hoặc mặt cao [số hóa 1 phim]', 12, 6, 5, 2, 1),
(42, b'1', '18.0070.0028', 'Chụp X-quang sọ tiếp tuyến [số hóa 1 phim]', 12, 6, 5, 2, 1),
(43, b'1', '18.0073.0028', 'Chụp X-quang Hirtz [số hóa 1 phim]', 12, 6, 5, 2, 1),
(44, b'1', '18.0074.0028', 'Chụp X-quang hàm chếch một bên [số hóa 1 phim]', 12, 6, 5, 2, 1),
(45, b'1', '18.0075.0028', 'Chụp X-quang xương chính mũi nghiêng hoặc tiếp tuyến [số hóa 1 phim]', 12, 6, 5, 2, 1),
(46, b'1', '18.0077.0028', 'Chụp X-quang Chausse III [số hóa 1 phim]', 12, 6, 5, 2, 1),
(47, b'1', '18.0078.0028', 'Chụp X-quang Schuller [số hóa 1 phim]', 12, 6, 5, 2, 1),
(48, b'1', '18.0079.0028', 'Chụp X-quang Stenvers [số hóa 1 phim]', 12, 6, 5, 2, 1),
(49, b'1', '18.0080.0028', 'Chụp X-quang khớp thái dương hàm [số hóa 1 phim]', 12, 6, 5, 2, 1),
(50, b'1', '18.0085.0028', 'Chụp X-quang mỏm trâm [số hóa 1 phim]', 12, 6, 5, 2, 1),
(51, b'1', '18.0086.0028', 'Chụp X-quang cột sống cổ thẳng nghiêng [số hóa 1 phim]', 12, 6, 5, 2, 1),
(52, b'1', '18.0086.0029', 'Chụp X-quang cột sống cổ thẳng nghiêng [số hóa 2 phim]', 12, 6, 5, 2, 1),
(53, b'1', '18.0087.0028', 'Chụp X-quang cột sống cổ chếch hai bên [số hóa 1 phim]', 12, 6, 5, 2, 1),
(54, b'1', '18.0087.0029', 'Chụp X-quang cột sống cổ chếch hai bên [số hóa 2 phim]', 12, 6, 5, 2, 1),
(55, b'1', '18.0088.0030', 'Chụp X-quang cột sống cổ động, nghiêng 3 tư thế [số hóa 3 phim]', 12, 6, 5, 2, 1),
(56, b'1', '18.0089.0028', 'Chụp X-quang cột sống cổ C1-C2 [số hóa 1 phim]', 12, 6, 5, 2, 1),
(57, b'1', '18.0089.0029', 'Chụp X-quang cột sống cổ C1-C2 [số hóa 2 phim]', 12, 6, 5, 2, 1),
(58, b'1', '18.0090.0028', 'Chụp X-quang cột sống ngực thẳng nghiêng hoặc chếch [số hóa 1 phim]', 12, 6, 5, 2, 1),
(59, b'1', '18.0090.0029', 'Chụp X-quang cột sống ngực thẳng nghiêng hoặc chếch [số hóa 2 phim]', 12, 6, 5, 2, 1),
(60, b'1', '18.0091.0028', 'Chụp X-quang cột sống thắt lưng thẳng nghiêng [số hóa 1 phim]', 12, 6, 5, 2, 1),
(61, b'1', '18.0091.0029', 'Chụp X-quang cột sống thắt lưng thẳng nghiêng [số hóa 2 phim]', 12, 6, 5, 2, 1),
(62, b'1', '18.0092.0028', 'Chụp X-quang cột sống thắt lưng chếch hai bên [số hóa 1 phim]', 12, 6, 5, 2, 1),
(63, b'1', '18.0092.0029', 'Chụp X-quang cột sống thắt lưng chếch hai bên [số hóa 2 phim]', 12, 6, 5, 2, 1),
(64, b'1', '18.0093.0028', 'Chụp X-quang cột sống thắt lưng L5-S1 thẳng nghiêng [số hóa 1 phim]', 12, 6, 5, 2, 1),
(65, b'1', '18.0093.0029', 'Chụp X-quang cột sống thắt lưng L5-S1 thẳng nghiêng [số hóa 2 phim]', 12, 6, 5, 2, 1),
(66, b'1', '18.0096.0028', 'Chụp X-quang cột sống cùng cụt thẳng nghiêng [số hóa 1 phim]', 12, 6, 5, 2, 1),
(67, b'1', '18.0096.0029', 'Chụp X-quang cột sống cùng cụt thẳng nghiêng [số hóa 2 phim]', 12, 6, 5, 2, 1),
(68, b'1', '18.0097.0030', 'Chụp X-quang khớp cùng chậu thẳng chếch hai bên [số hóa 3 phim]', 12, 6, 5, 2, 1),
(69, b'1', '18.0098.0028', 'Chụp X-quang khung chậu thẳng [số hóa 1 phim]', 12, 6, 5, 2, 1),
(70, b'1', '18.0099.0028', 'Chụp X-quang xương đòn thẳng hoặc chếch [số hóa 1 phim]', 12, 6, 5, 2, 1),
(71, b'1', '18.0100.0028', 'Chụp X-quang khớp vai thẳng [số hóa 1 phim]', 12, 6, 5, 2, 1),
(72, b'1', '18.0101.0028', 'Chụp X-quang khớp vai nghiêng hoặc chếch [số hóa 1 phim]', 12, 6, 5, 2, 1),
(73, b'1', '18.0102.0028', 'Chụp X-quang xương bả vai thẳng nghiêng [số hóa 1 phim]', 12, 6, 5, 2, 1),
(74, b'1', '18.0102.0029', 'Chụp X-quang xương bả vai thẳng nghiêng [số hóa 2 phim]', 12, 6, 5, 2, 1),
(75, b'1', '18.0103.0028', 'Chụp X-quang xương cánh tay thẳng nghiêng [số hóa 1 phim]', 12, 6, 5, 2, 1),
(76, b'1', '18.0103.0029', 'Chụp X-quang xương cánh tay thẳng nghiêng [số hóa 2 phim]', 12, 6, 5, 2, 1),
(77, b'1', '18.0104.0028', 'Chụp X-quang khớp khuỷu thẳng, nghiêng hoặc chếch [số hóa 1 phim]', 12, 6, 5, 2, 1),
(78, b'1', '18.0104.0029', 'Chụp X-quang khớp khuỷu thẳng, nghiêng hoặc chếch [số hóa 2 phim]', 12, 6, 5, 2, 1),
(79, b'1', '18.0106.0028', 'Chụp X-quang xương cẳng tay thẳng nghiêng [số hóa 1 phim]', 12, 6, 5, 2, 1),
(80, b'1', '18.0106.0029', 'Chụp X-quang xương cẳng tay thẳng nghiêng [số hóa 2 phim]', 12, 6, 5, 2, 1),
(81, b'1', '18.0108.0028', 'Chụp X-quang xương bàn ngón tay thẳng, nghiêng hoặc chếch [số hóa 1 phim]', 12, 6, 5, 2, 1),
(82, b'1', '18.0108.0029', 'Chụp X-quang xương bàn ngón tay thẳng, nghiêng hoặc chếch [số hóa 2 phim]', 12, 6, 5, 2, 1),
(83, b'1', '18.0112.0028', 'Chụp X-quang khớp gối thẳng, nghiêng hoặc chếch [số hóa 1 phim]', 12, 6, 5, 2, 1),
(84, b'1', '18.0112.0029', 'Chụp X-quang khớp gối thẳng, nghiêng hoặc chếch [số hóa 2 phim]', 12, 6, 5, 2, 1),
(85, b'1', '18.0114.0028', 'Chụp X-quang xương cẳng chân thẳng nghiêng [số hóa 1 phim]', 12, 6, 5, 2, 1),
(86, b'1', '18.0114.0029', 'Chụp X-quang xương cẳng chân thẳng nghiêng [số hóa 2 phim]', 12, 6, 5, 2, 1),
(87, b'1', '18.0115.0028', 'Chụp X-quang xương cổ chân thẳng, nghiêng hoặc chếch [số hóa 1 phim]', 12, 6, 5, 2, 1),
(88, b'1', '18.0115.0029', 'Chụp X-quang xương cổ chân thẳng, nghiêng hoặc chếch [số hóa 2 phim]', 12, 6, 5, 2, 1),
(89, b'1', '18.0116.0028', 'Chụp X-quang xương bàn, ngón chân thẳng, nghiêng hoặc chếch [số hóa 1 phim]', 12, 6, 5, 2, 1),
(90, b'1', '18.0116.0029', 'Chụp X-quang xương bàn, ngón chân thẳng, nghiêng hoặc chếch [số hóa 2 phim]', 12, 6, 5, 2, 1),
(91, b'1', '18.0067.0028', 'Chụp X-quang sọ thẳng/nghiêng [số hóa 1 phim]', 12, 6, 5, 2, 1),
(92, b'1', '18.0119.0028', 'Chụp X-quang ngực thẳng [số hóa 1 phim]', 12, 6, 5, 2, 1),
(93, b'1', '18.0120.0028', 'Chụp X-quang ngực nghiêng hoặc chếch mỗi bên [số hóa 1 phim]', 12, 6, 5, 2, 1),
(94, b'1', '18.0111.0028', 'Chụp X-quang xương đùi thẳng nghiêng [số hóa 1 phim]', 12, 6, 5, 2, 1),
(95, b'1', '18.0109.0028', 'Chụp X-quang khớp háng thẳng hai bên [số hóa 1 phim]', 12, 6, 5, 2, 1),
(96, b'1', '18.0107.0028', 'Chụp X-quang xương cổ tay thẳng, nghiêng hoặc chếch [số hóa 1 phim]', 12, 6, 5, 2, 1),
(97, b'1', '18.0110.0028', 'Chụp X-quang khớp háng nghiêng [số hóa 1 phim]', 12, 6, 5, 2, 1),
(98, b'1', 'KKB001', 'Công Khám', 7, 2, 1, NULL, 4),
(99, b'1', '02.03', 'Khám Nội tổng hợp', 4, 2, 1, 98, 4),
(100, b'1', '03.18', 'Khám nhi', 4, 2, 1, 98, 4),
(101, b'1', '10.19', 'Khám Ngoại tổng hợp', 4, 2, 1, 98, 4);

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `lichlamviectheothu`
--

CREATE TABLE `lichlamviectheothu` (
  `id` bigint(20) NOT NULL,
  `thu` enum('CN','T2','T3','T4','T5','T6','T7') NOT NULL,
  `bac_si_id` bigint(20) NOT NULL,
  `ca_lam_viec_id` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Đang đổ dữ liệu cho bảng `lichlamviectheothu`
--

INSERT INTO `lichlamviectheothu` (`id`, `thu`, `bac_si_id`, `ca_lam_viec_id`) VALUES
(1, 'T2', 1, 1),
(2, 'T2', 1, 2),
(3, 'T3', 1, 1),
(4, 'T3', 1, 2),
(5, 'T4', 1, 1),
(6, 'T4', 1, 2),
(7, 'T5', 1, 1),
(8, 'T5', 1, 2),
(9, 'T6', 1, 1),
(10, 'T6', 1, 2),
(11, 'T7', 1, 1),
(12, 'T7', 1, 2),
(13, 'T2', 2, 1),
(14, 'T2', 2, 2),
(15, 'T3', 2, 1),
(16, 'T3', 2, 2),
(17, 'T4', 2, 1),
(18, 'T4', 2, 2),
(19, 'T5', 2, 1),
(20, 'T5', 2, 2),
(21, 'T6', 2, 1),
(22, 'T6', 2, 2),
(23, 'T7', 2, 1),
(24, 'T7', 2, 2),
(36, 'CN', 3, 1),
(25, 'T2', 3, 1),
(26, 'T2', 3, 2),
(27, 'T3', 3, 1),
(28, 'T3', 3, 2),
(29, 'T4', 3, 1),
(30, 'T4', 3, 2),
(31, 'T5', 3, 1),
(32, 'T5', 3, 2),
(33, 'T6', 3, 1),
(34, 'T6', 3, 2),
(35, 'T7', 3, 1),
(47, 'CN', 4, 1),
(48, 'CN', 4, 2),
(37, 'T2', 4, 1),
(38, 'T2', 4, 2),
(39, 'T3', 4, 1),
(40, 'T3', 4, 2),
(41, 'T4', 4, 1),
(42, 'T4', 4, 2),
(43, 'T5', 4, 1),
(44, 'T5', 4, 2),
(45, 'T6', 4, 1),
(46, 'T6', 4, 2),
(59, 'CN', 5, 1),
(60, 'CN', 5, 2),
(49, 'T2', 5, 1),
(50, 'T2', 5, 2),
(51, 'T3', 5, 1),
(52, 'T3', 5, 2),
(53, 'T4', 5, 1),
(54, 'T4', 5, 2),
(55, 'T5', 5, 1),
(56, 'T5', 5, 2),
(57, 'T6', 5, 1),
(58, 'T6', 5, 2),
(169, 'T2', 6, 1),
(170, 'T2', 6, 2),
(171, 'T3', 6, 1),
(172, 'T3', 6, 2),
(173, 'T4', 6, 1),
(174, 'T4', 6, 2),
(175, 'T5', 6, 1),
(176, 'T5', 6, 2),
(177, 'T6', 6, 1),
(178, 'T6', 6, 2),
(179, 'T7', 6, 1),
(180, 'T7', 6, 2),
(157, 'T2', 7, 1),
(158, 'T2', 7, 2),
(159, 'T3', 7, 1),
(160, 'T3', 7, 2),
(161, 'T4', 7, 1),
(162, 'T4', 7, 2),
(163, 'T5', 7, 1),
(164, 'T5', 7, 2),
(165, 'T6', 7, 1),
(166, 'T6', 7, 2),
(167, 'T7', 7, 1),
(168, 'T7', 7, 2),
(145, 'T2', 8, 1),
(146, 'T2', 8, 2),
(147, 'T3', 8, 1),
(148, 'T3', 8, 2),
(149, 'T4', 8, 1),
(150, 'T4', 8, 2),
(151, 'T5', 8, 1),
(152, 'T5', 8, 2),
(153, 'T6', 8, 1),
(154, 'T6', 8, 2),
(155, 'T7', 8, 1),
(156, 'T7', 8, 2),
(133, 'T2', 9, 1),
(134, 'T2', 9, 2),
(135, 'T3', 9, 1),
(136, 'T3', 9, 2),
(137, 'T4', 9, 1),
(138, 'T4', 9, 2),
(139, 'T5', 9, 1),
(140, 'T5', 9, 2),
(141, 'T6', 9, 1),
(142, 'T6', 9, 2),
(143, 'T7', 9, 1),
(144, 'T7', 9, 2),
(248, 'T2', 10, 3),
(249, 'T3', 10, 3),
(250, 'T4', 10, 3),
(251, 'T5', 10, 3),
(252, 'T6', 10, 3),
(121, 'T2', 11, 1),
(122, 'T2', 11, 2),
(123, 'T3', 11, 1),
(124, 'T3', 11, 2),
(125, 'T4', 11, 1),
(126, 'T4', 11, 2),
(127, 'T5', 11, 1),
(128, 'T5', 11, 2),
(129, 'T6', 11, 1),
(130, 'T6', 11, 2),
(131, 'T7', 11, 1),
(132, 'T7', 11, 2),
(109, 'T2', 12, 1),
(110, 'T2', 12, 2),
(111, 'T3', 12, 1),
(112, 'T3', 12, 2),
(113, 'T4', 12, 1),
(114, 'T4', 12, 2),
(115, 'T5', 12, 1),
(116, 'T5', 12, 2),
(117, 'T6', 12, 1),
(118, 'T6', 12, 2),
(119, 'T7', 12, 1),
(120, 'T7', 12, 2),
(97, 'T2', 13, 1),
(98, 'T2', 13, 2),
(99, 'T3', 13, 1),
(100, 'T3', 13, 2),
(101, 'T4', 13, 1),
(102, 'T4', 13, 2),
(103, 'T5', 13, 1),
(104, 'T5', 13, 2),
(105, 'T6', 13, 1),
(106, 'T6', 13, 2),
(107, 'T7', 13, 1),
(108, 'T7', 13, 2),
(85, 'T2', 14, 1),
(86, 'T2', 14, 2),
(87, 'T3', 14, 1),
(88, 'T3', 14, 2),
(89, 'T4', 14, 1),
(90, 'T4', 14, 2),
(91, 'T5', 14, 1),
(92, 'T5', 14, 2),
(93, 'T6', 14, 1),
(94, 'T6', 14, 2),
(95, 'T7', 14, 1),
(96, 'T7', 14, 2),
(83, 'CN', 15, 1),
(84, 'CN', 15, 2),
(73, 'T2', 15, 1),
(74, 'T2', 15, 2),
(75, 'T3', 15, 1),
(76, 'T3', 15, 2),
(77, 'T4', 15, 1),
(78, 'T4', 15, 2),
(79, 'T5', 15, 1),
(80, 'T5', 15, 2),
(81, 'T6', 15, 1),
(82, 'T6', 15, 2),
(61, 'T2', 16, 1),
(62, 'T2', 16, 2),
(63, 'T3', 16, 1),
(64, 'T3', 16, 2),
(65, 'T4', 16, 1),
(66, 'T4', 16, 2),
(67, 'T5', 16, 1),
(68, 'T5', 16, 2),
(69, 'T6', 16, 1),
(70, 'T6', 16, 2),
(71, 'T7', 16, 1),
(72, 'T7', 16, 2);

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `nanglucchuyenmon`
--

CREATE TABLE `nanglucchuyenmon` (
  `id` bigint(20) NOT NULL,
  `vai_tro` enum('DOC_KQ','THUC_HIEN') NOT NULL,
  `bac_si_id` bigint(20) NOT NULL,
  `dvkt_id` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Đang đổ dữ liệu cho bảng `nanglucchuyenmon`
--

INSERT INTO `nanglucchuyenmon` (`id`, `vai_tro`, `bac_si_id`, `dvkt_id`) VALUES
(1, 'DOC_KQ', 14, 31),
(128, 'THUC_HIEN', 14, 31),
(2, 'DOC_KQ', 14, 32),
(129, 'THUC_HIEN', 14, 32),
(3, 'DOC_KQ', 14, 33),
(130, 'THUC_HIEN', 14, 33),
(4, 'DOC_KQ', 14, 34),
(131, 'THUC_HIEN', 14, 34),
(5, 'DOC_KQ', 14, 35),
(132, 'THUC_HIEN', 14, 35),
(6, 'DOC_KQ', 14, 36),
(133, 'THUC_HIEN', 14, 36),
(7, 'DOC_KQ', 14, 37),
(134, 'THUC_HIEN', 14, 37),
(8, 'DOC_KQ', 14, 38),
(135, 'THUC_HIEN', 14, 38),
(9, 'DOC_KQ', 14, 39),
(136, 'THUC_HIEN', 14, 39),
(10, 'DOC_KQ', 14, 40),
(137, 'THUC_HIEN', 14, 40),
(11, 'DOC_KQ', 14, 41),
(138, 'THUC_HIEN', 14, 41),
(12, 'DOC_KQ', 14, 42),
(139, 'THUC_HIEN', 14, 42),
(13, 'DOC_KQ', 14, 43),
(140, 'THUC_HIEN', 14, 43),
(14, 'DOC_KQ', 14, 44),
(141, 'THUC_HIEN', 14, 44),
(15, 'DOC_KQ', 14, 45),
(142, 'THUC_HIEN', 14, 45),
(16, 'DOC_KQ', 14, 46),
(143, 'THUC_HIEN', 14, 46),
(17, 'DOC_KQ', 14, 47),
(144, 'THUC_HIEN', 14, 47),
(18, 'DOC_KQ', 14, 48),
(145, 'THUC_HIEN', 14, 48),
(19, 'DOC_KQ', 14, 49),
(146, 'THUC_HIEN', 14, 49),
(20, 'DOC_KQ', 14, 50),
(147, 'THUC_HIEN', 14, 50),
(21, 'DOC_KQ', 14, 51),
(148, 'THUC_HIEN', 14, 51),
(22, 'DOC_KQ', 14, 52),
(149, 'THUC_HIEN', 14, 52),
(23, 'DOC_KQ', 14, 53),
(150, 'THUC_HIEN', 14, 53),
(24, 'DOC_KQ', 14, 54),
(151, 'THUC_HIEN', 14, 54),
(25, 'DOC_KQ', 14, 55),
(152, 'THUC_HIEN', 14, 55),
(26, 'DOC_KQ', 14, 56),
(153, 'THUC_HIEN', 14, 56),
(27, 'DOC_KQ', 14, 57),
(154, 'THUC_HIEN', 14, 57),
(28, 'DOC_KQ', 14, 58),
(155, 'THUC_HIEN', 14, 58),
(29, 'DOC_KQ', 14, 59),
(156, 'THUC_HIEN', 14, 59),
(30, 'DOC_KQ', 14, 60),
(157, 'THUC_HIEN', 14, 60),
(31, 'DOC_KQ', 14, 61),
(158, 'THUC_HIEN', 14, 61),
(32, 'DOC_KQ', 14, 62),
(159, 'THUC_HIEN', 14, 62),
(33, 'DOC_KQ', 14, 63),
(160, 'THUC_HIEN', 14, 63),
(34, 'DOC_KQ', 14, 64),
(161, 'THUC_HIEN', 14, 64),
(35, 'DOC_KQ', 14, 65),
(162, 'THUC_HIEN', 14, 65),
(36, 'DOC_KQ', 14, 66),
(163, 'THUC_HIEN', 14, 66),
(37, 'DOC_KQ', 14, 67),
(164, 'THUC_HIEN', 14, 67),
(38, 'DOC_KQ', 14, 68),
(165, 'THUC_HIEN', 14, 68),
(39, 'DOC_KQ', 14, 69),
(166, 'THUC_HIEN', 14, 69),
(40, 'DOC_KQ', 14, 70),
(167, 'THUC_HIEN', 14, 70),
(41, 'DOC_KQ', 14, 71),
(168, 'THUC_HIEN', 14, 71),
(42, 'DOC_KQ', 14, 72),
(169, 'THUC_HIEN', 14, 72),
(43, 'DOC_KQ', 14, 73),
(170, 'THUC_HIEN', 14, 73),
(44, 'DOC_KQ', 14, 74),
(171, 'THUC_HIEN', 14, 74),
(45, 'DOC_KQ', 14, 75),
(172, 'THUC_HIEN', 14, 75),
(46, 'DOC_KQ', 14, 76),
(173, 'THUC_HIEN', 14, 76),
(47, 'DOC_KQ', 14, 77),
(174, 'THUC_HIEN', 14, 77),
(48, 'DOC_KQ', 14, 78),
(175, 'THUC_HIEN', 14, 78),
(49, 'DOC_KQ', 14, 79),
(176, 'THUC_HIEN', 14, 79),
(50, 'DOC_KQ', 14, 80),
(177, 'THUC_HIEN', 14, 80),
(51, 'DOC_KQ', 14, 81),
(178, 'THUC_HIEN', 14, 81),
(52, 'DOC_KQ', 14, 82),
(179, 'THUC_HIEN', 14, 82),
(53, 'DOC_KQ', 14, 83),
(180, 'THUC_HIEN', 14, 83),
(54, 'DOC_KQ', 14, 84),
(181, 'THUC_HIEN', 14, 84),
(55, 'DOC_KQ', 14, 85),
(182, 'THUC_HIEN', 14, 85),
(56, 'DOC_KQ', 14, 86),
(183, 'THUC_HIEN', 14, 86),
(57, 'DOC_KQ', 14, 87),
(184, 'THUC_HIEN', 14, 87),
(58, 'DOC_KQ', 14, 88),
(185, 'THUC_HIEN', 14, 88),
(59, 'DOC_KQ', 14, 89),
(186, 'THUC_HIEN', 14, 89),
(60, 'DOC_KQ', 14, 90),
(187, 'THUC_HIEN', 14, 90),
(61, 'DOC_KQ', 14, 91),
(188, 'THUC_HIEN', 14, 91),
(62, 'DOC_KQ', 14, 92),
(189, 'THUC_HIEN', 14, 92),
(63, 'DOC_KQ', 14, 93),
(190, 'THUC_HIEN', 14, 93),
(64, 'DOC_KQ', 14, 94),
(191, 'THUC_HIEN', 14, 94),
(65, 'DOC_KQ', 14, 95),
(192, 'THUC_HIEN', 14, 95),
(66, 'DOC_KQ', 14, 96),
(193, 'THUC_HIEN', 14, 96),
(67, 'DOC_KQ', 14, 97),
(194, 'THUC_HIEN', 14, 97);

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `nhomdvkt`
--

CREATE TABLE `nhomdvkt` (
  `id` bigint(20) NOT NULL,
  `ten_nhom` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Đang đổ dữ liệu cho bảng `nhomdvkt`
--

INSERT INTO `nhomdvkt` (`id`, `ten_nhom`) VALUES
(1, 'Chuẩn đoán hình ảnh'),
(2, 'Xét Nghiệm'),
(3, 'Thủ Thuật'),
(4, 'Khám Bệnh');

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `phancongnangluc`
--

CREATE TABLE `phancongnangluc` (
  `id` bigint(20) NOT NULL,
  `thu` enum('CN','T2','T3','T4','T5','T6','T7') NOT NULL,
  `vai_tro` enum('DOC_KQ','THUC_HIEN') NOT NULL,
  `bac_si_id` bigint(20) NOT NULL,
  `ca_lam_viec_id` bigint(20) NOT NULL,
  `dvkt_id` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Đang đổ dữ liệu cho bảng `phancongnangluc`
--

INSERT INTO `phancongnangluc` (`id`, `thu`, `vai_tro`, `bac_si_id`, `ca_lam_viec_id`, `dvkt_id`) VALUES
(1, 'T2', 'DOC_KQ', 14, 1, 31),
(2, 'T2', 'DOC_KQ', 14, 1, 32),
(3, 'T2', 'DOC_KQ', 14, 1, 33),
(4, 'T2', 'DOC_KQ', 14, 1, 34),
(5, 'T2', 'DOC_KQ', 14, 1, 35),
(6, 'T2', 'DOC_KQ', 14, 1, 36),
(7, 'T2', 'DOC_KQ', 14, 1, 37),
(8, 'T2', 'DOC_KQ', 14, 1, 38),
(9, 'T3', 'DOC_KQ', 14, 1, 31),
(10, 'T3', 'DOC_KQ', 14, 1, 32),
(11, 'T3', 'DOC_KQ', 14, 1, 33),
(12, 'T3', 'DOC_KQ', 14, 1, 34),
(13, 'T3', 'DOC_KQ', 14, 1, 35),
(14, 'T3', 'DOC_KQ', 14, 1, 36),
(15, 'T3', 'DOC_KQ', 14, 1, 37),
(16, 'T3', 'DOC_KQ', 14, 1, 38),
(17, 'T4', 'DOC_KQ', 14, 1, 31),
(18, 'T4', 'DOC_KQ', 14, 1, 32),
(19, 'T4', 'DOC_KQ', 14, 1, 33),
(20, 'T4', 'DOC_KQ', 14, 1, 34),
(21, 'T4', 'DOC_KQ', 14, 1, 35),
(22, 'T4', 'DOC_KQ', 14, 1, 36),
(23, 'T4', 'DOC_KQ', 14, 1, 37),
(24, 'T4', 'DOC_KQ', 14, 1, 38),
(25, 'T5', 'DOC_KQ', 14, 1, 31),
(26, 'T5', 'DOC_KQ', 14, 1, 32),
(27, 'T5', 'DOC_KQ', 14, 1, 33),
(28, 'T5', 'DOC_KQ', 14, 1, 34),
(29, 'T5', 'DOC_KQ', 14, 1, 35),
(30, 'T5', 'DOC_KQ', 14, 1, 36),
(31, 'T5', 'DOC_KQ', 14, 1, 37),
(32, 'T5', 'DOC_KQ', 14, 1, 38),
(33, 'T6', 'DOC_KQ', 14, 1, 31),
(34, 'T6', 'DOC_KQ', 14, 1, 32),
(35, 'T6', 'DOC_KQ', 14, 1, 33),
(36, 'T6', 'DOC_KQ', 14, 1, 34),
(37, 'T6', 'DOC_KQ', 14, 1, 35),
(38, 'T6', 'DOC_KQ', 14, 1, 36),
(39, 'T6', 'DOC_KQ', 14, 1, 37),
(40, 'T6', 'DOC_KQ', 14, 1, 38),
(64, 'T7', 'DOC_KQ', 14, 1, 31),
(65, 'T7', 'DOC_KQ', 14, 1, 32),
(66, 'T7', 'DOC_KQ', 14, 1, 33),
(67, 'T7', 'DOC_KQ', 14, 1, 34),
(68, 'T7', 'DOC_KQ', 14, 1, 35),
(69, 'T7', 'DOC_KQ', 14, 1, 36),
(70, 'T7', 'DOC_KQ', 14, 1, 37),
(71, 'T7', 'DOC_KQ', 14, 1, 38);

--
-- Chỉ mục cho các bảng đã đổ
--

--
-- Chỉ mục cho bảng `bacsi`
--
ALTER TABLE `bacsi`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `UKdmfvyppb4fk911d3tqj9nmv9e` (`ma_bac_si`);

--
-- Chỉ mục cho bảng `bacsi_chuyenkhoa`
--
ALTER TABLE `bacsi_chuyenkhoa`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `UK4ow1rh8nsk4psopbfaa863n2n` (`bac_si_id`,`chuyen_khoa_id`),
  ADD KEY `FK1i78s8br4y7qf0xy4c8dfolm1` (`chuyen_khoa_id`);

--
-- Chỉ mục cho bảng `calamviec`
--
ALTER TABLE `calamviec`
  ADD PRIMARY KEY (`id`);

--
-- Chỉ mục cho bảng `chuyenkhoa`
--
ALTER TABLE `chuyenkhoa`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `UK2aog3e8107em16qk539i6w817` (`ma_chuyen_khoa`),
  ADD KEY `FK1bmqgpbrvmhc55m792jtm25h3` (`chuyen_khoa_cha_id`);

--
-- Chỉ mục cho bảng `dichvukythuat`
--
ALTER TABLE `dichvukythuat`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `UKf8x8r94c1ng5fevwd2x93uv2m` (`ma_dvkt`),
  ADD KEY `FKfyytx462ytytclrwtbeix7lkc` (`chuyen_khoa_id`),
  ADD KEY `FKdi29xns27mhttgjpvopljrrx8` (`dvkt_cha_id`),
  ADD KEY `FK9ff8wehvs8cakadgt2daudchv` (`nhom_dvkt_id`);

--
-- Chỉ mục cho bảng `lichlamviectheothu`
--
ALTER TABLE `lichlamviectheothu`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `UK889yuxvuli6mmytrbej2ln577` (`bac_si_id`,`thu`,`ca_lam_viec_id`),
  ADD KEY `FKi26rgnfntletwdoaqy6x0g6sy` (`ca_lam_viec_id`);

--
-- Chỉ mục cho bảng `nanglucchuyenmon`
--
ALTER TABLE `nanglucchuyenmon`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `UK1qjq4ns4l1lewj0i5ttkee5vm` (`bac_si_id`,`dvkt_id`,`vai_tro`),
  ADD KEY `FK8iya0dq7hgfu7ui8g0ty93vwg` (`dvkt_id`);

--
-- Chỉ mục cho bảng `nhomdvkt`
--
ALTER TABLE `nhomdvkt`
  ADD PRIMARY KEY (`id`);

--
-- Chỉ mục cho bảng `phancongnangluc`
--
ALTER TABLE `phancongnangluc`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `UK6481wlfvixvmj0wpo8t3ygg8l` (`bac_si_id`,`thu`,`ca_lam_viec_id`,`dvkt_id`,`vai_tro`),
  ADD KEY `FKbat9ybnvhs75kj1rckyhykucu` (`ca_lam_viec_id`),
  ADD KEY `FKv9vx6xshf15erlurpkjw9w58` (`dvkt_id`);

--
-- AUTO_INCREMENT cho các bảng đã đổ
--

--
-- AUTO_INCREMENT cho bảng `bacsi`
--
ALTER TABLE `bacsi`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=17;

--
-- AUTO_INCREMENT cho bảng `bacsi_chuyenkhoa`
--
ALTER TABLE `bacsi_chuyenkhoa`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT cho bảng `calamviec`
--
ALTER TABLE `calamviec`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT cho bảng `chuyenkhoa`
--
ALTER TABLE `chuyenkhoa`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;

--
-- AUTO_INCREMENT cho bảng `dichvukythuat`
--
ALTER TABLE `dichvukythuat`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=102;

--
-- AUTO_INCREMENT cho bảng `lichlamviectheothu`
--
ALTER TABLE `lichlamviectheothu`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=253;

--
-- AUTO_INCREMENT cho bảng `nanglucchuyenmon`
--
ALTER TABLE `nanglucchuyenmon`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=255;

--
-- AUTO_INCREMENT cho bảng `nhomdvkt`
--
ALTER TABLE `nhomdvkt`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT cho bảng `phancongnangluc`
--
ALTER TABLE `phancongnangluc`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=79;

--
-- Các ràng buộc cho các bảng đã đổ
--

--
-- Các ràng buộc cho bảng `bacsi_chuyenkhoa`
--
ALTER TABLE `bacsi_chuyenkhoa`
  ADD CONSTRAINT `FK1i78s8br4y7qf0xy4c8dfolm1` FOREIGN KEY (`chuyen_khoa_id`) REFERENCES `chuyenkhoa` (`id`),
  ADD CONSTRAINT `FKrwuxro3utw6e01i5ylipffyne` FOREIGN KEY (`bac_si_id`) REFERENCES `bacsi` (`id`);

--
-- Các ràng buộc cho bảng `chuyenkhoa`
--
ALTER TABLE `chuyenkhoa`
  ADD CONSTRAINT `FK1bmqgpbrvmhc55m792jtm25h3` FOREIGN KEY (`chuyen_khoa_cha_id`) REFERENCES `chuyenkhoa` (`id`);

--
-- Các ràng buộc cho bảng `dichvukythuat`
--
ALTER TABLE `dichvukythuat`
  ADD CONSTRAINT `FK9ff8wehvs8cakadgt2daudchv` FOREIGN KEY (`nhom_dvkt_id`) REFERENCES `nhomdvkt` (`id`),
  ADD CONSTRAINT `FKdi29xns27mhttgjpvopljrrx8` FOREIGN KEY (`dvkt_cha_id`) REFERENCES `dichvukythuat` (`id`),
  ADD CONSTRAINT `FKfyytx462ytytclrwtbeix7lkc` FOREIGN KEY (`chuyen_khoa_id`) REFERENCES `chuyenkhoa` (`id`);

--
-- Các ràng buộc cho bảng `lichlamviectheothu`
--
ALTER TABLE `lichlamviectheothu`
  ADD CONSTRAINT `FK9jtxqtv390e6840tjbj579dw3` FOREIGN KEY (`bac_si_id`) REFERENCES `bacsi` (`id`),
  ADD CONSTRAINT `FKi26rgnfntletwdoaqy6x0g6sy` FOREIGN KEY (`ca_lam_viec_id`) REFERENCES `calamviec` (`id`);

--
-- Các ràng buộc cho bảng `nanglucchuyenmon`
--
ALTER TABLE `nanglucchuyenmon`
  ADD CONSTRAINT `FK8iya0dq7hgfu7ui8g0ty93vwg` FOREIGN KEY (`dvkt_id`) REFERENCES `dichvukythuat` (`id`),
  ADD CONSTRAINT `FKede1a4eabobturufs57jn2bin` FOREIGN KEY (`bac_si_id`) REFERENCES `bacsi` (`id`);

--
-- Các ràng buộc cho bảng `phancongnangluc`
--
ALTER TABLE `phancongnangluc`
  ADD CONSTRAINT `FKbat9ybnvhs75kj1rckyhykucu` FOREIGN KEY (`ca_lam_viec_id`) REFERENCES `calamviec` (`id`),
  ADD CONSTRAINT `FKikfwg1ui4o8jj7l3cpf4yxo3s` FOREIGN KEY (`bac_si_id`) REFERENCES `bacsi` (`id`),
  ADD CONSTRAINT `FKv9vx6xshf15erlurpkjw9w58` FOREIGN KEY (`dvkt_id`) REFERENCES `dichvukythuat` (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
