package com.checkxmlbv01.websitecheckxmlbv01.controller;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.checkxmlbv01.websitecheckxmlbv01.domain.DTO.RestResponse;
import com.checkxmlbv01.websitecheckxmlbv01.domain.DTO.NhomDVKT.NhomDVKTReqDTO;
import com.checkxmlbv01.websitecheckxmlbv01.domain.DTO.NhomDVKT.NhomDVKTResDTO;
import com.checkxmlbv01.websitecheckxmlbv01.domain.DTO.NhomDVKT.NhomDVKTUpdateDTO;
import com.checkxmlbv01.websitecheckxmlbv01.service.NhomDVKTService;
import com.checkxmlbv01.websitecheckxmlbv01.ultil.annotation.ApiMessage;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/nhom-dvkt")
public class NhomDVKTController {

    private final NhomDVKTService nhomDVKTService;

    public NhomDVKTController(NhomDVKTService nhomDVKTService) {
        this.nhomDVKTService = nhomDVKTService;
    }

    @PostMapping
    @ApiMessage("Tạo nhóm dịch vụ kỹ thuật")
    public ResponseEntity<RestResponse<NhomDVKTResDTO>> create(
            @RequestBody NhomDVKTReqDTO request) {

        NhomDVKTResDTO data = nhomDVKTService.create(request);
        return ResponseEntity.ok(RestResponse.success(data));
    }

    @GetMapping
    @ApiMessage("Lấy danh sách nhóm dịch vụ kỹ thuật")
    public ResponseEntity<RestResponse<List<NhomDVKTResDTO>>> getAll() {

        List<NhomDVKTResDTO> data = nhomDVKTService.getAll();
        return ResponseEntity.ok(RestResponse.success(data));
    }

    @GetMapping("/{id}")
    @ApiMessage("Lấy chi tiết nhóm dịch vụ kỹ thuật")
    public ResponseEntity<RestResponse<NhomDVKTResDTO>> getById(
            @PathVariable Long id) {

        NhomDVKTResDTO data = nhomDVKTService.getById(id);
        return ResponseEntity.ok(RestResponse.success(data));
    }

    @PutMapping("/{id}")
    @ApiMessage("Cập nhật nhóm dịch vụ kỹ thuật")
    public ResponseEntity<RestResponse<NhomDVKTResDTO>> update(
            @PathVariable Long id,
            @Valid @RequestBody NhomDVKTUpdateDTO request) {

        NhomDVKTResDTO data = nhomDVKTService.update(id, request);
        return ResponseEntity.ok(RestResponse.success(data));
    }

    @DeleteMapping("/{id}")
    @ApiMessage("Xóa nhóm dịch vụ kỹ thuật")
    public ResponseEntity<RestResponse<Void>> delete(
            @PathVariable Long id) {

        nhomDVKTService.delete(id);
        return ResponseEntity.ok(RestResponse.success(null));
    }
}
