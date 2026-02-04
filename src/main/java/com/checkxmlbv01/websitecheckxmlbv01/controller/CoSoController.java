package com.checkxmlbv01.websitecheckxmlbv01.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.checkxmlbv01.websitecheckxmlbv01.domain.DTO.CoSoResDTO;
import com.checkxmlbv01.websitecheckxmlbv01.service.CoSoService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1")
public class CoSoController {
    private final CoSoService coSoService;

    public CoSoController(CoSoService coSoService) {
        this.coSoService = coSoService;
    }

    @PostMapping("/co_so")
    public ResponseEntity<CoSoResDTO> create(@Valid @RequestBody CoSoResDTO dto) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(coSoService.createCoSo(dto));
    }
}
