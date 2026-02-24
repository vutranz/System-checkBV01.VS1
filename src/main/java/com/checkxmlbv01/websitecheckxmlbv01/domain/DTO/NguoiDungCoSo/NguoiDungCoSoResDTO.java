package com.checkxmlbv01.websitecheckxmlbv01.domain.DTO.NguoiDungCoSo;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class NguoiDungCoSoResDTO {

    private Long id;
    private Long nguoiDungId;
    private String tenDangNhap;
    private Long coSoId;
    private String tenCoSo;
    private LocalDateTime ganLuc;
}
