package com.checkxmlbv01.websitecheckxmlbv01.controller;
import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.checkxmlbv01.websitecheckxmlbv01.domain.DTO.RestResponse;
import com.checkxmlbv01.websitecheckxmlbv01.domain.DTO.LichLamViecTheoThu.LichLamViecTheoThuRequest;
import com.checkxmlbv01.websitecheckxmlbv01.domain.DTO.LichLamViecTheoThu.LichLamViecTheoThuResponse;
import com.checkxmlbv01.websitecheckxmlbv01.service.LichLamViecTheoThu.LichLamViecTheoThuService;
@RestController
@RequestMapping("/api/v1/lich-lam-viec-theo-thu")

public class LichLamViecTheoThuController {

    private final LichLamViecTheoThuService service;

    public LichLamViecTheoThuController(LichLamViecTheoThuService service) {
        this.service = service;
    }

    @PostMapping
    public RestResponse<LichLamViecTheoThuResponse> create(
            @RequestBody LichLamViecTheoThuRequest request) {

        try {
            return RestResponse.success(service.create(request));
        } catch (Exception e) {
            return RestResponse.error(
                    "Tạo lịch thất bại",
                    List.of(e.getMessage())
            );
        }
    }

    @PutMapping("/{id}")
    public RestResponse<LichLamViecTheoThuResponse> update(
            @PathVariable Long id,
            @RequestBody LichLamViecTheoThuRequest request) {

        try {
            return RestResponse.success(service.update(id, request));
        } catch (Exception e) {
            return RestResponse.error(
                    "Cập nhật thất bại",
                    List.of(e.getMessage())
            );
        }
    }

    @GetMapping
    public RestResponse<List<LichLamViecTheoThuResponse>> getAll() {
        return RestResponse.success(service.getAll());
    }

    @GetMapping("/bac-si/{bacSiId}")
    public RestResponse<List<LichLamViecTheoThuResponse>> getByBacSi(
            @PathVariable Long bacSiId) {

        return RestResponse.success(service.getByBacSi(bacSiId));
    }

    @DeleteMapping("/{id}")
    public RestResponse<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return RestResponse.success(null);
    }
}
