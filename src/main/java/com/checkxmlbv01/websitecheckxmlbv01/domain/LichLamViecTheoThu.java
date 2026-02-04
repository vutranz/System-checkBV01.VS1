package com.checkxmlbv01.websitecheckxmlbv01.domain;
import com.checkxmlbv01.websitecheckxmlbv01.domain.enums.ThuTrongTuan;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "lich_lam_viec_theo_thu")
public class LichLamViecTheoThu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "bac_si_id")
    private BacSi bacSi;

    @Enumerated(EnumType.STRING)
    private ThuTrongTuan thu;

    @ManyToOne
    @JoinColumn(name = "ca_lam_viec_id")
    private CaLamViec caLamViec;
}
