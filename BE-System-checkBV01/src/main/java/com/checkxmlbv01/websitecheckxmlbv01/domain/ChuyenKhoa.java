package com.checkxmlbv01.websitecheckxmlbv01.domain;
import java.util.ArrayList;
import java.util.List;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "chuyenkhoa")
public class ChuyenKhoa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ma_chuyen_khoa", nullable = false, unique = true, length = 50)
    private String maChuyenKhoa;

    @Column(name = "ten_chuyen_khoa", nullable = false)
    private String tenChuyenKhoa;

    @Column(name = "hoat_dong", nullable = false)
    private Boolean hoatDong = true;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chuyen_khoa_cha_id")
    private ChuyenKhoa chuyenKhoaCha;

    @OneToMany(mappedBy = "chuyenKhoaCha")
    private List<ChuyenKhoa> chuyenKhoaCons = new ArrayList<>();

    // getters & setters
}
