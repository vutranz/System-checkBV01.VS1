package com.checkxmlbv01.websitecheckxmlbv01.domain.DTO.DichVuKyThuat;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DichVuKyThuatResponse {

    private Long id;

    private String maDvkt;

    private String tenDvkt;

    private Integer thoiGianMin;

    private Integer thoiGianMax;

    private Boolean hoatDong;

    private Long nhomDvktId;
    private String tenNhom;

    private Long chuyenKhoaId;
    private String tenChuyenKhoa;

    private Long dvktChaId;
    private String tenDvktCha;
}
