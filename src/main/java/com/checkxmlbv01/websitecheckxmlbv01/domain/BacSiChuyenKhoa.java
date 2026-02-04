package com.checkxmlbv01.websitecheckxmlbv01.domain;
import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "bac_si_chuyen_khoa")
public class BacSiChuyenKhoa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private BacSi bacSi;

    @ManyToOne
    private ChuyenKhoa chuyenKhoa;

    private Boolean laChuyenKhoaChinh;
    private LocalDateTime ganLuc;
}
