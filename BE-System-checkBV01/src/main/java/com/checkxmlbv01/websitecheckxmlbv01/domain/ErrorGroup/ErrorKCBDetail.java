package com.checkxmlbv01.websitecheckxmlbv01.domain.ErrorGroup;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErrorKCBDetail {
   private String maLk;
    private String maBn;
    private String maDichVu;

    private String maBsCd;      // 🔥 bỏ dấu
    private String maBsTh;
    private String maBsDocKq;

    private String tenDichVu;

    private String ngayYl;
    private String ngayThyl;
    private String ngayKq;      // 🔥 sửa chuẩn

    private String hoten;
    private String errorDetail;

    public ErrorKCBDetail(String maLk,
                      String maDichVu,
                      String tenDichVu,
                      String errorDetail) {
    this.maLk = maLk;
    this.maDichVu = maDichVu;
    this.tenDichVu = tenDichVu;
    this.errorDetail = errorDetail;
}

}
