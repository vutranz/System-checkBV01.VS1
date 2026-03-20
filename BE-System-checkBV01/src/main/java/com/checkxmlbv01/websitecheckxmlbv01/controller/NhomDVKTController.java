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
import com.checkxmlbv01.websitecheckxmlbv01.domain.DTO.NhomDVKT.NhomDVKTRequest;
import com.checkxmlbv01.websitecheckxmlbv01.domain.DTO.NhomDVKT.NhomDVKTResponse;
import com.checkxmlbv01.websitecheckxmlbv01.service.NhomDVKT.NhomDVKTService;

@RestController
@RequestMapping("/api/v1/nhom-dvkt")

public class NhomDVKTController {

    private final NhomDVKTService service;

     public NhomDVKTController(NhomDVKTService service) {
        this.service = service;
    }
    @PostMapping
    public RestResponse<NhomDVKTResponse> create(
            @RequestBody NhomDVKTRequest request) {

        try {
            return RestResponse.success(service.create(request));
        } catch (Exception e) {
            return RestResponse.error(
                    "Tạo nhóm thất bại",
                    List.of(e.getMessage())
            );
        }
    }

    @PutMapping("/{id}")
    public RestResponse<NhomDVKTResponse> update(
            @PathVariable Long id,
            @RequestBody NhomDVKTRequest request) {

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
    public RestResponse<List<NhomDVKTResponse>> getAll() {
        return RestResponse.success(service.getAll());
    }

    @GetMapping("/{id}")
    public RestResponse<NhomDVKTResponse> getById(
            @PathVariable Long id) {

        return RestResponse.success(service.getById(id));
    }

    @DeleteMapping("/{id}")
    public RestResponse<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return RestResponse.success(null);
    }
}
