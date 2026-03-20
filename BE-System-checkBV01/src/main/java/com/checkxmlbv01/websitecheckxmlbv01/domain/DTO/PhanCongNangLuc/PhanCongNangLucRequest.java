package com.checkxmlbv01.websitecheckxmlbv01.domain.DTO.PhanCongNangLuc;

import com.checkxmlbv01.websitecheckxmlbv01.domain.enums.ThuTrongTuan;
import com.checkxmlbv01.websitecheckxmlbv01.domain.enums.VaiTroBacSi;

import lombok.Data;

@Data
public class PhanCongNangLucRequest {

    private Long bacSiId;

    private ThuTrongTuan thu;

    private Long caLamViecId;

    private Long dvktId;

    private VaiTroBacSi vaiTro;
}
