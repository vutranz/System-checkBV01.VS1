package com.checkxmlbv01.websitecheckxmlbv01.controller;
import com.checkxmlbv01.websitecheckxmlbv01.domain.DTO.RestResponse;
import com.checkxmlbv01.websitecheckxmlbv01.domain.DTO.DichVuKyThuat.DichVuKyThuatRequest;
import com.checkxmlbv01.websitecheckxmlbv01.domain.DTO.DichVuKyThuat.DichVuKyThuatResponse;
import com.checkxmlbv01.websitecheckxmlbv01.domain.DTO.DichVuKyThuat.DichVuKyThuatTreeResponse;
import com.checkxmlbv01.websitecheckxmlbv01.service.DichVuKyThuat.DichVuKyThuatService;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@RestController
@RequestMapping("/api/v1/dich-vu-ky-thuat")

public class DichVuKyThuatController {

    private final DichVuKyThuatService service;

    public DichVuKyThuatController(DichVuKyThuatService service) {
        this.service = service;
    }
    @PostMapping
    public RestResponse<DichVuKyThuatResponse> create(
            @RequestBody DichVuKyThuatRequest request) {

        return RestResponse.success(service.create(request));
    }

    // =========================
    // UPDATE
    // =========================
    @PutMapping("/{id}")
    public RestResponse<DichVuKyThuatResponse> update(
            @PathVariable Long id,
            @RequestBody DichVuKyThuatRequest request) {

        return RestResponse.success(service.update(id, request));
    }

    @DeleteMapping("/{id}")
    public RestResponse<Void> delete(@PathVariable Long id) {

        service.delete(id);
        return RestResponse.success(null);
    }
    
    @GetMapping
    public RestResponse<List<DichVuKyThuatResponse>> getAll() {

        return RestResponse.success(service.getAll());
    }

    @GetMapping("/{id}")
    public RestResponse<DichVuKyThuatResponse> getById(
            @PathVariable Long id) {

        return RestResponse.success(service.getById(id));
    }


    @GetMapping("/root")
    public RestResponse<List<DichVuKyThuatResponse>> getRoot() {

        return RestResponse.success(service.getRoot());
    }

    
    @GetMapping("/{id}/children")
    public RestResponse<List<DichVuKyThuatResponse>> getChildren(
            @PathVariable Long id) {

        return RestResponse.success(service.getChildren(id));
    }

    @GetMapping("/tree")
    public RestResponse<List<DichVuKyThuatTreeResponse>> getTree() {
        return RestResponse.success(service.getTree());
    }
}