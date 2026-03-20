package com.checkxmlbv01.websitecheckxmlbv01.domain.DTO.LichLamViecTheoThu;

import com.checkxmlbv01.websitecheckxmlbv01.domain.enums.ThuTrongTuan;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LichLamViecTheoThuRequest {

    private Long bacSiId;

    private ThuTrongTuan thu;

    private Long caLamViecId;
}
