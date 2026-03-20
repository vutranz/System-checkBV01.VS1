package com.checkxmlbv01.websitecheckxmlbv01.service.Import;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.poi.ss.usermodel.Sheet; // ✅ đúng
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import com.checkxmlbv01.websitecheckxmlbv01.domain.ErrorGroup.ErrorKCBDetail;
import com.checkxmlbv01.websitecheckxmlbv01.domain.ErrorGroup.ErrorKCBGroup;
import com.checkxmlbv01.websitecheckxmlbv01.ultil.TimeUtils;

@Service
public class ExcelExportService {

   /* public ByteArrayInputStream export(ErrorKCBGroup group) throws Exception {

        Workbook wb = new XSSFWorkbook();
        Sheet sheet = wb.createSheet("Errors");

        // 🔥 Header
        Row header = sheet.createRow(0);
        String[] cols = {
                "Mã LK", "Mã DV", "Tên DV",
                "Ngày YL", "Ngày THYL", "Ngày KQ", "Lỗi"
        };

        CellStyle headerStyle = wb.createCellStyle();
        Font font = wb.createFont();
        font.setBold(true);
        headerStyle.setFont(font);

        for (int i = 0; i < cols.length; i++) {
            Cell cell = header.createCell(i);
            cell.setCellValue(cols[i]);
            cell.setCellStyle(headerStyle);
        }

        // 🔥 Data
        int rowIdx = 1;

        for (ErrorKCBDetail e : group.getErrors()) {

            Row row = sheet.createRow(rowIdx++);

            row.createCell(0).setCellValue(nvl(e.getMaLk()));
            row.createCell(1).setCellValue(nvl(e.getMaDichVu()));
            row.createCell(2).setCellValue(nvl(e.getTenDichVu()));
            row.createCell(3).setCellValue(nvl(e.getNgayYL()));
            row.createCell(4).setCellValue(nvl(e.getNgayTHYL()));
            row.createCell(5).setCellValue(nvl(e.getNgaykq()));
            row.createCell(6).setCellValue(nvl(e.getErrorDetail()));
        }

        // auto size
        for (int i = 0; i < cols.length; i++) {
            sheet.autoSizeColumn(i);
        }

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        wb.write(out);
        wb.close();

        return new ByteArrayInputStream(out.toByteArray());
    }*/ 

        
    private String nvl(String s) {
        return s == null ? "" : s;
    }

    

    public ByteArrayInputStream export(ErrorKCBGroup group) throws Exception {

        try (Workbook wb = new SXSSFWorkbook();
             ByteArrayOutputStream out = new ByteArrayOutputStream()) {

            Sheet sheet = wb.createSheet("CHECK BV01");

            int rowIdx = 0;

            // 🔥 Title
            Row titleRow = sheet.createRow(rowIdx++);
            titleRow.createCell(0).setCellValue("===== CHECK BV01 =====");

            rowIdx++; // dòng trống

            // 🔥 Group theo mã LK
            Map<String, List<ErrorKCBDetail>> map =
                    group.getErrors().stream()
                            .collect(Collectors.groupingBy(ErrorKCBDetail::getMaLk));

            for (Map.Entry<String, List<ErrorKCBDetail>> entry : map.entrySet()) {

                String maLk = entry.getKey();
                List<ErrorKCBDetail> list = entry.getValue();

                // 👉 Hồ sơ lỗi
                Row hoSoRow = sheet.createRow(rowIdx++);
                hoSoRow.createCell(0).setCellValue(maLk);

                // 👉 List lỗi
                for (ErrorKCBDetail e : list) {
                   
                    String line = String.format(
                    "DV: %s | Bác Sĩ CĐ: %s | Ngày YL: %s | Ngày TH: %s | KQ: %s | Lỗi: %s",
                    nvl(e.getTenDichVu()),
                    nvl(e.getHoten()),
                    TimeUtils.format(e.getNgayYL()),
                    TimeUtils.format(e.getNgayTHYL()),
                    TimeUtils.format(e.getNgaykq()),
                    nvl(e.getErrorDetail())
            );

                    Row row = sheet.createRow(rowIdx++);
                    row.createCell(0).setCellValue(line);
                }

                rowIdx++; // cách dòng giữa các hồ sơ
            }

            sheet.setColumnWidth(0, 20000); // rộng cho dễ đọc

            wb.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        }
    }
}
