package com.checkxmlbv01.websitecheckxmlbv01.domain.DTO.ChuyenKhoa;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChuyenKhoaRequest {

    private String maChuyenKhoa;
    private String tenChuyenKhoa;
    private Boolean hoatDong;
    private Long parentId;   // chỉ truyền id cha
}
