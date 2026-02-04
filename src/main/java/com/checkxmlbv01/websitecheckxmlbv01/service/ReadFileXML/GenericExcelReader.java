package com.checkxmlbv01.websitecheckxmlbv01.service.ReadFileXML;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

public class GenericExcelReader {

    public static <T> List<T> read(
            String path,
            int totalColumns,
            RowMapper<T> mapper
    ) {

        List<T> list = new ArrayList<>();
        Path tempFile = copyToTemp(path);
        DataFormatter formatter = new DataFormatter();

        try (InputStream is = Files.newInputStream(tempFile);
             Workbook wb = WorkbookFactory.create(is)) {

            Sheet sheet = wb.getSheetAt(0);

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;

                if (row.getLastCellNum() < totalColumns) {
                    throw new RuntimeException(
                        "Dòng " + (i + 1) + " thiếu cột"
                    );
                }

                list.add(mapper.map(row, formatter));
            }

        } catch (Exception e) {
            throw new RuntimeException("Lỗi đọc Excel: " + path, e);
        }

        return list;
    }

    private static Path copyToTemp(String path) {
        try {
            Path source = Paths.get(path);
            Path temp = Files.createTempFile("excel_", ".xlsx");
            Files.copy(source, temp, StandardCopyOption.REPLACE_EXISTING);
            return temp;
        } catch (Exception e) {
            throw new RuntimeException("Không copy được file Excel để đọc", e);
        }
    }
}