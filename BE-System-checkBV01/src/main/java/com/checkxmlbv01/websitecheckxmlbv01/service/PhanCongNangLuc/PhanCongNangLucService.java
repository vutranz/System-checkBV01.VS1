package com.checkxmlbv01.websitecheckxmlbv01.service.PhanCongNangLuc;

import java.util.List;

import com.checkxmlbv01.websitecheckxmlbv01.domain.DTO.PhanCongNangLuc.PhanCongNangLucRequest;
import com.checkxmlbv01.websitecheckxmlbv01.domain.DTO.PhanCongNangLuc.PhanCongNangLucResponse;
import com.checkxmlbv01.websitecheckxmlbv01.domain.enums.ThuTrongTuan;

public interface PhanCongNangLucService {

    PhanCongNangLucResponse create(PhanCongNangLucRequest request);

    PhanCongNangLucResponse update(Long id, PhanCongNangLucRequest request);

    void delete(Long id);

    List<PhanCongNangLucResponse> getAll();

    List<PhanCongNangLucResponse> getByBacSi(Long bacSiId);

    List<PhanCongNangLucResponse> getByDvkt(Long dvktId);

    List<PhanCongNangLucResponse> getByThuAndCa(
            ThuTrongTuan thu,
            Long caLamViecId
    );
}