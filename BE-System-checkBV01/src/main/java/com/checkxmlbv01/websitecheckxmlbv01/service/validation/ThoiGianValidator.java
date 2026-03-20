package com.checkxmlbv01.websitecheckxmlbv01.service.validation;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.checkxmlbv01.websitecheckxmlbv01.domain.BacSi;
import com.checkxmlbv01.websitecheckxmlbv01.domain.CaLamViec;
import com.checkxmlbv01.websitecheckxmlbv01.domain.DichVuKyThuat;
import com.checkxmlbv01.websitecheckxmlbv01.domain.ErrorGroup.ErrorKCBDetail;
import com.checkxmlbv01.websitecheckxmlbv01.domain.ErrorGroup.ErrorKCBGroup;
import com.checkxmlbv01.websitecheckxmlbv01.domain.enums.ThuTrongTuan;
import com.checkxmlbv01.websitecheckxmlbv01.domain.enums.VaiTroBacSi;
import com.checkxmlbv01.websitecheckxmlbv01.domain.xml.xml1;
import com.checkxmlbv01.websitecheckxmlbv01.domain.xml.xml2;
import com.checkxmlbv01.websitecheckxmlbv01.domain.xml.xml3;
import com.checkxmlbv01.websitecheckxmlbv01.domain.xml.xml4;
import com.checkxmlbv01.websitecheckxmlbv01.repository.BacSiRepository;
import com.checkxmlbv01.websitecheckxmlbv01.repository.CaLamViecRepository;
import com.checkxmlbv01.websitecheckxmlbv01.repository.PhanCongNangLucRepository;

@Component
public class ThoiGianValidator {

    private static final DateTimeFormatter FMT =
            DateTimeFormatter.ofPattern("yyyyMMddHHmm");

    private static final String NHOM_CONG_KHAM = "Công Khám";

    private final CaLamViecRepository caRepo;
    private final PhanCongNangLucRepository phanCongRepo;
    private final BacSiRepository bacSiRepo;

    public ThoiGianValidator(
            CaLamViecRepository caRepo,
            PhanCongNangLucRepository phanCongRepo,
            BacSiRepository bacSiRepo) {

        this.caRepo = caRepo;
        this.phanCongRepo = phanCongRepo;
        this.bacSiRepo = bacSiRepo;
    }

    /* ========================= UTIL ========================= */

    private LocalDateTime parse(String time) {
        if (time == null) return null;
        try {
            return LocalDateTime.parse(time, FMT);
        } catch (Exception e) {
            return null;
        }
    }

    private String normalizeCCHN(String cchn) {
        return cchn == null ? null : cchn.trim();
    }

    private void addError(ErrorKCBGroup group,
                          String maLK,
                          xml3 dv,
                          String msg) {

        ErrorKCBDetail err = new ErrorKCBDetail();
        err.setMaLk(maLK);

        if (dv != null) {
            err.setMaDichVu(dv.getMaDichVu());
            err.setTenDichVu(dv.getTenDichVu());
            err.setNgayYL(dv.getNgayYl());
            err.setNgayTHYL(dv.getNgayThYl());
            err.setNgaykq(dv.getNgayKq());
        }

        err.setErrorDetail(msg);
        group.addError(err);
    }

    private boolean laCongKham(DichVuKyThuat dvkt) {
        if (dvkt == null || dvkt.getNhomDVKT() == null)
            return false;

        String ten = dvkt.getNhomDVKT().getTenNhom();

        return ten != null &&
                ten.toLowerCase().contains(NHOM_CONG_KHAM);
    }

    private CaLamViec findCa(LocalTime time,
                             List<CaLamViec> caList) {

        for (CaLamViec ca : caList) {
            if (!time.isBefore(ca.getGioBatDau())
                    && !time.isAfter(ca.getGioKetThuc())) {
                return ca;
            }
        }
        return null;
    }

    private ThuTrongTuan getThu(LocalDateTime time) {
        return switch (time.getDayOfWeek()) {
            case MONDAY -> ThuTrongTuan.T2;
            case TUESDAY -> ThuTrongTuan.T3;
            case WEDNESDAY -> ThuTrongTuan.T4;
            case THURSDAY -> ThuTrongTuan.T5;
            case FRIDAY -> ThuTrongTuan.T6;
            case SATURDAY -> ThuTrongTuan.T7;
            default -> ThuTrongTuan.CN;
        };
    }

    /* ========================= CHECK TIME DVKT ========================= */

    private void checkThoiGianDVKT(
            xml3 dv,
            DichVuKyThuat dvkt,
            String maLK,
            ErrorKCBGroup group,
            LocalDateTime congKhamEndTime
    ) {
        try {

            boolean congKham = laCongKham(dvkt);

            LocalDateTime yl   = parse(dv.getNgayYl());
            LocalDateTime thyl = parse(dv.getNgayThYl());
            LocalDateTime kq   = parse(dv.getNgayKq());

            LocalDateTime start = congKham ? yl : thyl;
            LocalDateTime end   = kq;

            if (!congKham && yl != null && thyl != null) {
                if (!yl.isBefore(thyl)) {
                    addError(group, maLK, dv, "YL phải < THYL");
                }
            }

            if (!congKham && congKhamEndTime != null && yl != null) {
                if (!yl.isAfter(congKhamEndTime)) {
                    addError(group, maLK, dv,
                            "YL phải > KQ công khám (" +
                                    congKhamEndTime.format(FMT) + ")");
                }
            }

            if (start != null && end != null
                    && dvkt.getThoiGianMin() != null
                    && dvkt.getThoiGianMax() != null) {

                long diff = Duration.between(start, end).toMinutes();

                if (diff < dvkt.getThoiGianMin()
                        || diff > dvkt.getThoiGianMax()) {

                    addError(group, maLK, dv,
                            "Thời gian DV lệch " + diff + "p (chuẩn: "
                                    + dvkt.getThoiGianMin() + "-"
                                    + dvkt.getThoiGianMax() + ")");
                }
            }

        } catch (Exception e) {
        }
    }

    /* ========================= RULE THUỐC (THEO NGÀY) ========================= */

    private void checkThuocTheoNgay(
            String maLK,
            List<xml3> dsDv,
            List<xml2> dsThuoc,
            ErrorKCBGroup group
    ) {

        if (dsThuoc == null || dsThuoc.isEmpty()) return;

        Map<LocalDate, List<xml3>> dvByDate =
                dsDv.stream()
                        .filter(dv -> parse(dv.getNgayKq()) != null)
                        .collect(Collectors.groupingBy(
                                dv -> parse(dv.getNgayKq()).toLocalDate()
                        ));

        Map<LocalDate, List<xml2>> thuocByDate =
                dsThuoc.stream()
                        .filter(t -> parse(t.getNgayYl()) != null)
                        .collect(Collectors.groupingBy(
                                t -> parse(t.getNgayYl()).toLocalDate()
                        ));

        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

        for (LocalDate date : thuocByDate.keySet()) {

            List<xml2> thuocTrongNgay = thuocByDate.get(date);
            List<xml3> dvTrongNgay = dvByDate.get(date);

            if (dvTrongNgay == null || dvTrongNgay.isEmpty()) {
                continue; // ngoại trú hợp lệ
            }

            LocalDateTime maxKQ = dvTrongNgay.stream()
                    .map(dv -> parse(dv.getNgayKq()))
                    .filter(Objects::nonNull)
                    .max(LocalDateTime::compareTo)
                    .orElse(null);

            if (maxKQ == null) continue;

            for (xml2 thuoc : thuocTrongNgay) {

                LocalDateTime t = parse(thuoc.getNgayYl());

                if (t == null) continue;

                if (!t.isAfter(maxKQ)) {
                    addError(group, maLK, null,
                            "Ngày " + date +
                                    " - Thuốc lúc " + t.format(fmt) +
                                    " phải sau CLS cuối (" +
                                    maxKQ.format(fmt) + ")");
                }
            }
        }
    }

    /* ========================= MAIN ========================= */

    public void validate(xml1 hs,
                         List<xml3> dsDv,
                         List<xml2> dsThuoc, // 👈 NEW
                         Map<String, DichVuKyThuat> dvktMap,
                         Map<String, xml4> xml4Map,
                         ErrorKCBGroup group) {

        if (hs == null || dsDv == null || dsDv.isEmpty())
            return;

        List<CaLamViec> caList = caRepo.findByHoatDong(true);

        Map<String, BacSi> bacSiMap =
                bacSiRepo.findAll()
                        .stream()
                        .collect(Collectors.toMap(
                                bs -> normalizeCCHN(bs.getCchn()),
                                bs -> bs,
                                (a, b) -> a
                        ));

        /* ===== RULE THUỐC ===== */
        checkThuocTheoNgay(
                hs.getMaLk(),
                dsDv,
                dsThuoc,
                group
        );

        /* ===== lấy KQ công khám ===== */
        LocalDateTime congKhamEndTime = dsDv.stream()
                .filter(dv -> laCongKham(dvktMap.get(dv.getMaDichVu())))
                .map(dv -> parse(dv.getNgayKq()))
                .filter(Objects::nonNull)
                .max(LocalDateTime::compareTo)
                .orElse(null);

        for (xml3 dv : dsDv) {

            DichVuKyThuat dvkt = dvktMap.get(dv.getMaDichVu());

            if (dvkt == null) {
                addError(group, hs.getMaLk(), dv,
                        "Không tồn tại DVKT");
                continue;
            }

            checkThoiGianDVKT(
                    dv, dvkt, hs.getMaLk(),
                    group, congKhamEndTime
            );

            boolean congKham = laCongKham(dvkt);

            LocalDateTime start = congKham
                    ? parse(dv.getNgayYl())
                    : parse(dv.getNgayThYl());

            LocalDateTime kqTime = parse(dv.getNgayKq());

            String cchnTH = dv.getNguoiThucHien();
            BacSi bsTH = null;

            if (cchnTH == null || cchnTH.isBlank()) {
                addError(group, hs.getMaLk(), dv,
                        "Thiếu CCHN BS thực hiện");
            } else {
                bsTH = bacSiMap.get(normalizeCCHN(cchnTH));

                if (bsTH == null) {
                    addError(group, hs.getMaLk(), dv,
                            "Không tồn tại BS TH: " + cchnTH);
                }
            }

            xml4 kq = xml4Map.get(hs.getMaLk() + "_" + dv.getMaDichVu());

            BacSi bsDoc = null;

            if (kq != null) {

                String cchnDoc = kq.getMaBsDocKq();

                if (cchnDoc == null || cchnDoc.isBlank()) {
                    addError(group, hs.getMaLk(), dv,
                            "Thiếu BS đọc KQ");
                } else {
                    bsDoc = bacSiMap.get(normalizeCCHN(cchnDoc));

                    if (bsDoc == null) {
                        addError(group, hs.getMaLk(), dv,
                                "Không tồn tại BS đọc: " + cchnDoc);
                    }
                }
            }

            if (start == null || kqTime == null) {
                addError(group, hs.getMaLk(), dv,
                        "Thiếu thời gian");
                continue;
            }

            CaLamViec ca = findCa(start.toLocalTime(), caList);

            if (ca == null) {
                addError(group, hs.getMaLk(), dv,
                        "Ngoài giờ làm việc");
                continue;
            }

            ThuTrongTuan thu = getThu(start);

            if (bsTH != null) {

                boolean okTH =
                        phanCongRepo.existsByBacSi_IdAndDvkt_IdAndThuAndCaLamViec_IdAndVaiTro(
                                bsTH.getId(), dvkt.getId(), thu, ca.getId(),
                                VaiTroBacSi.THUC_HIEN
                        );

                if (!okTH) {
                    addError(group, hs.getMaLk(), dv,
                            String.format(
                                    "BS không được phân công TH (CCHN: %s - %s)",
                                    bsTH.getCchn(), bsTH.getHoTen()
                            ));
                }
            }

            if (bsDoc != null) {

                boolean okDoc =
                        phanCongRepo.existsByBacSi_IdAndDvkt_IdAndThuAndCaLamViec_IdAndVaiTro(
                                bsDoc.getId(), dvkt.getId(), thu, ca.getId(),
                                VaiTroBacSi.DOC_KQ
                        );

                if (!okDoc) {
                    addError(group, hs.getMaLk(), dv,
                            String.format(
                                    "BS không được phân công ĐỌC (CCHN: %s - %s)",
                                    bsDoc.getCchn(), bsDoc.getHoTen()
                            ));
                }
            }
        }
    }
}


