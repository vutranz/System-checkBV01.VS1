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
import com.checkxmlbv01.websitecheckxmlbv01.ultil.annotation.ApiMessage;

import jakarta.validation.Valid;

import com.checkxmlbv01.websitecheckxmlbv01.domain.DTO.RestResponse;
import com.checkxmlbv01.websitecheckxmlbv01.domain.DTO.BacSiDTO.BacSiReqDTO;
import com.checkxmlbv01.websitecheckxmlbv01.domain.DTO.BacSiDTO.BacSiResDTO;
import com.checkxmlbv01.websitecheckxmlbv01.domain.DTO.BacSiDTO.BacSiUpdateDTO;
import com.checkxmlbv01.websitecheckxmlbv01.service.BacSiService;

@RestController
@RequestMapping("/api/v1/bac-si")
public class BacSiController {

    private final BacSiService bacSiService;

    public BacSiController(BacSiService bacSiService){
        this.bacSiService=bacSiService;
    }

    @PostMapping
    @ApiMessage("Tạo bác sĩ")
    public ResponseEntity<RestResponse<BacSiResDTO>> create(
            @Valid @RequestBody BacSiReqDTO request) {

        return ResponseEntity.ok(
                RestResponse.success(bacSiService.create(request))
        );
    }

    @PutMapping("/{id}")
    @ApiMessage("Cập nhật bác sĩ")
    public ResponseEntity<RestResponse<BacSiResDTO>> update(
            @PathVariable Long id,
            @RequestBody BacSiUpdateDTO request) {

        return ResponseEntity.ok(
                RestResponse.success(bacSiService.update(id, request))
        );
    }

    @GetMapping
    @ApiMessage("Danh sách bác sĩ")
    public ResponseEntity<RestResponse<List<BacSiResDTO>>> getAll() {

        return ResponseEntity.ok(
                RestResponse.success(bacSiService.getAll())
        );
    }

    @GetMapping("/{id}")
    @ApiMessage("Chi tiết bác sĩ")
    public ResponseEntity<RestResponse<BacSiResDTO>> getById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                RestResponse.success(bacSiService.getById(id))
        );
    }

    @DeleteMapping("/{id}")
    @ApiMessage("Xóa bác sĩ")
    public ResponseEntity<RestResponse<Void>> delete(@PathVariable Long id) {

        bacSiService.delete(id);
        return ResponseEntity.ok(RestResponse.success(null));
    }
}
