package com.checkxmlbv01.websitecheckxmlbv01.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.checkxmlbv01.websitecheckxmlbv01.domain.DTO.RestResponse;
import com.checkxmlbv01.websitecheckxmlbv01.domain.DTO.PhanCongNangLuc.PhanCongNangLucRequest;
import com.checkxmlbv01.websitecheckxmlbv01.domain.DTO.PhanCongNangLuc.PhanCongNangLucResponse;
import com.checkxmlbv01.websitecheckxmlbv01.domain.enums.ThuTrongTuan;
import com.checkxmlbv01.websitecheckxmlbv01.service.PhanCongNangLuc.PhanCongNangLucService;


@RestController
@RequestMapping("/api/v1/phan-cong-nang-luc")
public class PhanCongNangLucController {

    private final PhanCongNangLucService service;

    public PhanCongNangLucController(PhanCongNangLucService service) {
        this.service = service;
    }

    @PostMapping
    public RestResponse<PhanCongNangLucResponse> create(
            @RequestBody PhanCongNangLucRequest request) {

        return RestResponse.success(service.create(request));
    }


    @PutMapping("/{id}")
    public RestResponse<PhanCongNangLucResponse> update(
            @PathVariable Long id,
            @RequestBody PhanCongNangLucRequest request) {

        return RestResponse.success(service.update(id, request));
    }


    @DeleteMapping("/{id}")
    public RestResponse<Void> delete(@PathVariable Long id) {

        service.delete(id);
        return RestResponse.success(null);
    }


    @GetMapping
    public RestResponse<List<PhanCongNangLucResponse>> getAll() {

        return RestResponse.success(service.getAll());
    }


    @GetMapping("/bac-si/{bacSiId}")
    public RestResponse<List<PhanCongNangLucResponse>> getByBacSi(
            @PathVariable Long bacSiId) {

        return RestResponse.success(service.getByBacSi(bacSiId));
    }


    @GetMapping("/dvkt/{dvktId}")
    public RestResponse<List<PhanCongNangLucResponse>> getByDvkt(
            @PathVariable Long dvktId) {

        return RestResponse.success(service.getByDvkt(dvktId));
    }

   @PostMapping("/filter")
    public RestResponse<List<PhanCongNangLucResponse>> getByThuAndCa(
            @RequestParam ThuTrongTuan thu,
            @RequestParam Long caLamViecId) {

        return RestResponse.success(
                service.getByThuAndCa(thu, caLamViecId)
        );
    }
}
