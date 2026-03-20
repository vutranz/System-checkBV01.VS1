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
import com.checkxmlbv01.websitecheckxmlbv01.domain.DTO.BacSiChuyenKhoa.BacSiChuyenKhoaRequest;
import com.checkxmlbv01.websitecheckxmlbv01.domain.DTO.BacSiChuyenKhoa.BacSiChuyenKhoaResponse;
import com.checkxmlbv01.websitecheckxmlbv01.service.BacSiChuyenKhoa.BacSiChuyenKhoaService;


@RestController
@RequestMapping("/api/v1/bacsi-chuyenkhoa")
public class BacSiChuyenKhoaController {

    private final BacSiChuyenKhoaService service;

    public BacSiChuyenKhoaController(BacSiChuyenKhoaService service) {
        this.service = service;
    }

    @PostMapping
    public RestResponse<BacSiChuyenKhoaResponse> create(
            @RequestBody BacSiChuyenKhoaRequest request) {

        try {
            return RestResponse.success(service.create(request));
        } catch (Exception e) {
            return RestResponse.error(
                    "Gán chuyên khoa cho bác sĩ thất bại",
                    List.of(e.getMessage())
            );
        }
    }

    @DeleteMapping("/{id}")
    public RestResponse<Void> delete(@PathVariable Long id) {

        try {
            service.delete(id);
            return RestResponse.success(null);
        } catch (Exception e) {
            return RestResponse.error(
                    "Xoá thất bại",
                    List.of(e.getMessage())
            );
        }
    }

    @GetMapping
    public RestResponse<List<BacSiChuyenKhoaResponse>> getAll() {

        try {
            return RestResponse.success(service.getAll());
        } catch (Exception e) {
            return RestResponse.error(
                    "Lấy dữ liệu thất bại",
                    List.of(e.getMessage())
            );
        }
    }

    @GetMapping("/bac-si/{bacSiId}")
    public RestResponse<List<BacSiChuyenKhoaResponse>> getByBacSi(
            @PathVariable Long bacSiId) {

        try {
            return RestResponse.success(service.getByBacSi(bacSiId));
        } catch (Exception e) {
            return RestResponse.error(
                    "Không tìm thấy dữ liệu",
                    List.of(e.getMessage())
            );
        }
    }

    @PutMapping("/{id}")
    public RestResponse<BacSiChuyenKhoaResponse> update(
            @PathVariable Long id,
            @RequestBody BacSiChuyenKhoaRequest request) {

        try {
            return RestResponse.success(service.update(id, request));
        } catch (Exception e) {
            return RestResponse.error(
                    "Cập nhật thất bại",
                    List.of(e.getMessage())
            );
        }
    }
}
