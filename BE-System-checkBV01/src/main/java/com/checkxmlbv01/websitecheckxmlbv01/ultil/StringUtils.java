package com.checkxmlbv01.websitecheckxmlbv01.ultil;

public class StringUtils {
    /**
     * Chuẩn hóa chuỗi: Trim dấu cách, nếu null trả về null
     */
    public static String norm(String s) {
        return s == null ? null : s.trim();
    }

    /**
     * Kiểm tra chuỗi trống sau khi trim
     */
    public static boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }
}
