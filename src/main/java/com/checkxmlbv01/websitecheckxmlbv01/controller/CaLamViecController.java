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

import com.checkxmlbv01.websitecheckxmlbv01.domain.DTO.CaLamViecDTO.CaLamViecReqDTO;
import com.checkxmlbv01.websitecheckxmlbv01.domain.DTO.CaLamViecDTO.CaLamViecResDTO;
import com.checkxmlbv01.websitecheckxmlbv01.domain.DTO.CaLamViecDTO.CaLamViecUpdateDTO;
import com.checkxmlbv01.websitecheckxmlbv01.service.CaLamViecService;

@RestController
@RequestMapping("/api/v1/ca-lam-viec")
public class CaLamViecController {

    private final CaLamViecService service;

    public CaLamViecController(CaLamViecService service) {
        this.service = service;
    }

    @PostMapping
    public CaLamViecResDTO create(@RequestBody CaLamViecReqDTO request) {
        return service.create(request);
    }

    @GetMapping("/{id}")
    public CaLamViecResDTO getById(@PathVariable Long id) {
        return service.getById(id);
    }

    @GetMapping
    public List<CaLamViecResDTO> getByCoSo(@RequestParam Long coSoId) {
        return service.getByCoSo(coSoId);
    }

    @PutMapping("/{id}")
    public CaLamViecResDTO update(@PathVariable Long id,
                                  @RequestBody CaLamViecUpdateDTO request) {
        return service.update(id, request);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
