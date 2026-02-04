package com.checkxmlbv01.websitecheckxmlbv01.domain;
import com.checkxmlbv01.websitecheckxmlbv01.domain.enums.VaiTroBacSi;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
@Table(name = "nang_luc_bac_si",
       uniqueConstraints = @UniqueConstraint(
         columnNames = {"bac_si_id","ca_lam_viec_id","dvkt_id","vai_tro"}))
public class NangLucBacSi {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "bac_si_id")
    private BacSi bacSi;

    @ManyToOne
    @JoinColumn(name = "ca_lam_viec_id")
    private CaLamViec caLamViec;

    @ManyToOne
    @JoinColumn(name = "dvkt_id")
    private DichVuKyThuat dvkt;

    @Enumerated(EnumType.STRING)
    private VaiTroBacSi vaiTro;
}