package com.checkxmlbv01.websitecheckxmlbv01.service.DichVuKyThuat;

import java.util.List;

import com.checkxmlbv01.websitecheckxmlbv01.domain.DTO.DichVuKyThuat.DichVuKyThuatRequest;
import com.checkxmlbv01.websitecheckxmlbv01.domain.DTO.DichVuKyThuat.DichVuKyThuatResponse;
import com.checkxmlbv01.websitecheckxmlbv01.domain.DTO.DichVuKyThuat.DichVuKyThuatTreeResponse;

public interface DichVuKyThuatService {

    DichVuKyThuatResponse create(DichVuKyThuatRequest request);

    DichVuKyThuatResponse update(Long id, DichVuKyThuatRequest request);

    void delete(Long id);

    List<DichVuKyThuatResponse> getAll();

    List<DichVuKyThuatResponse> getRoot();

    List<DichVuKyThuatResponse> getChildren(Long parentId);

    DichVuKyThuatResponse getById(Long id);
    List<DichVuKyThuatTreeResponse> getTree();

    //  List<DichVuKyThuatResponse> search(Long dvktChaId,
    //                               Long nhomDvktId,
    //                               Long chuyenKhoaId) ;
}