package com.checkxmlbv01.websitecheckxmlbv01.domain.DTO.PhanCongNangLuc;

import com.checkxmlbv01.websitecheckxmlbv01.domain.enums.ThuTrongTuan;
import com.checkxmlbv01.websitecheckxmlbv01.domain.enums.VaiTroBacSi;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PhanCongNangLucResponse {

    private Long id;

    private Long bacSiId;
    private String tenBacSi;

    private ThuTrongTuan thu;

    private Long caLamViecId;
    private String tenCa;

    private Long dvktId;
    private String tenDvkt;

    private VaiTroBacSi vaiTro;
}
