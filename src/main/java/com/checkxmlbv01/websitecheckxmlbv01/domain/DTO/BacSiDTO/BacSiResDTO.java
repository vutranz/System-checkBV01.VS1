package com.checkxmlbv01.websitecheckxmlbv01.domain.DTO.BacSiDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class BacSiResDTO {

    private Long id;
    private Long coSoId;
    private String maBacSi;
    private String hoTen;
    private String cchn;
    private Boolean hoatDong;
}
