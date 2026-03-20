package com.checkxmlbv01.websitecheckxmlbv01.domain.DTO.LichLamViecTheoThu;

import com.checkxmlbv01.websitecheckxmlbv01.domain.enums.ThuTrongTuan;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LichLamViecTheoThuResponse {

    private Long id;

    private Long bacSiId;

    private String tenBacSi;

    private ThuTrongTuan thu;

    private Long caLamViecId;

    private String tenCa;
}
