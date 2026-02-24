package com.checkxmlbv01.websitecheckxmlbv01.domain.DTO.NguoiDungDTO;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NguoiDungReqDTO {

    @NotBlank(message = "Tên đăng nhập không được để trống")
    private String tenDangNhap;

    @NotBlank(message = "Mật khẩu không được để trống")
    private String matKhau;

    @NotBlank(message = "Họ tên không được để trống")
    private String hoTen;
    private Boolean hoatDong;
}

