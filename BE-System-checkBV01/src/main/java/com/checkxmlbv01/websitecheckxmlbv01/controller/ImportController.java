package com.checkxmlbv01.websitecheckxmlbv01.controller;


import java.io.ByteArrayInputStream;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpHeaders; // ✅ ĐÚNG
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.checkxmlbv01.websitecheckxmlbv01.service.Import.ImportService;
import com.checkxmlbv01.websitecheckxmlbv01.service.ReadFileXML.HoSoKhamBenh;


@RestController
@RequestMapping("/api/import")
public class ImportController {

    private final ImportService importService;

    public ImportController(ImportService importService) {
        this.importService = importService;
    }

    // 🔵 POST: import file
    @PostMapping("/ho-so")
    public ResponseEntity<?> importHoSo(
            @RequestParam("xml1") MultipartFile xml1File,
            @RequestParam("xml2") MultipartFile xml2File,
            @RequestParam("xml3") MultipartFile xml3File,
            @RequestParam("xml4") MultipartFile xml4File
    ) {
        try {
            importService.importHoSo(xml1File, xml2File, xml3File, xml4File);
            return ResponseEntity.ok("Import thành công");
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body("Import thất bại: " + e.getMessage());
        }
    }

    // 🟢 GET: lấy tất cả hồ sơ
   @GetMapping("/ho-so")
    public ResponseEntity<?> getFirst() {
        List<?> list = importService.getAll();
        return ResponseEntity.ok(list.isEmpty() ? null : list.get(0));
    }

    // 🟢 GET: lấy theo mã LK
    @GetMapping("/ho-so/{maLk}")
    public ResponseEntity<?> getByMaLk(@PathVariable String maLk) {

        HoSoKhamBenh hoSo = importService.findByMaLk(maLk);

        if (hoSo == null) {
            return ResponseEntity
                    .badRequest()
                    .body("Không tìm thấy mã LK: " + maLk);
        }

        return ResponseEntity.ok(hoSo);
    }

    // 🟢 EXPORT EXCEL
    @GetMapping("/export-excel")
    public ResponseEntity<byte[]> exportExcel() {
        try {

            ByteArrayInputStream excel = importService.exportExcel();

            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition",
                    "attachment; filename=ket-qua.xlsx");

            return ResponseEntity.ok()
                    .headers(headers)
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(excel.readAllBytes());

        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(("Lỗi: " + e.getMessage()).getBytes());
        }
    }
}