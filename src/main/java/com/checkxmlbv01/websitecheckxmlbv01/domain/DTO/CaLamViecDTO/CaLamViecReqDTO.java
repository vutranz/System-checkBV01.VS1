package com.checkxmlbv01.websitecheckxmlbv01.domain.DTO.CaLamViecDTO;

import java.time.LocalTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CaLamViecReqDTO {

    private Long coSoId;
    private String tenCa;
    private LocalTime gioBatDau;
    private LocalTime gioKetThuc;
}