package com.checkxmlbv01.websitecheckxmlbv01.service.ReadFileXML;

import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import com.checkxmlbv01.websitecheckxmlbv01.domain.xml.xml2;
import com.checkxmlbv01.websitecheckxmlbv01.ultil.ExcelUtils;

public class ExcelReader_XML2 implements RowMapper<xml2>{
    @Override
     public xml2 map(Row row, DataFormatter formatter) {
        xml2 xml = new xml2();

            xml.setMaLk(ExcelUtils.getStringCellValue(row.getCell(0), formatter));
            xml.setStt(ExcelUtils.getIntegerCellValue(row.getCell(1), formatter));
            xml.setMaThuoc(ExcelUtils.getStringCellValue(row.getCell(2), formatter));
            xml.setMaPpChebien(ExcelUtils.getStringCellValue(row.getCell(3), formatter));
            xml.setMaCskcbThuoc(ExcelUtils.getStringCellValue(row.getCell(4), formatter));
            xml.setMaNhom(ExcelUtils.getStringCellValue(row.getCell(5), formatter));
            xml.setTenThuoc(ExcelUtils.getStringCellValue(row.getCell(6), formatter));
            xml.setDonViTinh(ExcelUtils.getStringCellValue(row.getCell(7), formatter));
            xml.setHamLuong(ExcelUtils.getStringCellValue(row.getCell(8), formatter));
            xml.setDuongDung(ExcelUtils.getStringCellValue(row.getCell(9), formatter));
            xml.setDangBaoChe(ExcelUtils.getStringCellValue(row.getCell(10), formatter));
            xml.setLieuDung(ExcelUtils.getStringCellValue(row.getCell(11), formatter));
            xml.setCachDung(ExcelUtils.getStringCellValue(row.getCell(12), formatter));
            xml.setSoDangKy(ExcelUtils.getStringCellValue(row.getCell(13), formatter));
            xml.setTtThau(ExcelUtils.getStringCellValue(row.getCell(14), formatter));
            xml.setPhamVi(ExcelUtils.getStringCellValue(row.getCell(15), formatter));
            xml.setTyleTtBh(ExcelUtils.getDoubleCellValue(row.getCell(16), formatter));
            xml.setSoLuong(ExcelUtils.getDoubleCellValue(row.getCell(17), formatter));
            xml.setDonGia(ExcelUtils.getDoubleCellValue(row.getCell(18), formatter));
            xml.setThanhTienBv(ExcelUtils.getDoubleCellValue(row.getCell(19), formatter));
            xml.setThanhTienBh(ExcelUtils.getDoubleCellValue(row.getCell(20), formatter));
            xml.setTNguonkhacNsnn(ExcelUtils.getDoubleCellValue(row.getCell(21), formatter));
            xml.setTNguonkhacVtnn(ExcelUtils.getDoubleCellValue(row.getCell(22), formatter));
            xml.setTNguonkhacVttn(ExcelUtils.getDoubleCellValue(row.getCell(23), formatter));
            xml.setTNguonkhacCl(ExcelUtils.getDoubleCellValue(row.getCell(24), formatter));
            xml.setTNguonkhac(ExcelUtils.getDoubleCellValue(row.getCell(25), formatter));
            xml.setMucHuong(ExcelUtils.getDoubleCellValue(row.getCell(26), formatter));
            xml.setTBntt(ExcelUtils.getDoubleCellValue(row.getCell(27), formatter));
            xml.setTBncct(ExcelUtils.getDoubleCellValue(row.getCell(28), formatter));
            xml.setTBhtt(ExcelUtils.getDoubleCellValue(row.getCell(29), formatter));
            xml.setMaKhoa(ExcelUtils.getStringCellValue(row.getCell(30), formatter));
            xml.setMaBacSi(ExcelUtils.getStringCellValue(row.getCell(31), formatter));
            xml.setMaDichVu(ExcelUtils.getStringCellValue(row.getCell(32), formatter));
            xml.setNgayYl(ExcelUtils.getStringCellValue(row.getCell(33), formatter));
            xml.setNgayThYl(ExcelUtils.getStringCellValue(row.getCell(34), formatter));
            xml.setMaPttt(ExcelUtils.getStringCellValue(row.getCell(35), formatter));
            xml.setNguonCtra(ExcelUtils.getStringCellValue(row.getCell(36), formatter));
            xml.setVetThuongTp(ExcelUtils.getStringCellValue(row.getCell(37), formatter));
            xml.setDuPhong(ExcelUtils.getStringCellValue(row.getCell(38), formatter));

            return xml;
    }
}
