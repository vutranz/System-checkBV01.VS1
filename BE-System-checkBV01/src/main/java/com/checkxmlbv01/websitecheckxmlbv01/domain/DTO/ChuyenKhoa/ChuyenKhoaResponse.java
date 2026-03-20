package com.checkxmlbv01.websitecheckxmlbv01.domain.DTO.ChuyenKhoa;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChuyenKhoaResponse {

    private Long id;
    private String maChuyenKhoa;
    private String tenChuyenKhoa;
    private Boolean hoatDong;
    private Long parentId;
}