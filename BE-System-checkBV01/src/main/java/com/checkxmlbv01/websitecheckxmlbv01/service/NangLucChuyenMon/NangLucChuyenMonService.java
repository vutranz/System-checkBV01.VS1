package com.checkxmlbv01.websitecheckxmlbv01.service.NangLucChuyenMon;

import java.util.List;

import com.checkxmlbv01.websitecheckxmlbv01.domain.DTO.NangLucChuyenMon.NangLucChuyenMonRequest;
import com.checkxmlbv01.websitecheckxmlbv01.domain.DTO.NangLucChuyenMon.NangLucChuyenMonResponse;

public interface NangLucChuyenMonService {

    NangLucChuyenMonResponse create(NangLucChuyenMonRequest request);

    NangLucChuyenMonResponse update(Long id, NangLucChuyenMonRequest request);

    void delete(Long id);

    List<NangLucChuyenMonResponse> getByBacSi(Long bacSiId);

    List<NangLucChuyenMonResponse> getByDvkt(Long dvktId);

    List<NangLucChuyenMonResponse> getAll();
}
