package com.checkxmlbv01.websitecheckxmlbv01.domain.DTO.NangLucChuyenMon;

import com.checkxmlbv01.websitecheckxmlbv01.domain.enums.VaiTroBacSi;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class NangLucChuyenMonResponse {

    private Long id;

    private Long bacSiId;
    private String tenBacSi;

    private Long dvktId;
    private String tenDvkt;

    private VaiTroBacSi vaiTro;
}
