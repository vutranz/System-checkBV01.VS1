package com.checkxmlbv01.websitecheckxmlbv01.service.CaLamViec;

import java.util.List;

import com.checkxmlbv01.websitecheckxmlbv01.domain.DTO.CaLamViec.CaLamViecRequest;
import com.checkxmlbv01.websitecheckxmlbv01.domain.DTO.CaLamViec.CaLamViecResponse;

public interface CaLamViecService {

    CaLamViecResponse create(CaLamViecRequest request);

    CaLamViecResponse update(Long id, CaLamViecRequest request);

    void delete(Long id);

    List<CaLamViecResponse> getAll();

    CaLamViecResponse getById(Long id);
}