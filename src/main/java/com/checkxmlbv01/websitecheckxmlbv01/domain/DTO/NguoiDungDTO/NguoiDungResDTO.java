package com.checkxmlbv01.websitecheckxmlbv01.domain.DTO.NguoiDungDTO;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NguoiDungResDTO {

    private Long id;
    private String tenDangNhap;
    private String hoTen;
    private Boolean hoatDong;
    private LocalDateTime taoLuc;
    private LocalDateTime capNhatLuc;
}

