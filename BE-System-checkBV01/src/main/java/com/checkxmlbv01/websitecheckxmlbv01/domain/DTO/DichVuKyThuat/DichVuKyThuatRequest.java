package com.checkxmlbv01.websitecheckxmlbv01.domain.DTO.DichVuKyThuat;

import lombok.Data;

@Data
public class DichVuKyThuatRequest {

    private String maDvkt;

    private String tenDvkt;

    private Integer thoiGianMin;

    private Integer thoiGianMax;

    private Boolean hoatDong;

    private Long nhomDvktId;

    private Long chuyenKhoaId;

    private Long dvktChaId; // có thể null
}