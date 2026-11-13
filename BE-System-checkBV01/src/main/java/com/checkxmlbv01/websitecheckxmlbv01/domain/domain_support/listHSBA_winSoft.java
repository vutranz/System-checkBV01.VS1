package com.checkxmlbv01.websitecheckxmlbv01.domain.domain_support;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data 
@NoArgsConstructor 
@AllArgsConstructor 
public class listHSBA_winSoft {
    private String maBn;                // Mã BN
    private String tenBenhNhan;         // Tên bệnh nhân
    private String ngayVao;             // Ngày vào
    private String ngayKb;              // Ngày KB
    private String ngayRa;              // Ngày ra
    private String soThe;               // Sổ thẻ
    private BigDecimal thanhTien;       // Thành tiền
    private BigDecimal bhytTra;         // BHYT Trả
    private BigDecimal thuPhi;          // Thu phí
    private BigDecimal phuThu;          // Phụ thu
    private BigDecimal mienGiam;        // Miễn giảm
    private BigDecimal thucThu;         // Thực thu
    private String bacSy;               // Bác Sỹ
    private String cdBanDau;            // CĐ ban đầu
    private String maTenIcd10;          // Ma_Ten_ICD10
    private String maTenIcd10Kt;        // Ma_Ten_ICD10_KT
    private String userCapNhat;         // User cập nhật
    private String ngayLamHs;           // Ngày làm HS
    private String ngayCapNhatHsKb;     // Ngày cập nhật HS KB
    private String khoa;                // Khoa
    private String reportId;            // Report_ID
    private String maKpXml;             // MA_KP_XML
    private String maLk;                // MA_LK
    private String upd;                 // Upd
    private String maLkt;               // MaLKT
    private String trang;               // Trạng
}
