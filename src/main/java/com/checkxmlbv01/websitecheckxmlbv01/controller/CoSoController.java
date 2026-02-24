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

import com.checkxmlbv01.websitecheckxmlbv01.domain.DTO.CoSoDTO.CoSoReqDTO;
import com.checkxmlbv01.websitecheckxmlbv01.domain.DTO.CoSoDTO.CoSoResDTO;
import com.checkxmlbv01.websitecheckxmlbv01.service.CoSoService;
import com.checkxmlbv01.websitecheckxmlbv01.ultil.annotation.ApiMessage;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1")
public class CoSoController {

    private final CoSoService coSoService;

    public CoSoController(CoSoService coSoService) {
        this.coSoService = coSoService;
    }

    @PostMapping("/co-so")
    @ApiMessage("Tạo cơ sở thành công")
    public ResponseEntity<CoSoResDTO> create(
        @Valid @RequestBody CoSoReqDTO dto) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(coSoService.create(dto));
    }

    @GetMapping("/co-so")
    @ApiMessage("Lấy danh sách cơ sở thành công")
    public ResponseEntity<List<CoSoResDTO>> getAll() {
        return ResponseEntity.ok(coSoService.getAll());
    }

    @GetMapping("/co-so/{id}")
    @ApiMessage("Lấy thông tin cơ sở thành công")
    public ResponseEntity<CoSoResDTO> getById(
            @PathVariable Long id) {
        return ResponseEntity.ok(coSoService.getById(id));
    }

    @PutMapping("/co-so/{id}")
    @ApiMessage("Cập nhật cơ sở thành công")
    public ResponseEntity<CoSoResDTO> update(
            @PathVariable Long id,
            @Valid @RequestBody CoSoReqDTO dto
    ) {
        return ResponseEntity.ok(coSoService.update(id, dto));
    }

    @DeleteMapping("/co-so/{id}")
    @ApiMessage("Xóa cơ sở thành công")
    public ResponseEntity<Void> delete(
            @PathVariable Long id
    ) {
        coSoService.delete(id);
        return ResponseEntity.noContent().build();
    }
}



