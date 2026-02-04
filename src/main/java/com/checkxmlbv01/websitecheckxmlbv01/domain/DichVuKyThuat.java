package com.checkxmlbv01.websitecheckxmlbv01.domain;
import jakarta.persistence.Entity;
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
@Table(name = "dich_vu_ky_thuat")
public class DichVuKyThuat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "co_so_id")
    private CoSo coSo;

    @ManyToOne
    @JoinColumn(name = "nhom_dvkt_id")
    private NhomDVKT nhomDVKT;

    @ManyToOne
    @JoinColumn(name = "dvkt_cha_id")
    private DichVuKyThuat dvktCha;

    private String maDvkt;
    private String tenDvkt;
    private Integer thoiGianMin;
    private Integer thoiGianMax;
    private Boolean hoatDong;
}
