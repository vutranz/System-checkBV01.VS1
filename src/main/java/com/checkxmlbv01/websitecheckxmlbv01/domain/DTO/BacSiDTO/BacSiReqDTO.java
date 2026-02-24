package com.checkxmlbv01.websitecheckxmlbv01.domain.DTO.BacSiDTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class BacSiReqDTO {

    @NotNull
    private Long coSoId;

    @NotBlank
    private String maBacSi;

    @NotBlank
    private String hoTen;

    private String cchn;

    private Boolean hoatDong = true;
}