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
import com.checkxmlbv01.websitecheckxmlbv01.domain.DTO.NangLucChuyenMon.NangLucChuyenMonRequest;
import com.checkxmlbv01.websitecheckxmlbv01.domain.DTO.NangLucChuyenMon.NangLucChuyenMonResponse;
import com.checkxmlbv01.websitecheckxmlbv01.service.NangLucChuyenMon.NangLucChuyenMonService;


@RestController
@RequestMapping("/api/v1/nang-luc-chuyen-mon")
public class NangLucChuyenMonController {

    private final NangLucChuyenMonService service;

     public NangLucChuyenMonController(NangLucChuyenMonService service) {
        this.service = service;
    }

    @PostMapping
    public RestResponse<NangLucChuyenMonResponse> create(
            @RequestBody NangLucChuyenMonRequest request) {

        try {
            return RestResponse.success(service.create(request));
        } catch (Exception e) {
            return RestResponse.error(
                    "Tạo năng lực thất bại",
                    List.of(e.getMessage())
            );
        }
    }

    @PutMapping("/{id}")
    public RestResponse<NangLucChuyenMonResponse> update(
            @PathVariable Long id,
            @RequestBody NangLucChuyenMonRequest request) {

        try {
            return RestResponse.success(service.update(id, request));
        } catch (Exception e) {
            return RestResponse.error(
                    "Cập nhật thất bại",
                    List.of(e.getMessage())
            );
        }
    }

    @DeleteMapping("/{id}")
    public RestResponse<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return RestResponse.success(null);
    }

    @GetMapping
    public RestResponse<List<NangLucChuyenMonResponse>> getAll() {
        return RestResponse.success(service.getAll());
    }

    @GetMapping("/bac-si/{bacSiId}")
    public RestResponse<List<NangLucChuyenMonResponse>> getByBacSi(
            @PathVariable Long bacSiId) {

        return RestResponse.success(service.getByBacSi(bacSiId));
    }

    @GetMapping("/dvkt/{dvktId}")
    public RestResponse<List<NangLucChuyenMonResponse>> getByDvkt(
            @PathVariable Long dvktId) {

        return RestResponse.success(service.getByDvkt(dvktId));
    }
}