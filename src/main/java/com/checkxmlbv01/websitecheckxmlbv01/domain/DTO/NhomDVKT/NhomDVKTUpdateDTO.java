package com.checkxmlbv01.websitecheckxmlbv01.domain.DTO.NhomDVKT;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class NhomDVKTUpdateDTO {

    @NotBlank(message = "Tên nhóm không được để trống")
    private String tenNhom;
}
