package com.checkxmlbv01.websitecheckxmlbv01.domain;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "calamviec")
public class CaLamViec {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ten_ca", nullable = false)
    private String tenCa;

    @Column(name = "gio_bat_dau", nullable = false)
    private java.time.LocalTime gioBatDau;

    @Column(name = "gio_ket_thuc", nullable = false)
    private java.time.LocalTime gioKetThuc;

    @Column(name = "hoat_dong", nullable = false)
    private Boolean hoatDong = true;

    // getters & setters
}
