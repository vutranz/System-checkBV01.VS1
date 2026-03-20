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
import com.checkxmlbv01.websitecheckxmlbv01.domain.DTO.CaLamViec.CaLamViecRequest;
import com.checkxmlbv01.websitecheckxmlbv01.domain.DTO.CaLamViec.CaLamViecResponse;
import com.checkxmlbv01.websitecheckxmlbv01.service.CaLamViec.CaLamViecService;

@RestController
@RequestMapping("/api/v1/ca-lam-viec")

public class CaLamViecController {

    private final CaLamViecService service;

    public CaLamViecController(CaLamViecService service) {
        this.service = service;
    }

    @PostMapping
    public RestResponse<CaLamViecResponse> create(
            @RequestBody CaLamViecRequest request) {

        try {
            return RestResponse.success(service.create(request));
        } catch (Exception e) {
            return RestResponse.error(
                    "Tạo ca làm việc thất bại",
                    List.of(e.getMessage())
            );
        }
    }

    @PutMapping("/{id}")
    public RestResponse<CaLamViecResponse> update(
            @PathVariable Long id,
            @RequestBody CaLamViecRequest request) {

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
    public RestResponse<List<CaLamViecResponse>> getAll() {
        return RestResponse.success(service.getAll());
    }

    @GetMapping("/{id}")
    public RestResponse<CaLamViecResponse> getById(@PathVariable Long id) {
        return RestResponse.success(service.getById(id));
    }

    @DeleteMapping("/{id}")
    public RestResponse<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return RestResponse.success(null);
    }
}
