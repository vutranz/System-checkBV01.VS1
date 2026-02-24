package com.checkxmlbv01.websitecheckxmlbv01.domain.DTO.CoSoDTO;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CoSoReqDTO {
    @NotBlank(message = "Mã cơ sở không được để trống")
    private String maCoSo;

    @NotBlank(message = "Tên cơ sở không được để trống")
    private String tenCoSo;
}
