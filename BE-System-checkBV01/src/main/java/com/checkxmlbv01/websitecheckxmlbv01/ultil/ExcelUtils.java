package com.checkxmlbv01.websitecheckxmlbv01.ultil;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;

public class ExcelUtils {

    public static String getStringCellValue(Cell cell, DataFormatter formatter) {
    if (cell == null) return "";
    return formatter.formatCellValue(cell).trim();
    }

    public static Integer getIntegerCellValue(Cell cell, DataFormatter formatter) {
        if (cell == null) return 0;
        String value = formatter.formatCellValue(cell);
        return value.isEmpty() ? 0 : Integer.parseInt(value);
    }

    public static Double getDoubleCellValue(Cell cell, DataFormatter formatter) {
        if (cell == null) return 0.0;
        String value = formatter.formatCellValue(cell);
        return value.isEmpty() ? 0.0 : Double.parseDouble(value);
    }
}

