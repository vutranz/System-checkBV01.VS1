package com.checkxmlbv01.websitecheckxmlbv01.ultil;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.stereotype.Component;

@Component
public class TimeUtils {

    private static final DateTimeFormatter FMT =
            DateTimeFormatter.ofPattern("yyyyMMddHHmm");

    private static final DateTimeFormatter OUT_FMT = 
            DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
            
    public static LocalDateTime parse(String time) {
        if (time == null) return null;
        try {
            return LocalDateTime.parse(time, FMT);
        } catch (Exception e) {
            return null;
        }
    }

    // Thêm static vào đây
    public static String format(String time) {
        LocalDateTime dt = parse(time);
        if (dt == null) return "";

        DateTimeFormatter outFmt = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        return dt.format(outFmt);
    }

    public String normalizeCCHN(String cchn) {
        if (cchn == null) return null;
        return cchn.split("/")[0].trim();
    }
}