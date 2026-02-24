package com.checkxmlbv01.websitecheckxmlbv01.domain.DTO.ChuyenKhoaDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ChuyenKhoaResDTO {

    private Long id;
    private Long coSoId;
    private String maChuyenKhoa;
    private String tenChuyenKhoa;
    private Boolean hoatDong;
}
