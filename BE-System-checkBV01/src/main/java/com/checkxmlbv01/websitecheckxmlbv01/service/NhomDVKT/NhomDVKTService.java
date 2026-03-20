package com.checkxmlbv01.websitecheckxmlbv01.service.NhomDVKT;

import java.util.List;

import com.checkxmlbv01.websitecheckxmlbv01.domain.DTO.NhomDVKT.NhomDVKTRequest;
import com.checkxmlbv01.websitecheckxmlbv01.domain.DTO.NhomDVKT.NhomDVKTResponse;

public interface NhomDVKTService {

    NhomDVKTResponse create(NhomDVKTRequest request);

    NhomDVKTResponse update(Long id, NhomDVKTRequest request);

    void delete(Long id);

    List<NhomDVKTResponse> getAll();

    NhomDVKTResponse getById(Long id);
}
