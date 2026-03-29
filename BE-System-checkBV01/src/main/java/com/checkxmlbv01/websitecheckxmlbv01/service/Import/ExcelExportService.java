package com.checkxmlbv01.websitecheckxmlbv01.service.Import;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.poi.ss.usermodel.Sheet; // ✅ đúng
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import org.springframework.stereotype.Service;

import com.checkxmlbv01.websitecheckxmlbv01.domain.ErrorGroup.ErrorKCBDetail;
import com.checkxmlbv01.websitecheckxmlbv01.domain.ErrorGroup.ErrorKCBGroup;
import com.checkxmlbv01.websitecheckxmlbv01.ultil.TimeUtils;


@Service
public class ExcelExportService {

    private String nvl(String s) {
        return s == null ? "" : s;
    }

    public ByteArrayInputStream export(ErrorKCBGroup group) throws Exception {

        try (Workbook wb = new SXSSFWorkbook();
             ByteArrayOutputStream out = new ByteArrayOutputStream()) {

            Sheet sheet = wb.createSheet("CHECK BV01");

            int rowIdx = 0;

            // ================= STYLE =================

            // 🔥 FONT CHUNG
            Font normalFont = wb.createFont();
            normalFont.setFontName("Times New Roman");
            normalFont.setFontHeightInPoints((short) 8);

            // 🔥 TITLE FONT (ĐỎ ĐẬM)
            Font titleFont = wb.createFont();
            titleFont.setFontName("Times New Roman");
            titleFont.setFontHeightInPoints((short) 13);
            titleFont.setBold(true);
            titleFont.setColor(IndexedColors.RED.getIndex());

            CellStyle titleStyle = wb.createCellStyle();
            titleStyle.setFont(titleFont);
            titleStyle.setAlignment(HorizontalAlignment.CENTER);
            titleStyle.setVerticalAlignment(VerticalAlignment.CENTER);

            // 🔥 HEADER STYLE (XANH LÁ)
            Font headerFont = wb.createFont();
            headerFont.setFontName("Times New Roman");
            headerFont.setFontHeightInPoints((short) 12);
            headerFont.setBold(true);
            headerFont.setColor(IndexedColors.WHITE.getIndex());

            CellStyle headerStyle = wb.createCellStyle();
            headerStyle.setFont(headerFont);
            headerStyle.setAlignment(HorizontalAlignment.CENTER);
            headerStyle.setVerticalAlignment(VerticalAlignment.CENTER);
            headerStyle.setFillForegroundColor(IndexedColors.GREEN.getIndex());
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            // 🔥 DATA STYLE
            CellStyle dataStyle = wb.createCellStyle();
            dataStyle.setFont(normalFont);
            dataStyle.setVerticalAlignment(VerticalAlignment.CENTER);

            // 🔥 WRAP TEXT (cột lỗi)
            CellStyle wrapStyle = wb.createCellStyle();
            wrapStyle.cloneStyleFrom(dataStyle);
            wrapStyle.setWrapText(true);

            // ================= TITLE =================

            Row titleRow = sheet.createRow(rowIdx++);
            titleRow.setHeightInPoints(20);

            Cell titleCell = titleRow.createCell(0);
            titleCell.setCellValue("===== CHECK BV01 =====");
            titleCell.setCellStyle(titleStyle);

            sheet.addMergedRegion(new CellRangeAddress(
                    titleRow.getRowNum(),
                    titleRow.getRowNum(),
                    0,
                    11
            ));

            rowIdx++;

            // ================= HEADER =================

            Row header = sheet.createRow(rowIdx++);
            int col = 0;

            String[] headers = {
                    "Mã LK", "Mã BN", "Họ tên", "Mã DV", "Tên dịch vụ",
                    "BS chỉ định", "BS thực hiện", "BS đọc KQ",
                    "Ngày YL", "Ngày THYL", "Ngày KQ", "Chi tiết lỗi"
            };

            for (String h : headers) {
                Cell cell = header.createCell(col++);
                cell.setCellValue(h);
                cell.setCellStyle(headerStyle);
            }

            sheet.createFreezePane(0, rowIdx);

            // ================= DATA =================

            Map<String, List<ErrorKCBDetail>> map =
                    group.getErrors().stream()
                            .collect(Collectors.groupingBy(ErrorKCBDetail::getMaLk));

            for (Map.Entry<String, List<ErrorKCBDetail>> entry : map.entrySet()) {

                List<ErrorKCBDetail> list = entry.getValue();

                for (ErrorKCBDetail e : list) {

                    Row row = sheet.createRow(rowIdx++);
                    int c = 0;

                    Cell cell;

                    cell = row.createCell(c++);
                    cell.setCellValue(nvl(e.getMaLk()));
                    cell.setCellStyle(dataStyle);

                    cell = row.createCell(c++);
                    cell.setCellValue(nvl(e.getMaBn()));
                    cell.setCellStyle(dataStyle);

                    cell = row.createCell(c++);
                    cell.setCellValue(nvl(e.getHoten()));
                    cell.setCellStyle(dataStyle);

                    cell = row.createCell(c++);
                    cell.setCellValue(nvl(e.getMaDichVu()));
                    cell.setCellStyle(dataStyle);

                    cell = row.createCell(c++);
                    cell.setCellValue(nvl(e.getTenDichVu()));
                    cell.setCellStyle(dataStyle);

                    cell = row.createCell(c++);
                    cell.setCellValue(nvl(e.getMaBsCd()));
                    cell.setCellStyle(dataStyle);

                    cell = row.createCell(c++);
                    cell.setCellValue(nvl(e.getMaBsTh()));
                    cell.setCellStyle(dataStyle);

                    cell = row.createCell(c++);
                    cell.setCellValue(nvl(e.getMaBsDocKq()));
                    cell.setCellStyle(dataStyle);

                    cell = row.createCell(c++);
                    cell.setCellValue(TimeUtils.format(nvl(e.getNgayYl())));
                    cell.setCellStyle(dataStyle);

                    cell = row.createCell(c++);
                    cell.setCellValue(TimeUtils.format(nvl(e.getNgayThyl())));
                    cell.setCellStyle(dataStyle);

                    cell = row.createCell(c++);
                    cell.setCellValue(TimeUtils.format(nvl(e.getNgayKq())));
                    cell.setCellStyle(dataStyle);

                    cell = row.createCell(c++);
                    cell.setCellValue(nvl(e.getErrorDetail()));
                    cell.setCellStyle(wrapStyle);
                }

                rowIdx++;
            }

            // ================= WIDTH =================

            for (int i = 0; i < 12; i++) {
                sheet.setColumnWidth(i, 5000);
            }

            sheet.setColumnWidth(4, 8000);
            sheet.setColumnWidth(11, 12000);

            wb.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        }
    }
}