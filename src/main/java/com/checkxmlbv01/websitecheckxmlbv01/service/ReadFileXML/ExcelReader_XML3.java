package com.checkxmlbv01.websitecheckxmlbv01.service.ReadFileXML;

import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;

import com.checkxmlbv01.websitecheckxmlbv01.domain.xml.xml3;
import com.checkxmlbv01.websitecheckxmlbv01.ultil.ExcelUtils;

public class ExcelReader_XML3 implements RowMapper<xml3>{
    @Override
     public xml3 map(Row row, DataFormatter formatter) {
        xml3 xml = new xml3();

           xml.setMaLk(ExcelUtils.getStringCellValue(row.getCell(0), formatter));
            xml.setStt(ExcelUtils.getIntegerCellValue(row.getCell(1), formatter));
            xml.setMaDichVu(ExcelUtils.getStringCellValue(row.getCell(2), formatter));
            xml.setMaPtttQt(ExcelUtils.getStringCellValue(row.getCell(3), formatter));
            xml.setMaVatTu(ExcelUtils.getStringCellValue(row.getCell(4), formatter));
            xml.setMaNhom(ExcelUtils.getStringCellValue(row.getCell(5), formatter));
            xml.setGoiVtyt(ExcelUtils.getStringCellValue(row.getCell(6), formatter));
            xml.setTenVatTu(ExcelUtils.getStringCellValue(row.getCell(7), formatter));
            xml.setTenDichVu(ExcelUtils.getStringCellValue(row.getCell(8), formatter));
            xml.setMaXangDau(ExcelUtils.getStringCellValue(row.getCell(9), formatter));
            xml.setDonViTinh(ExcelUtils.getStringCellValue(row.getCell(10), formatter));
            xml.setPhamVi(ExcelUtils.getStringCellValue(row.getCell(11), formatter));
            xml.setSoLuong(ExcelUtils.getDoubleCellValue(row.getCell(12), formatter));
            xml.setDonGiaBv(ExcelUtils.getDoubleCellValue(row.getCell(13), formatter));
            xml.setDonGiaBh(ExcelUtils.getDoubleCellValue(row.getCell(14), formatter));
            xml.setTtThau(ExcelUtils.getStringCellValue(row.getCell(15), formatter));
            xml.setTyleTtDv(ExcelUtils.getDoubleCellValue(row.getCell(16), formatter));
            xml.setTyleTtBh(ExcelUtils.getDoubleCellValue(row.getCell(17), formatter));
            xml.setThanhTienBv(ExcelUtils.getDoubleCellValue(row.getCell(18), formatter));
            xml.setThanhTienBh(ExcelUtils.getDoubleCellValue(row.getCell(19), formatter));
            xml.setTTrantt(ExcelUtils.getDoubleCellValue(row.getCell(20), formatter));
            xml.setMucHuong(ExcelUtils.getDoubleCellValue(row.getCell(21), formatter));
            xml.setTNguonKhacNsnn(ExcelUtils.getDoubleCellValue(row.getCell(22), formatter));
            xml.setTNguonKhacVtnn(ExcelUtils.getDoubleCellValue(row.getCell(23), formatter));
            xml.setTNguonKhacVttn(ExcelUtils.getDoubleCellValue(row.getCell(24), formatter));
            xml.setTNguonKhacCl(ExcelUtils.getDoubleCellValue(row.getCell(25), formatter));
            xml.setTNguonKhac(ExcelUtils.getDoubleCellValue(row.getCell(26), formatter));
            xml.setTBntt(ExcelUtils.getDoubleCellValue(row.getCell(27), formatter));
            xml.setTBncct(ExcelUtils.getDoubleCellValue(row.getCell(28), formatter));
            xml.setTBhtt(ExcelUtils.getDoubleCellValue(row.getCell(29), formatter));
            xml.setMaKhoa(ExcelUtils.getStringCellValue(row.getCell(30), formatter));
            xml.setMaGiuong(ExcelUtils.getStringCellValue(row.getCell(31), formatter));
            xml.setMaBacSi(ExcelUtils.getStringCellValue(row.getCell(32), formatter));
            xml.setNguoiThucHien(ExcelUtils.getStringCellValue(row.getCell(33), formatter));
            xml.setMaBenh(ExcelUtils.getStringCellValue(row.getCell(34), formatter));
            xml.setMaBenhYhct(ExcelUtils.getStringCellValue(row.getCell(35), formatter));
            xml.setNgayYl(ExcelUtils.getStringCellValue(row.getCell(36), formatter));
            xml.setNgayThYl(ExcelUtils.getStringCellValue(row.getCell(37), formatter));
            xml.setNgayKq(ExcelUtils.getStringCellValue(row.getCell(38), formatter));
            xml.setMaPttt(ExcelUtils.getStringCellValue(row.getCell(39), formatter));
            xml.setVetThuongTp(ExcelUtils.getStringCellValue(row.getCell(40), formatter));
            xml.setPpVoCam(ExcelUtils.getStringCellValue(row.getCell(41), formatter));
            xml.setViTriThDvkt(ExcelUtils.getStringCellValue(row.getCell(42), formatter));
            xml.setMaMay(ExcelUtils.getStringCellValue(row.getCell(43), formatter));
            xml.setMaHieuSp(ExcelUtils.getStringCellValue(row.getCell(44), formatter));
            xml.setTaiSuDung(ExcelUtils.getStringCellValue(row.getCell(45), formatter));
            xml.setDuPhong(ExcelUtils.getStringCellValue(row.getCell(46), formatter));

            return xml;
    }
}
