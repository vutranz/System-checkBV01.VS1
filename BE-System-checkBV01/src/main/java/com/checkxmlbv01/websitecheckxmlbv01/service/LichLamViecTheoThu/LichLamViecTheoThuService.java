package com.checkxmlbv01.websitecheckxmlbv01.service.LichLamViecTheoThu;

import java.util.List;

import com.checkxmlbv01.websitecheckxmlbv01.domain.DTO.LichLamViecTheoThu.LichLamViecTheoThuRequest;
import com.checkxmlbv01.websitecheckxmlbv01.domain.DTO.LichLamViecTheoThu.LichLamViecTheoThuResponse;

public interface LichLamViecTheoThuService {

    LichLamViecTheoThuResponse create(LichLamViecTheoThuRequest request);

    LichLamViecTheoThuResponse update(Long id, LichLamViecTheoThuRequest request);

    void delete(Long id);

    List<LichLamViecTheoThuResponse> getByBacSi(Long bacSiId);

    List<LichLamViecTheoThuResponse> getAll();
}
