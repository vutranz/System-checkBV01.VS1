package com.checkxmlbv01.websitecheckxmlbv01.domain.DTO.CaLamViec;

import java.time.LocalTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CaLamViecRequest {

    private String tenCa;

    private LocalTime gioBatDau;

    private LocalTime gioKetThuc;

    private Boolean hoatDong;
}
