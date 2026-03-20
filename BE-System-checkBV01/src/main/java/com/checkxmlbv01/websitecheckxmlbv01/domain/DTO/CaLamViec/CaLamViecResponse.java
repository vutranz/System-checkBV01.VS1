package com.checkxmlbv01.websitecheckxmlbv01.domain.DTO.CaLamViec;

import java.time.LocalTime;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CaLamViecResponse {

    private Long id;

    private String tenCa;

    private LocalTime gioBatDau;

    private LocalTime gioKetThuc;

    private Boolean hoatDong;
}