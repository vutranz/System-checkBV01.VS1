package com.checkxmlbv01.websitecheckxmlbv01.domain.DTO.NangLucChuyenMon;

import com.checkxmlbv01.websitecheckxmlbv01.domain.enums.VaiTroBacSi;

import lombok.Data;

@Data
public class NangLucChuyenMonRequest {

    private Long bacSiId;

    private Long dvktId;

    private VaiTroBacSi vaiTro;
}
