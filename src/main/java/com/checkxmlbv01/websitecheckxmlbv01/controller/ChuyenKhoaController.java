package com.checkxmlbv01.websitecheckxmlbv01.controller;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.checkxmlbv01.websitecheckxmlbv01.ultil.annotation.ApiMessage;

import jakarta.validation.Valid;

import com.checkxmlbv01.websitecheckxmlbv01.domain.DTO.RestResponse;
import com.checkxmlbv01.websitecheckxmlbv01.domain.DTO.ChuyenKhoaDTO.ChuyenKhoaReqDTO;
import com.checkxmlbv01.websitecheckxmlbv01.domain.DTO.ChuyenKhoaDTO.ChuyenKhoaResDTO;
import com.checkxmlbv01.websitecheckxmlbv01.domain.DTO.ChuyenKhoaDTO.ChuyenKhoaUpdateDTO;
import com.checkxmlbv01.websitecheckxmlbv01.service.ChuyenKhoaService;
@RestController
@RequestMapping("/api/v1/chuyen-khoa")
public class ChuyenKhoaController {

    private final ChuyenKhoaService chuyenKhoaService;

    public ChuyenKhoaController(ChuyenKhoaService chuyenKhoaService){
        this.chuyenKhoaService=chuyenKhoaService;
    }
    @PostMapping
    @ApiMessage("Tạo chuyên khoa")
    public ResponseEntity<RestResponse<ChuyenKhoaResDTO>> create(
            @Valid @RequestBody ChuyenKhoaReqDTO request) {

        return ResponseEntity.ok(
                RestResponse.success(chuyenKhoaService.create(request))
        );
    }

    @PutMapping("/{id}")
    @ApiMessage("Cập nhật chuyên khoa")
    public ResponseEntity<RestResponse<ChuyenKhoaResDTO>> update(
            @PathVariable Long id,
            @RequestBody ChuyenKhoaUpdateDTO request) {

        return ResponseEntity.ok(
                RestResponse.success(chuyenKhoaService.update(id, request))
        );
    }

    @GetMapping
    @ApiMessage("Danh sách chuyên khoa")
    public ResponseEntity<RestResponse<List<ChuyenKhoaResDTO>>> getAll() {

        return ResponseEntity.ok(
                RestResponse.success(chuyenKhoaService.getAll())
        );
    }

    @GetMapping("/{id}")
    @ApiMessage("Chi tiết chuyên khoa")
    public ResponseEntity<RestResponse<ChuyenKhoaResDTO>> getById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                RestResponse.success(chuyenKhoaService.getById(id))
        );
    }

    @DeleteMapping("/{id}")
    @ApiMessage("Xóa chuyên khoa")
    public ResponseEntity<RestResponse<Void>> delete(@PathVariable Long id) {

        chuyenKhoaService.delete(id);
        return ResponseEntity.ok(RestResponse.success(null));
    }
}
