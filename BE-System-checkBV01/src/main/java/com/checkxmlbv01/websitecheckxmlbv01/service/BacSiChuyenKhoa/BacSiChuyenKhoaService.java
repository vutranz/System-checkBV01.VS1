package com.checkxmlbv01.websitecheckxmlbv01.service.BacSiChuyenKhoa;

import java.util.List;

import com.checkxmlbv01.websitecheckxmlbv01.domain.DTO.BacSiChuyenKhoa.BacSiChuyenKhoaRequest;
import com.checkxmlbv01.websitecheckxmlbv01.domain.DTO.BacSiChuyenKhoa.BacSiChuyenKhoaResponse;

public interface BacSiChuyenKhoaService {

    BacSiChuyenKhoaResponse create(BacSiChuyenKhoaRequest request);

    void delete(Long id);

    List<BacSiChuyenKhoaResponse> getByBacSi(Long bacSiId);

    List<BacSiChuyenKhoaResponse> getAll();

    BacSiChuyenKhoaResponse update(Long id, BacSiChuyenKhoaRequest request);
}