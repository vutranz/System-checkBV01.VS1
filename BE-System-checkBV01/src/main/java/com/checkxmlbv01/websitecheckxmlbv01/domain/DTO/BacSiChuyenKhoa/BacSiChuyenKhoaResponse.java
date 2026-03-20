package com.checkxmlbv01.websitecheckxmlbv01.domain.DTO.BacSiChuyenKhoa;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BacSiChuyenKhoaResponse {

    private Long id;

    private Long bacSiId;
    private String tenBacSi;

    private Long chuyenKhoaId;
    private String tenChuyenKhoa;

    private Boolean laChuyenKhoaChinh;
    private LocalDateTime ganLuc;
}
