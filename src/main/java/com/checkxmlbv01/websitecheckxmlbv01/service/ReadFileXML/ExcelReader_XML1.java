package com.checkxmlbv01.websitecheckxmlbv01.service.ReadFileXML;
import org.apache.poi.ss.usermodel.*;

import com.checkxmlbv01.websitecheckxmlbv01.domain.xml.xml1;
import com.checkxmlbv01.websitecheckxmlbv01.ultil.ExcelUtils;

public class ExcelReader_XML1 implements RowMapper<xml1>{

    @Override
     public xml1 map(Row row, DataFormatter formatter) {
        xml1 xml = new xml1();

        xml.setMaLk(ExcelUtils.getStringCellValue(row.getCell(0), formatter));
        xml.setStt(ExcelUtils.getStringCellValue(row.getCell(1), formatter));
        xml.setMaBn(ExcelUtils.getStringCellValue(row.getCell(2), formatter));
        xml.setHoTen(ExcelUtils.getStringCellValue(row.getCell(3), formatter));
        xml.setSoCccd(ExcelUtils.getStringCellValue(row.getCell(4), formatter));
        xml.setNgaySinh(ExcelUtils.getStringCellValue(row.getCell(5), formatter));
        xml.setGioiTinh(ExcelUtils.getStringCellValue(row.getCell(6), formatter));
        xml.setNhomMau(ExcelUtils.getStringCellValue(row.getCell(7), formatter));
        xml.setMaQuocTich(ExcelUtils.getStringCellValue(row.getCell(8), formatter));
        xml.setMaDanToc(ExcelUtils.getStringCellValue(row.getCell(9), formatter));
        xml.setMaNgheNghiep(ExcelUtils.getStringCellValue(row.getCell(10), formatter));
        xml.setDiaChi(ExcelUtils.getStringCellValue(row.getCell(11), formatter));
        xml.setMaTinhCuTru(ExcelUtils.getStringCellValue(row.getCell(12), formatter));
        xml.setMaHuyenCuTru(ExcelUtils.getStringCellValue(row.getCell(13), formatter));
        xml.setMaXaCuTru(ExcelUtils.getStringCellValue(row.getCell(14), formatter));
        xml.setDienThoai(ExcelUtils.getStringCellValue(row.getCell(15), formatter));
        xml.setMaTheBhyt(ExcelUtils.getStringCellValue(row.getCell(16), formatter));
        xml.setMaDkbd(ExcelUtils.getStringCellValue(row.getCell(17), formatter));
        xml.setGtTheTu(ExcelUtils.getStringCellValue(row.getCell(18), formatter));
        xml.setGtTheDen(ExcelUtils.getStringCellValue(row.getCell(19), formatter));
        xml.setNgayMienCct(ExcelUtils.getStringCellValue(row.getCell(20), formatter));
        xml.setLyDoVv(ExcelUtils.getStringCellValue(row.getCell(21), formatter));
        xml.setLyDoVnt(ExcelUtils.getStringCellValue(row.getCell(22), formatter));
        xml.setMaLyDoVnt(ExcelUtils.getStringCellValue(row.getCell(23), formatter));
        xml.setChanDoanVao(ExcelUtils.getStringCellValue(row.getCell(24), formatter));
        xml.setChanDoanRv(ExcelUtils.getStringCellValue(row.getCell(25), formatter));
        xml.setMaBenhChinh(ExcelUtils.getStringCellValue(row.getCell(26), formatter));
        xml.setMaBenhKt(ExcelUtils.getStringCellValue(row.getCell(27), formatter));
        xml.setMaBenhYhct(ExcelUtils.getStringCellValue(row.getCell(28), formatter));
        xml.setMaPtttQt(ExcelUtils.getStringCellValue(row.getCell(29), formatter));
        xml.setMaDoiTuongKcb(ExcelUtils.getStringCellValue(row.getCell(30), formatter));
        xml.setMaNoiDi(ExcelUtils.getStringCellValue(row.getCell(31), formatter));
        xml.setMaNoiDen(ExcelUtils.getStringCellValue(row.getCell(32), formatter));
        xml.setMaTaiNan(ExcelUtils.getStringCellValue(row.getCell(33), formatter));
        xml.setNgayVao(ExcelUtils.getStringCellValue(row.getCell(34), formatter));
        xml.setNgayVaoNoiTru(ExcelUtils.getStringCellValue(row.getCell(35), formatter));
        xml.setNgayRa(ExcelUtils.getStringCellValue(row.getCell(36), formatter));
        xml.setGiayChuyenTuyen(ExcelUtils.getStringCellValue(row.getCell(37), formatter));
        xml.setSoNgayDtri(ExcelUtils.getIntegerCellValue(row.getCell(38), formatter));
        xml.setPpDieuTri(ExcelUtils.getStringCellValue(row.getCell(39), formatter));
        xml.setKetQuaDtri(ExcelUtils.getStringCellValue(row.getCell(40), formatter));
        xml.setMaLoaiRv(ExcelUtils.getStringCellValue(row.getCell(41), formatter));
        xml.setGhiChu(ExcelUtils.getStringCellValue(row.getCell(42), formatter));
        xml.setNgayTtoan(ExcelUtils.getStringCellValue(row.getCell(43), formatter));
        xml.setTThuoc(ExcelUtils.getDoubleCellValue(row.getCell(44), formatter));
        xml.setTVtyt(ExcelUtils.getDoubleCellValue(row.getCell(45), formatter));
        xml.setTTongchiBv(ExcelUtils.getDoubleCellValue(row.getCell(46), formatter));
        xml.setTTongchiBh(ExcelUtils.getDoubleCellValue(row.getCell(47), formatter));
        xml.setTBntt(ExcelUtils.getDoubleCellValue(row.getCell(48), formatter));
        xml.setTBncct(ExcelUtils.getDoubleCellValue(row.getCell(49), formatter));
        xml.setTBhtt(ExcelUtils.getDoubleCellValue(row.getCell(50), formatter));
        xml.setTNguonKhac(ExcelUtils.getDoubleCellValue(row.getCell(51), formatter));
        xml.setTBhttGdv(ExcelUtils.getDoubleCellValue(row.getCell(52), formatter));
        xml.setNamQt(ExcelUtils.getIntegerCellValue(row.getCell(53), formatter));
        xml.setThangQt(ExcelUtils.getIntegerCellValue(row.getCell(54), formatter));
        xml.setMaLoaiKcb(ExcelUtils.getStringCellValue(row.getCell(55), formatter));
        xml.setMaKhoa(ExcelUtils.getStringCellValue(row.getCell(56), formatter));
        xml.setMaCskcb(ExcelUtils.getStringCellValue(row.getCell(57), formatter));
        xml.setMaKhuvuc(ExcelUtils.getStringCellValue(row.getCell(58), formatter));
        xml.setCanNang(ExcelUtils.getDoubleCellValue(row.getCell(59), formatter));
        xml.setCanNangCon(ExcelUtils.getDoubleCellValue(row.getCell(60), formatter));
        xml.setNamNamLienTuc(ExcelUtils.getIntegerCellValue(row.getCell(61), formatter));
        xml.setNgayTaiKham(ExcelUtils.getStringCellValue(row.getCell(62), formatter));
        xml.setMaHsba(ExcelUtils.getStringCellValue(row.getCell(63), formatter));
        xml.setMaTtdv(ExcelUtils.getStringCellValue(row.getCell(64), formatter));
        xml.setDuPhong(ExcelUtils.getStringCellValue(row.getCell(65), formatter));

        return xml;
    }
}
