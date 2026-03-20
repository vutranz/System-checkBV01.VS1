package com.checkxmlbv01.websitecheckxmlbv01.domain;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "bacsi")
public class BacSi {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ma_bac_si", nullable = false, unique = true, length = 50)
    private String maBacSi;

    @Column(name = "ho_ten", nullable = false)
    private String hoTen;

    @Column(name = "cchn", length = 100)
    private String cchn;

    @Column(name = "hoat_dong", nullable = false)
    private Boolean hoatDong = true;

    @OneToMany(mappedBy = "bacSi", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<BacSiChuyenKhoa> chuyenKhoas;

    @JsonIgnore
    @OneToMany(mappedBy = "bacSi", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LichLamViecTheoThu> lichLamViecs;

    @JsonIgnore
    @OneToMany(mappedBy = "bacSi", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<NangLucChuyenMon> nangLucs;

    @JsonIgnore
    @OneToMany(mappedBy = "bacSi", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PhanCongNangLuc> phanCongNangLucs;

    // getters & setters
}