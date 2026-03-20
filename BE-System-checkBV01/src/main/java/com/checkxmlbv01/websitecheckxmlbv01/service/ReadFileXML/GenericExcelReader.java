package com.checkxmlbv01.websitecheckxmlbv01.service.ReadFileXML;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

public class GenericExcelReader {

    public static <T> List<T> read(
            InputStream inputStream,
            int totalColumns,
            RowMapper<T> mapper
    ) {

        List<T> list = new ArrayList<>();
        DataFormatter formatter = new DataFormatter();

        try (Workbook wb = WorkbookFactory.create(inputStream)) {

            Sheet sheet = wb.getSheetAt(0);

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {

                Row row = sheet.getRow(i);

                // Bỏ qua dòng null
                if (row == null) continue;

                // Nếu dòng hoàn toàn rỗng thì bỏ qua
                if (isRowEmpty(row)) continue;

                // Không còn kiểm tra getLastCellNum nữa
                // vì Excel rất dễ thiếu cell cuối

                list.add(mapper.map(row, formatter));
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Lỗi đọc Excel: " + e.getMessage(), e);
        }

        return list;
    }

    // Kiểm tra dòng có rỗng hoàn toàn không
    private static boolean isRowEmpty(Row row) {
        for (int i = 0; i < row.getLastCellNum(); i++) {
            Cell cell = row.getCell(i, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
            if (cell != null && cell.getCellType() != CellType.BLANK) {
                return false;
            }
        }
        return true;
    }
}



// public class GenericExcelReader {

//     /**
//      * Cách 1: Đọc từ File (Khuyên dùng nếu file đã nằm trên ổ đĩa)
//      * Giúp POI truy cập ngẫu nhiên dữ liệu, tránh lỗi Block not found tốt hơn InputStream.
//      */
//     public static <T> List<T> read(File file, RowMapper<T> mapper) {
//         try (Workbook wb = WorkbookFactory.create(file)) {
//             return processSheet(wb, mapper);
//         } catch (Exception e) {
//             e.printStackTrace();
//             throw new RuntimeException("Lỗi cấu trúc tệp Excel (File): " + e.getMessage(), e);
//         }
//     }

//     /**
//      * Cách 2: Đọc từ InputStream (Dùng cho Web/Upload)
//      */
//     public static <T> List<T> read(InputStream inputStream, RowMapper<T> mapper) {
//         // WorkbookFactory.create(inputStream) tự động xử lý đóng stream nếu truyền trực tiếp
//         // nhưng ta bọc trong try-with-resources của Workbook để giải phóng bộ nhớ tạm.
//         try (Workbook wb = WorkbookFactory.create(inputStream)) {
//             return processSheet(wb, mapper);
//         } catch (Exception e) {
//             e.printStackTrace();
//             // Lỗi "Block not found" thường bắn ra từ đây
//             throw new RuntimeException("Tệp Excel bị hỏng cấu trúc hoặc không đầy đủ: " + e.getMessage(), e);
//         }
//     }

//     private static <T> List<T> processSheet(Workbook wb, RowMapper<T> mapper) {
//         List<T> list = new ArrayList<>();
//         DataFormatter formatter = new DataFormatter();
//         Sheet sheet = wb.getSheetAt(0);

//         // Sử dụng sheet.getLastRowNum() có thể bỏ sót dòng cuối nếu dùng i < lastRowNum
//         // Nên dùng i <= lastRowNum hoặc dùng Iterator
//         for (int i = 1; i <= sheet.getLastRowNum(); i++) {
//             Row row = sheet.getRow(i);

//             if (row == null || isRowEmpty(row)) {
//                 continue;
//             }

//             try {
//                 T mappedObject = mapper.map(row, formatter);
//                 if (mappedObject != null) {
//                     list.add(mappedObject);
//                 }
//             } catch (Exception e) {
//                 // Log lỗi dòng cụ thể để dễ debug nhưng không làm chết cả quá trình đọc
//                 System.err.println("Lỗi tại dòng " + (i + 1) + ": " + e.getMessage());
//             }
//         }
//         return list;
//     }

//     private static boolean isRowEmpty(Row row) {
//         // Sử dụng cell đầu tiên hoặc duyệt nhanh để check rỗng
//         for (int i = 0; i < row.getLastCellNum(); i++) {
//             Cell cell = row.getCell(i, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
//             if (cell != null && cell.getCellType() != CellType.BLANK) {
//                 return false;
//             }
//         }
//         return true;
//     }
// }
