package com.checkxmlbv01.websitecheckxmlbv01.domain;
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
@Table(name = "dichvukythuat")
public class DichVuKyThuat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ma_dvkt", nullable = false, unique = true, length = 50)
    private String maDvkt;

    @Column(name = "ten_dvkt", nullable = false)
    private String tenDvkt;

    private Integer thoiGianMin;
    private Integer thoiGianMax;

    @Column(name = "hoat_dong", nullable = false)
    private Boolean hoatDong = true;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "nhom_dvkt_id", nullable = false)
    private NhomDVKT nhomDVKT;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chuyen_khoa_id", nullable = false)
    private ChuyenKhoa chuyenKhoa;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dvkt_cha_id")
    private DichVuKyThuat dvktCha;

    @OneToMany(mappedBy = "dvktCha")
    private List<DichVuKyThuat> dvktCons;

    // getters & setters
}
