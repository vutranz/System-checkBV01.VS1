package com.checkxmlbv01.websitecheckxmlbv01.domain.DTO.NhomDVKT;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class NhomDVKTReqDTO {
      @NotBlank(message = "Tên nhóm không được để trống")
    private String tenNhom;

    @NotNull(message = "Cơ sở không được để trống")
    private Long coSoId;
}
