package com.checkxmlbv01.websitecheckxmlbv01.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "bacsi_chuyenkhoa",
       uniqueConstraints = @UniqueConstraint(columnNames = {"bac_si_id", "chuyen_khoa_id"}))
public class BacSiChuyenKhoa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bac_si_id", nullable = false)
    private BacSi bacSi;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chuyen_khoa_id", nullable = false)
    private ChuyenKhoa chuyenKhoa;

    @Column(name = "la_chuyen_khoa_chinh", nullable = false)
    private Boolean laChuyenKhoaChinh = false;

    @Column(name = "gan_luc", nullable = false)
    private java.time.LocalDateTime ganLuc;

}
