package com.checkxmlbv01.websitecheckxmlbv01.domain.DTO.DichVuKyThuat;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class DichVuKyThuatTreeResponse {

    private Long id;
    private String maDvkt;
    private String tenDvkt;
    private Boolean hoatDong;

    private Long dvktChaId;

    private List<DichVuKyThuatTreeResponse> children = new ArrayList<>();
}
