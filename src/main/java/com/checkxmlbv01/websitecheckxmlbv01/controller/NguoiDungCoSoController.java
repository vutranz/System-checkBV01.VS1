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

import com.checkxmlbv01.websitecheckxmlbv01.domain.DTO.RestResponse;
import com.checkxmlbv01.websitecheckxmlbv01.domain.DTO.NguoiDungCoSo.AssignRequest;
import com.checkxmlbv01.websitecheckxmlbv01.domain.DTO.NguoiDungCoSo.NguoiDungCoSoResDTO;
import com.checkxmlbv01.websitecheckxmlbv01.domain.DTO.NguoiDungCoSo.UpdateNguoiDungCoSoReqDTO;
import com.checkxmlbv01.websitecheckxmlbv01.service.NguoiDungCoSoService;
import com.checkxmlbv01.websitecheckxmlbv01.ultil.annotation.ApiMessage;

@RestController
@RequestMapping("/api/v1")
public class NguoiDungCoSoController {

    private final NguoiDungCoSoService nguoiDungCoSoService;

    public NguoiDungCoSoController(NguoiDungCoSoService nguoiDungCoSoService) {
        this.nguoiDungCoSoService = nguoiDungCoSoService;
    }

    @PostMapping("/nguoi-dung-coso")
    @ApiMessage("Tạo người dùng cơ sở")
    public ResponseEntity<RestResponse<NguoiDungCoSoResDTO>> assign(
            @RequestBody AssignRequest request) {

        NguoiDungCoSoResDTO data = nguoiDungCoSoService.assign(
                request.getNguoiDungId(),
                request.getCoSoId()
        );

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(RestResponse.success(data));
    }

    @GetMapping("/nguoi-dung-coso")
    @ApiMessage("Lấy người dùng cơ sở")
    public ResponseEntity<RestResponse<List<NguoiDungCoSoResDTO>>> getAll() {

        List<NguoiDungCoSoResDTO> data = nguoiDungCoSoService.getAll();

        return ResponseEntity.ok(RestResponse.success(data));
    }

    @GetMapping("/nguoi-dung-coso/{id}")
    @ApiMessage("Lấy người dùng cơ sở theo ID")
    public ResponseEntity<RestResponse<NguoiDungCoSoResDTO>> getById(
            @PathVariable Long id) {

        NguoiDungCoSoResDTO data = nguoiDungCoSoService.getById(id);

        return ResponseEntity.ok(RestResponse.success(data));
    }

    @DeleteMapping("/nguoi-dung-coso/{id}")
    @ApiMessage("Xóa người dùng cơ sở theo ID")
    public ResponseEntity<RestResponse<Void>> delete(
            @PathVariable Long id) {

        nguoiDungCoSoService.delete(id);

        return ResponseEntity.ok(RestResponse.success(null));
    }

    @PutMapping("/nguoi-dung-coso/{id}")
    @ApiMessage("Cập nhật người dùng cơ sở")
    public ResponseEntity<RestResponse<NguoiDungCoSoResDTO>> update(
            @PathVariable Long id,
            @RequestBody UpdateNguoiDungCoSoReqDTO request) {

        NguoiDungCoSoResDTO data =
                nguoiDungCoSoService.update(id, request);

        return ResponseEntity.ok(RestResponse.success(data));
    }
}