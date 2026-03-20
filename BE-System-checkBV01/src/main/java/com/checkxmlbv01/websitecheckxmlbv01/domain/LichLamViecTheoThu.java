package com.checkxmlbv01.websitecheckxmlbv01.domain;
import com.checkxmlbv01.websitecheckxmlbv01.domain.enums.ThuTrongTuan;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
@Table(name = "lichlamviectheothu",
       uniqueConstraints = @UniqueConstraint(columnNames = {"bac_si_id", "thu", "ca_lam_viec_id"}))
public class LichLamViecTheoThu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "thu", nullable = false)
    private ThuTrongTuan thu;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bac_si_id", nullable = false)
    private BacSi bacSi;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ca_lam_viec_id", nullable = false)
    private CaLamViec caLamViec;

    // getters & setters
}
