package com.checkxmlbv01.websitecheckxmlbv01.domain.DTO.ChuyenKhoaDTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ChuyenKhoaReqDTO {

    @NotNull(message = "Cơ sở không được để trống")
    private Long coSoId;

    @NotBlank(message = "Mã chuyên khoa không được để trống")
    private String maChuyenKhoa;

    @NotBlank(message = "Tên chuyên khoa không được để trống")
    private String tenChuyenKhoa;

    private Boolean hoatDong = true;
}