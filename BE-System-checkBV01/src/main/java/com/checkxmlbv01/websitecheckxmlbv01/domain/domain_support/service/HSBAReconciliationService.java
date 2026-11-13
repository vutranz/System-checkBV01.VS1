package com.checkxmlbv01.websitecheckxmlbv01.domain.domain_support.service;

import com.checkxmlbv01.websitecheckxmlbv01.domain.domain_support.listHSBA_gateBHYT;
import com.checkxmlbv01.websitecheckxmlbv01.domain.domain_support.listHSBA_winSoft;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class HSBAReconciliationService {

    // Class tĩnh chứa cấu trúc kết quả trả về khi đối soát
    public static class ComparisonResult {
        private String maBn;
        private String tenBenhNhan;
        private BigDecimal thucThuWinSoft;
        private BigDecimal tongChiGateBHYT;
        private String trangThai; // "KHỚP", "LỆCH TIỀN", "CHỈ CÓ Ở WINSOFT", "CHỈ CÓ Ở GATE BHYT"

        public ComparisonResult(String maBn, String tenBenhNhan, BigDecimal thucThuWinSoft, BigDecimal tongChiGateBHYT, String trangThai) {
            this.maBn = maBn;
            this.tenBenhNhan = tenBenhNhan;
            this.thucThuWinSoft = thucThuWinSoft;
            this.tongChiGateBHYT = tongChiGateBHYT;
            this.trangThai = trangThai;
        }

        // Getters
        public String getMaBn() { return maBn; }
        public String getTenBenhNhan() { return tenBenhNhan; }
        public BigDecimal getThucThuWinSoft() { return thucThuWinSoft; }
        public BigDecimal getTongChiGateBHYT() { return tongChiGateBHYT; }
        public String getTrangThai() { return trangThai; }
    }

    // Logic so sánh đối chiếu giữa 2 danh sách
    public List<ComparisonResult> compareData(List<listHSBA_winSoft> winSoftList, List<listHSBA_gateBHYT> gateBHYTList) {
        List<ComparisonResult> results = new ArrayList<>();

        // Chuyển danh sách Gate BHYT thành Map tra cứu nhanh theo Mã BN
        Map<String, listHSBA_gateBHYT> gateMap = gateBHYTList.stream()
                .collect(Collectors.toMap(listHSBA_gateBHYT::getMaBn, item -> item, (k1, k2) -> k1));

        // Duyệt qua danh sách WinSoft để đối chiếu
        for (listHSBA_winSoft win : winSoftList) {
            String maBn = win.getMaBn();
            listHSBA_gateBHYT gate = gateMap.get(maBn);

            if (gate != null) {
                BigDecimal thucThu = win.getThucThu() != null ? win.getThucThu() : BigDecimal.ZERO;
                BigDecimal tongChi = gate.getTongChi() != null ? gate.getTongChi() : BigDecimal.ZERO;

                // So sánh số tiền thực thu và tổng chi
                if (thucThu.compareTo(tongChi) == 0) {
                    results.add(new ComparisonResult(maBn, win.getTenBenhNhan(), thucThu, tongChi, "KHỚP"));
                } else {
                    results.add(new ComparisonResult(maBn, win.getTenBenhNhan(), thucThu, tongChi, "LỆCH TIỀN"));
                }
                
                gateMap.remove(maBn); // Xóa để đánh dấu đã xử lý
            } else {
                results.add(new ComparisonResult(maBn, win.getTenBenhNhan(), win.getThucThu(), null, "CHỈ CÓ Ở WINSOFT"));
            }
        }

        // Các bản ghi còn lại trong Map chỉ có bên Gate BHYT
        for (Map.Entry<String, listHSBA_gateBHYT> entry : gateMap.entrySet()) {
            listHSBA_gateBHYT gate = entry.getValue();
            results.add(new ComparisonResult(gate.getMaBn(), gate.getHoTen(), null, gate.getTongChi(), "CHỈ CÓ Ở GATE BHYT"));
        }

        return results;
    }
}