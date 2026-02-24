package com.checkxmlbv01.websitecheckxmlbv01.domain.DTO.CaLamViecDTO;
import java.time.LocalTime;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CaLamViecResDTO {

    private Long id;
    private Long coSoId;
    private String tenCa;
    private LocalTime gioBatDau;
    private LocalTime gioKetThuc;

    public CaLamViecResDTO(Long id, Long coSoId,
                           String tenCa,
                           LocalTime gioBatDau,
                           LocalTime gioKetThuc) {
        this.id = id;
        this.coSoId = coSoId;
        this.tenCa = tenCa;
        this.gioBatDau = gioBatDau;
        this.gioKetThuc = gioKetThuc;
    }
}
