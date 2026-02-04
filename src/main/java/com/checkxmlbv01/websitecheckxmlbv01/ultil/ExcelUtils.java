package com.checkxmlbv01.websitecheckxmlbv01.ultil;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;

public class ExcelUtils {

    public static String getStringCellValue(Cell cell, DataFormatter formatter) {
        if (cell == null) return null;
        return formatter.formatCellValue(cell).trim();
    }

    public static Integer getIntegerCellValue(Cell cell, DataFormatter formatter) {
        String value = getStringCellValue(cell, formatter);
        if (value == null || value.isEmpty()) return null;
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public static Double getDoubleCellValue(Cell cell, DataFormatter formatter) {
        String value = getStringCellValue(cell, formatter);
        if (value == null || value.isEmpty()) return null;
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}

