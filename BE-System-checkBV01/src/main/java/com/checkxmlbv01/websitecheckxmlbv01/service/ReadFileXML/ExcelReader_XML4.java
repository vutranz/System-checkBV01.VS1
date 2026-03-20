package com.checkxmlbv01.websitecheckxmlbv01.service.ReadFileXML;

import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;

import com.checkxmlbv01.websitecheckxmlbv01.domain.xml.xml4;
import com.checkxmlbv01.websitecheckxmlbv01.ultil.ExcelUtils;

public class ExcelReader_XML4 implements RowMapper<xml4> {

    @Override
    public xml4 map(Row row, DataFormatter formatter) {
        xml4 xml = new xml4();

        xml.setMaLk(ExcelUtils.getStringCellValue(row.getCell(0), formatter));
        xml.setStt(ExcelUtils.getIntegerCellValue(row.getCell(1), formatter));
        xml.setMaDichVu(ExcelUtils.getStringCellValue(row.getCell(2), formatter));
        xml.setMaChiSo(ExcelUtils.getStringCellValue(row.getCell(3), formatter));
        xml.setTenChiSo(ExcelUtils.getStringCellValue(row.getCell(4), formatter));
        xml.setGiaTri(ExcelUtils.getStringCellValue(row.getCell(5), formatter));
        xml.setDonViDo(ExcelUtils.getStringCellValue(row.getCell(6), formatter));
        xml.setMoTa(ExcelUtils.getStringCellValue(row.getCell(7), formatter));
        xml.setKetLuan(ExcelUtils.getStringCellValue(row.getCell(8), formatter));
        xml.setNgayKq(ExcelUtils.getStringCellValue(row.getCell(9), formatter));
        xml.setMaBsDocKq(ExcelUtils.getStringCellValue(row.getCell(10), formatter));
        xml.setDuPhong(ExcelUtils.getStringCellValue(row.getCell(11), formatter));

        return xml;
    }
}
