package com.checkxmlbv01.websitecheckxmlbv01.controller;

import org.springframework.web.bind.annotation.*;


import com.checkxmlbv01.websitecheckxmlbv01.domain.DTO.RestResponse;
import com.checkxmlbv01.websitecheckxmlbv01.domain.DTO.ChuyenKhoa.ChuyenKhoaRequest;
import com.checkxmlbv01.websitecheckxmlbv01.domain.DTO.ChuyenKhoa.ChuyenKhoaResponse;
import com.checkxmlbv01.websitecheckxmlbv01.service.ChuyenKhoa.ChuyenKhoaService;
import com.checkxmlbv01.websitecheckxmlbv01.ultil.annotation.ApiMessage;

import java.util.List;

@RestController
@RequestMapping("/api/v1/chuyen-khoa")
public class ChuyenKhoaController {

    private final ChuyenKhoaService service;

    public ChuyenKhoaController(ChuyenKhoaService service) {
        this.service = service;
    }

    @PostMapping
    @ApiMessage("Tạo chuyên khoa")
    public RestResponse<ChuyenKhoaResponse> create(
            @RequestBody ChuyenKhoaRequest request) {

        try {
            return RestResponse.success(service.create(request));
        } catch (Exception e) {
            return RestResponse.error(
                    "Tạo chuyên khoa thất bại",
                    List.of(e.getMessage())
            );
        }
    }

    @PutMapping("/{id}")
    @ApiMessage("Update chuyên khoa")
    public RestResponse<ChuyenKhoaResponse> update(
            @PathVariable Long id,
            @RequestBody ChuyenKhoaRequest request) {

        try {
            return RestResponse.success(service.update(id, request));
        } catch (Exception e) {
            return RestResponse.error(
                    "Cập nhật thất bại",
                    List.of(e.getMessage())
            );
        }
    }

    @ApiMessage("Xóa chuyên khoa")
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

    @ApiMessage("Lấy chuyên khoa theo ID")
    @GetMapping("/{id}")
    public RestResponse<ChuyenKhoaResponse> getById(
            @PathVariable Long id) {

        try {
            return RestResponse.success(service.getById(id));
        } catch (Exception e) {
            return RestResponse.error(
                    "Không tìm thấy",
                    List.of(e.getMessage())
            );
        }
    }

    @ApiMessage("Lấy tất cả chuyên khoa")
    @GetMapping
    public RestResponse<List<ChuyenKhoaResponse>> getAll() {
        return RestResponse.success(service.getAll());
    }
}
