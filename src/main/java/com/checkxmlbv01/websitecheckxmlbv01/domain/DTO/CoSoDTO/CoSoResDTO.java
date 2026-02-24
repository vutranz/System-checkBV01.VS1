package com.checkxmlbv01.websitecheckxmlbv01.domain.DTO.CoSoDTO;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CoSoResDTO {
    private Long id;

    @Column(unique = true)
    private String maCoSo;

    private String tenCoSo;
    private LocalDateTime taoLuc;
}
