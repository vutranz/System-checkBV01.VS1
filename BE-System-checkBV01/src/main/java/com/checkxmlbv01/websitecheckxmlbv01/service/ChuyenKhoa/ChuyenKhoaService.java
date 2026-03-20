package com.checkxmlbv01.websitecheckxmlbv01.service.ChuyenKhoa;
import java.util.List;


import com.checkxmlbv01.websitecheckxmlbv01.domain.DTO.ChuyenKhoa.ChuyenKhoaRequest;
import com.checkxmlbv01.websitecheckxmlbv01.domain.DTO.ChuyenKhoa.ChuyenKhoaResponse;


public interface ChuyenKhoaService {

    // Tạo mới
    ChuyenKhoaResponse create(ChuyenKhoaRequest request);

    // Cập nhật
    ChuyenKhoaResponse update(Long id, ChuyenKhoaRequest request);

    // Lấy tất cả
    List<ChuyenKhoaResponse> getAll();

    // Lấy theo id
    ChuyenKhoaResponse getById(Long id);

    // Xóa
    void delete(Long id);
}
