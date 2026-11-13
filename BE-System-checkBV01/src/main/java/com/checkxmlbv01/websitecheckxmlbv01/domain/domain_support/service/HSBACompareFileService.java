package com.checkxmlbv01.websitecheckxmlbv01.domain.domain_support.service;

import com.checkxmlbv01.websitecheckxmlbv01.domain.domain_support.listHSBA_winSoft;
import com.checkxmlbv01.websitecheckxmlbv01.domain.domain_support.listHSBA_gateBHYT;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class HSBACompareFileService {

    private final HSBAExcelService excelService;

    public HSBACompareFileService(HSBAExcelService excelService) {
        this.excelService = excelService;
    }

    /**
     * Đọc 2 file Excel, chạy logic đối chiếu và ghi kết quả ra file txt trên server.
     */
    public String compareMultipartExcelAndExportToTxt(MultipartFile winSoftFile, MultipartFile gateBHYTFile, String outputTxtPath) {
        String reportContent = compareMultipartExcelAndGetContent(winSoftFile, gateBHYTFile);

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(outputTxtPath))) {
            bw.write(reportContent);
            return "Đối chiếu thành công! File kết quả đã được lưu tại: " + outputTxtPath;
        } catch (Exception e) {
            e.printStackTrace();
            return "Lỗi khi ghi file báo cáo: " + e.getMessage();
        }
    }

    /**
     * Đọc 2 file Excel và tiến hành đối chiếu:
     * - Kiểm tra trùng lặp mã liên kết File 1 & File 2.
     * - Đối chiếu chéo mã liên kết giữa 2 file.
     * - Thống kê số lượng bệnh nhân theo Ngày ra (chỉ lấy dd/MM/yyyy) và Bác sĩ.
     */
    public String compareMultipartExcelAndGetContent(MultipartFile winSoftFile, MultipartFile gateBHYTFile) {
        StringBuilder report = new StringBuilder();

        try {
            // 1. Đọc dữ liệu từ 2 file Excel
            List<listHSBA_winSoft> winSoftList = excelService.readWinSoftExcel(winSoftFile);
            List<listHSBA_gateBHYT> gateBHYTList = excelService.readGateBHYTExcel(gateBHYTFile);

            // 2. Trích xuất và chuẩn hóa mã liên kết từ WinSoft (File 1)
            Map<String, Integer> file1Map = new TreeMap<>();
            for (listHSBA_winSoft item : winSoftList) {
                String rawCode = (item.getMaLkt() != null && !item.getMaLkt().trim().isEmpty()) 
                                 ? item.getMaLkt() 
                                 : item.getMaLk();
                                 
                String cleanCode = sanitizeCode(rawCode);
                if (cleanCode != null) {
                    file1Map.put(cleanCode, file1Map.getOrDefault(cleanCode, 0) + 1);
                }
            }

            // 3. Trích xuất và chuẩn hóa maLienKet từ Gate BHYT (File 2)
            Map<String, Integer> file2Map = new TreeMap<>();
            for (listHSBA_gateBHYT item : gateBHYTList) {
                String cleanCode = sanitizeCode(item.getMaLienKet());
                if (cleanCode != null) {
                    file2Map.put(cleanCode, file2Map.getOrDefault(cleanCode, 0) + 1);
                }
            }

            boolean hasError = false;

            // --- YÊU CẦU 1: KIỂM TRA TRÙNG LẶP TRONG FILE 1 ---
            report.append("===== KIỂM TRA TRÙNG LẶP FILE 1 (WINSOFT) =====\n");
            boolean hasFile1Duplicate = false;
            for (Map.Entry<String, Integer> entry : file1Map.entrySet()) {
                if (entry.getValue() > 1) {
                    hasError = true;
                    hasFile1Duplicate = true;
                    report.append("FAILED - Mã liên kết [").append(entry.getKey())
                          .append("] bị trùng lặp trong File 1 (Xuất hiện ").append(entry.getValue()).append(" lần)\n");
                }
            }
            if (!hasFile1Duplicate) {
                report.append("-> Không phát hiện mã liên kết trùng lặp trong File 1.\n");
            }

            // --- YÊU CẦU 2: KIỂM TRA TRÙNG LẶP TRONG FILE 2 ---
            report.append("\n===== KIỂM TRA TRÙNG LẶP FILE 2 (GATE BHYT) =====\n");
            boolean hasFile2Duplicate = false;
            for (Map.Entry<String, Integer> entry : file2Map.entrySet()) {
                if (entry.getValue() > 1) {
                    hasError = true;
                    hasFile2Duplicate = true;
                    report.append("FAILED - Mã liên kết [").append(entry.getKey())
                          .append("] bị trùng lặp trong File 2 (Xuất hiện ").append(entry.getValue()).append(" lần)\n");
                }
            }
            if (!hasFile2Duplicate) {
                report.append("-> Không phát hiện mã liên kết trùng lặp trong File 2.\n");
            }

            // --- YÊU CẦU 3: ĐỐI CHIẾU CHÉO GIỮA 2 FILE ---
            report.append("\n===== ĐỐI CHIẾU CHÉO MÃ LIÊN KẾT GIỮA 2 FILE =====\n");
            
            int missingInFile2Count = 0;
            for (String code : file1Map.keySet()) {
                if (!file2Map.containsKey(code)) {
                    hasError = true;
                    missingInFile2Count++;
                    report.append("FAILED - Mã [").append(code).append("] có trong File 1 (WinSoft) nhưng KHÔNG CÓ trong File 2 (Gate BHYT)\n");
                }
            }

            int missingInFile1Count = 0;
            for (String code : file2Map.keySet()) {
                if (!file1Map.containsKey(code)) {
                    hasError = true;
                    missingInFile1Count++;
                    report.append("FAILED - Mã [").append(code).append("] có trong File 2 (Gate BHYT) nhưng KHÔNG CÓ trong File 1 (WinSoft)\n");
                }
            }

            if (missingInFile2Count == 0 && missingInFile1Count == 0) {
                report.append("-> Hai file khớp hoàn toàn về danh sách mã liên kết (Không có mã thừa/thiếu).\n");
            }

            // --- YÊU CẦU 4: THỐNG KÊ GOM NHÓM THEO NGÀY RA (DD/MM/YYYY) VÀ BÁC SĨ ---
            report.append("\n===== THỐNG KÊ SỐ LƯỢNG BỆNH NHÂN THEO NGÀY RA & BÁC SĨ (WINSOFT) =====\n");
            Map<String, Integer> doctorDailyStatsMap = new TreeMap<>();
            
            for (listHSBA_winSoft item : winSoftList) {
                // Lọc chỉ lấy Ngày/Tháng/Năm từ chuỗi ngày ra
                String formattedDate = formatDateOnly(item.getNgayRa());
                
                String bacSy = (item.getBacSy() != null && !item.getBacSy().trim().isEmpty()) 
                               ? item.getBacSy().trim() 
                               : "Chưa phân công BS";
                               
                // Gom nhóm gộp Key: Ngày ra + Bác sĩ
                String key = "Ngày " + formattedDate + " | Bác sĩ: " + bacSy;
                doctorDailyStatsMap.put(key, doctorDailyStatsMap.getOrDefault(key, 0) + 1);
            }

            for (Map.Entry<String, Integer> entry : doctorDailyStatsMap.entrySet()) {
                report.append("- ").append(entry.getKey())
                      .append(" khám được: ").append(entry.getValue()).append(" bệnh nhân\n");
            }

            // Tổng kết kết quả kiểm tra
            report.append("\n==================================================\n");
            if (!hasError) {
                report.append("SUCCESS - DỮ LIỆU HỢP LỆ (Không trùng lặp, khớp 100% giữa 2 file).\n");
            } else {
                report.append("FAILED - PHÁT HIỆN LỖI DỮ LIỆU QUA ĐỐI CHIẾU.\n");
            }

        } catch (Exception e) {
            e.printStackTrace();
            report.append("Lỗi ngoại lệ khi xử lý file: ").append(e.getMessage());
        }

        return report.toString();
    }

    /**
     * Hàm phụ trợ: Trích xuất và định dạng chỉ lấy ngày/tháng/năm (dd/MM/yyyy) từ các dạng chuỗi ngày khác nhau.
     */
    private String formatDateOnly(String rawDate) {
        if (rawDate == null || rawDate.trim().isEmpty()) {
            return "Chưa có ngày ra";
        }
        rawDate = rawDate.trim();

        // 1. Nếu chuỗi đã chứa định dạng kiểu "13/11/2026 08:14" hoặc "13/11/2026"
        Matcher matcher = Pattern.compile("\\d{1,2}/\\d{1,2}/\\d{4}").matcher(rawDate);
        if (matcher.find()) {
            return matcher.group(0);
        }

        // 2. Nếu chuỗi là định dạng ngày kiểu Java toString() (ví dụ: "Fri Nov 13 07:19:00 ICT 2026")
        try {
            SimpleDateFormat inputFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH);
            Date date = inputFormat.parse(rawDate);
            SimpleDateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy");
            return outputFormat.format(date);
        } catch (Exception ignored) {
        }

        // 3. Fallback: Lấy phần tử đầu tiên nếu có khoảng trắng
        return rawDate.split("\\s+")[0];
    }

    /**
     * Hàm chuẩn hóa mã: loại bỏ khoảng trắng, ký tự điều khiển ẩn 
     * và xử lý trường hợp mã số bị Excel đọc nhầm thành dạng thập phân (ví dụ: "123.0").
     */
    private String sanitizeCode(String rawCode) {
        if (rawCode == null) return null;
        
        String clean = rawCode.trim().replaceAll("[\\p{Cntrl}\\s]+", "");
        
        if (clean.isEmpty()) return null;
        
        if (clean.matches("^\\d+\\.0+$")) {
            clean = clean.substring(0, clean.indexOf('.'));
        }
        
        return clean;
    }
}