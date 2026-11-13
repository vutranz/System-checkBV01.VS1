package com.checkxmlbv01.websitecheckxmlbv01.domain.domain_support;
import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data 
@NoArgsConstructor 
@AllArgsConstructor  
public class listHSBA_gateBHYT {
    private Integer stt;                // STT
    private String maLienKet;           // Mã liên kết
    private String maThe;               // Mã thẻ
    private String maBn;                // Mã BN
    private String hoTen;               // Họ tên
    private String ngaySinh;            // Ngày sinh
    private String gioiTinh;            // Giới tính
    private String ngayVao;             // Ngày vào
    private String ngayRa;              // Ngày ra
    private String chanDoan;            // Chẩn đoán
    private BigDecimal tongChi;         // Tổng chi
    private BigDecimal benhNhanTt;      // Bệnh nhân TT
    private BigDecimal benhNhanCct;     // Bệnh nhân CCT
    private BigDecimal baoHiemTt;       // Bảo hiểm TT
    private String ngayTt;              // Ngày TT
    private String ngayGuiHs;           // Ngày gửi HS
    private String ngayDeNghiTt;        // Ngày đề nghị TT
    private String trangThaiHs;         // Trạng thái HS
    private String trangThaiTt;         // Trạng thái TT
    private String loaiHs;              // Loại HS
    private String maLoi;               // Mã lỗi
    private String mieuTa;              // Miêu tả
}