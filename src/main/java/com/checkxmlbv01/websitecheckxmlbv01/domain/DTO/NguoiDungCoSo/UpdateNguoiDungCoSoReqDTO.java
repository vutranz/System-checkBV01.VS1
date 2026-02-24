package com.checkxmlbv01.websitecheckxmlbv01.domain.DTO.NguoiDungCoSo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateNguoiDungCoSoReqDTO {

    private Long nguoiDungId;
    private Long coSoId;
}
