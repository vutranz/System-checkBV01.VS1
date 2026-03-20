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
    private String maBsCĐ;
    private String maBsTH;
    private String tenDichVu;
    private String ngayYL;
    private String ngayTHYL;
    private String Ngaykq;
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
