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

import com.checkxmlbv01.websitecheckxmlbv01.domain.DTO.NguoiDungDTO.NguoiDungReqDTO;
import com.checkxmlbv01.websitecheckxmlbv01.domain.DTO.NguoiDungDTO.NguoiDungResDTO;
import com.checkxmlbv01.websitecheckxmlbv01.service.NguoiDungService;
import com.checkxmlbv01.websitecheckxmlbv01.ultil.annotation.ApiMessage;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1")
public class NguoiDungController {

    private final NguoiDungService service;

    public NguoiDungController(NguoiDungService service) {
        this.service = service;
    }

    @PostMapping("/nguoi-dung")
    @ApiMessage("Tạo người dùng thành công")
    public ResponseEntity<NguoiDungResDTO> create(
            @Valid @RequestBody NguoiDungReqDTO dto
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(service.create(dto));
    }

    @GetMapping("/nguoi-dung")
    public ResponseEntity<List<NguoiDungResDTO>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/nguoi-dung/{id}")
    public ResponseEntity<NguoiDungResDTO> getById(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(service.getById(id));
    }

    @PutMapping("/nguoi-dung/{id}")
    @ApiMessage("Cập nhật người dùng thành công")
    public ResponseEntity<NguoiDungResDTO> update(
            @PathVariable Long id,
            @Valid @RequestBody NguoiDungReqDTO dto
    ) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    @DeleteMapping("/nguoi-dung/{id}")
    @ApiMessage("Xóa người dùng thành công")
    public ResponseEntity<Void> delete(
            @PathVariable Long id
    ) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}

