package com.checkxmlbv01.websitecheckxmlbv01.domain.DTO.BacSiChuyenKhoa;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BacSiChuyenKhoaRequest {

    private Long bacSiId;
    private Long chuyenKhoaId;
    private Boolean laChuyenKhoaChinh;
}
