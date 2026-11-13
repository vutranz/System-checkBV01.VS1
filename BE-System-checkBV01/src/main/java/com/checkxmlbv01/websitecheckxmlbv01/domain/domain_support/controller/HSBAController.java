package com.checkxmlbv01.websitecheckxmlbv01.domain.domain_support.controller;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.checkxmlbv01.websitecheckxmlbv01.domain.DTO.RestResponse;
import com.checkxmlbv01.websitecheckxmlbv01.domain.domain_support.listHSBA_gateBHYT;
import com.checkxmlbv01.websitecheckxmlbv01.domain.domain_support.listHSBA_winSoft;
import com.checkxmlbv01.websitecheckxmlbv01.domain.domain_support.service.HSBACompareFileService;
import com.checkxmlbv01.websitecheckxmlbv01.domain.domain_support.service.HSBAExcelService;
import com.checkxmlbv01.websitecheckxmlbv01.domain.domain_support.service.HSBAReconciliationService;


import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.nio.charset.StandardCharsets;

import java.util.List;

@RestController
@RequestMapping("/api/v1/hsba")
public class HSBAController {

    private final HSBAExcelService excelService;
    private final HSBAReconciliationService reconciliationService;
    private final HSBACompareFileService compareFileService;

    // Constructor injection chuẩn theo mẫu hệ thống
    public HSBAController(HSBAExcelService excelService, HSBAReconciliationService reconciliationService, HSBACompareFileService compareFileService) {
        this.excelService = excelService;
        this.reconciliationService = reconciliationService;
        this.compareFileService = compareFileService;
        
    }

    // API 1: Đọc file Excel WinSoft
    @GetMapping("/read-winsoft")
    public RestResponse<List<listHSBA_winSoft>> readWinSoft(@RequestParam("filePath") String filePath) {
        try {
            List<listHSBA_winSoft> data = excelService.readWinSoftExcel(filePath);
            return RestResponse.success(data);
        } catch (Exception e) {
            return RestResponse.error(
                    "Đọc file Excel WinSoft thất bại",
                    List.of(e.getMessage())
            );
        }
    }

    // API 2: Đọc file Excel Gate BHYT
    @GetMapping("/read-gatebhyt")
    public RestResponse<List<listHSBA_gateBHYT>> readGateBHYT(@RequestParam("filePath") String filePath) {
        try {
            List<listHSBA_gateBHYT> data = excelService.readGateBHYTExcel(filePath);
            return RestResponse.success(data);
        } catch (Exception e) {
            return RestResponse.error(
                    "Đọc file Excel Gate BHYT thất bại",
                    List.of(e.getMessage())
            );
        }
    }

    // API 3: Thực hiện đối soát dữ liệu giữa 2 file Excel
    @PostMapping("/reconcile")
    public RestResponse<List<HSBAReconciliationService.ComparisonResult>> reconcileFiles(
            @RequestParam("winSoftPath") String winSoftPath,
            @RequestParam("gateBHYTPath") String gateBHYTPath) {
        try {
            // Đọc dữ liệu từ 2 nguồn
            List<listHSBA_winSoft> winSoftList = excelService.readWinSoftExcel(winSoftPath);
            List<listHSBA_gateBHYT> gateBHYTList = excelService.readGateBHYTExcel(gateBHYTPath);

            // Chạy logic so sánh đối soát
            List<HSBAReconciliationService.ComparisonResult> result = reconciliationService.compareData(winSoftList, gateBHYTList);

            return RestResponse.success(result);
        } catch (Exception e) {
            return RestResponse.error(
                    "Đối soát dữ liệu HSBA thất bại",
                    List.of(e.getMessage())
            );
        }
    }

    // API đối soát nhận file trực tiếp từ Postman (form-data)
   @PostMapping("/export-compare-txt")
    public ResponseEntity<Resource> exportCompareTxt(
            @RequestParam("winSoftFile") MultipartFile winSoftFile,
            @RequestParam("gateBHYTFile") MultipartFile gateBHYTFile) {
        try {
            // 1. Chạy logic đối chiếu và lấy nội dung báo cáo dạng String
            String reportContent = compareFileService.compareMultipartExcelAndGetContent(winSoftFile, gateBHYTFile);

            // 2. Chuyển nội dung report thành mảng byte UTF-8
            byte[] fileBytes = reportContent.getBytes(StandardCharsets.UTF_8);
            ByteArrayResource resource = new ByteArrayResource(fileBytes);

            // 3. Trả về response kèm Header để trình duyệt/Postman tự động tải về
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=ketqua_doisoat.txt")
                    .contentType(MediaType.TEXT_PLAIN)
                    .contentLength(fileBytes.length)
                    .body(resource);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }
}
