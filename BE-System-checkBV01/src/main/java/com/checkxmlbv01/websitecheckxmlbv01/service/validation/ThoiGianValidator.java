package com.checkxmlbv01.websitecheckxmlbv01.service.validation;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
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
import java.util.Optional;
import com.checkxmlbv01.websitecheckxmlbv01.domain.xml.xml4;
import com.checkxmlbv01.websitecheckxmlbv01.repository.BacSiRepository;
import com.checkxmlbv01.websitecheckxmlbv01.repository.CaLamViecRepository;
import com.checkxmlbv01.websitecheckxmlbv01.repository.PhanCongNangLucRepository;

@Component
public class ThoiGianValidator {

    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("yyyyMMddHHmm");
    private static final String NHOM_CONG_KHAM = "Công Khám";
    private static final String MA_LOAI_KCB_NGOAI_TRU = "02";

    private final CaLamViecRepository caRepo;
    private final PhanCongNangLucRepository phanCongRepo;
    private final BacSiRepository bacSiRepo;

    public ThoiGianValidator(CaLamViecRepository caRepo,
                             PhanCongNangLucRepository phanCongRepo,
                             BacSiRepository bacSiRepo) {
        this.caRepo = caRepo;
        this.phanCongRepo = phanCongRepo;
        this.bacSiRepo = bacSiRepo;
    }

    /* ========================= UTIL ========================= */

    private LocalDateTime parse(String time) {
        if (time == null || time.isBlank()) return null;
        time = time.trim();

        List<DateTimeFormatter> formats = List.of(
            DateTimeFormatter.ofPattern("yyyyMMddHHmm"),
            DateTimeFormatter.ofPattern("yyyyMMddHHmmss"),
            DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"),
            DateTimeFormatter.ISO_LOCAL_DATE_TIME
        );

        for (DateTimeFormatter fmt : formats) {
            try { return LocalDateTime.parse(time, fmt); }
            catch (Exception ignored) {}
        }
        System.out.println("PARSE FAIL: [" + time + "]");
        return null;
    }

    private String normalizeCCHN(String cchn) {
        return cchn == null ? null : cchn.trim();
    }

    private boolean laCongKham(DichVuKyThuat dvkt) {
        return dvkt != null
            && dvkt.getNhomDVKT() != null
            && dvkt.getNhomDVKT().getTenNhom() != null
            && dvkt.getNhomDVKT().getTenNhom().toLowerCase(Locale.ROOT)
                .contains(NHOM_CONG_KHAM.toLowerCase(Locale.ROOT));
    }

    private boolean laThuThuat(DichVuKyThuat dvkt) {
        return dvkt != null
            && dvkt.getNhomDVKT() != null
            && dvkt.getNhomDVKT().getTenNhom() != null
            && dvkt.getNhomDVKT().getTenNhom().toLowerCase(Locale.ROOT)
                .contains("thủ thuật");
    }

    private CaLamViec findCa(LocalTime time, List<CaLamViec> caList) {
        return caList.stream()
                .filter(ca -> {
                    LocalTime start = ca.getGioBatDau();
                    LocalTime end = ca.getGioKetThuc();
                    return (start.isBefore(end) && !time.isBefore(start) && !time.isAfter(end))
                        || (start.isAfter(end) && (!time.isBefore(start) || !time.isAfter(end)));
                })
                .findFirst().orElse(null);
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

    /* ========================= ERROR HANDLER ========================= */

    private void addError(ErrorKCBGroup group, xml1 hs, xml3 dv, BacSi bsTH, BacSi bsDoc, String message) {
        ErrorKCBDetail err = new ErrorKCBDetail();
        err.setMaLk(hs.getMaLk());
        err.setMaBn(hs.getMaBn());
        err.setHoten(hs.getHoTen());

        if (dv != null) {
            err.setMaDichVu(dv.getMaDichVu());
            err.setTenDichVu(dv.getTenDichVu());
            err.setMaBsCd(dv.getMaBacSi());
            err.setMaBsTh(bsTH != null ? bsTH.getHoTen() : "");
            err.setMaBsDocKq(bsDoc != null ? bsDoc.getHoTen() : "");
            err.setNgayYl(dv.getNgayYl());
            err.setNgayThyl(dv.getNgayThYl());
            err.setNgayKq(dv.getNgayKq());
        }

        err.setErrorDetail(message);
        group.addError(err);
    }

    private void addErrorDVKT(ErrorKCBGroup group, xml1 hs, xml3 dv,
                          Map<String, xml4> xml4Map,
                          Map<String, BacSi> bacSiMap,
                          String message) {

            ErrorKCBDetail err = new ErrorKCBDetail();
            err.setMaLk(hs.getMaLk());
            err.setMaBn(hs.getMaBn());
            err.setHoten(hs.getHoTen());
            err.setMaDichVu(dv.getMaDichVu());
            err.setTenDichVu(dv.getTenDichVu());

            // 🔥 BS CHỈ ĐỊNH
            String cchnCd = normalizeCCHN(dv.getMaBacSi());
            BacSi bsCd = cchnCd != null ? bacSiMap.get(cchnCd) : null;
            err.setMaBsCd(bsCd != null ? bsCd.getHoTen() : dv.getMaBacSi());

            // 🔥 BS THỰC HIỆN
            String cchnTH = normalizeCCHN(dv.getNguoiThucHien());
            BacSi bsTH = cchnTH != null ? bacSiMap.get(cchnTH) : null;
            err.setMaBsTh(bsTH != null ? bsTH.getHoTen() : dv.getNguoiThucHien());

            // 🔥 BS ĐỌC KQ
            xml4 kq = xml4Map.get(hs.getMaLk() + "_" + dv.getMaDichVu());
            if (kq != null) {
                String cchnDoc = normalizeCCHN(kq.getMaBsDocKq());
                BacSi bsDoc = cchnDoc != null ? bacSiMap.get(cchnDoc) : null;
                err.setMaBsDocKq(bsDoc != null ? bsDoc.getHoTen() : kq.getMaBsDocKq());
            }

            err.setNgayYl(dv.getNgayYl());
            err.setNgayThyl(dv.getNgayThYl());
            err.setNgayKq(dv.getNgayKq());
            err.setErrorDetail(message);

            group.addError(err);
        }

    private void checkPhanCong(BacSi bs, DichVuKyThuat dvkt, LocalDateTime start,
                               CaLamViec ca, VaiTroBacSi vaiTro, xml1 hs, xml3 dv, ErrorKCBGroup group) {
       if (dvkt == null) return; // bỏ qua thật

        if (bs == null) {
            addError(group, hs, dv, null, null, "Thiếu bác sĩ");
            return;
        }

        if (start == null) {
            addError(group, hs, dv, null, null, "Thiếu thời gian thực hiện");
            return;
        }

        if (ca == null) {
            addError(group, hs, dv, null, null, "Không xác định được ca làm việc");
            return;
        }
        boolean ok = phanCongRepo.existsByBacSi_IdAndDvkt_IdAndThuAndCaLamViec_IdAndVaiTro(
                bs.getId(), dvkt.getId(), getThu(start), ca.getId(), vaiTro
        );
        if (!ok) addError(group, hs, dv,
                vaiTro == VaiTroBacSi.THUC_HIEN ? bs : null,
                vaiTro == VaiTroBacSi.DOC_KQ ? bs : null,
                "BS không được phân công " + (vaiTro == VaiTroBacSi.THUC_HIEN ? "TH" : "ĐỌC") +
                        " (" + bs.getCchn() + " - " + bs.getHoTen() + ")");
    }

    private void checkGioLamViecBacSi(
            BacSi bs,
            LocalDateTime time,
            CaLamViec ca,
            VaiTroBacSi vaiTro,
            xml1 hs,
            xml3 dv,
            ErrorKCBGroup group
    ) {
        if (bs == null || time == null) return;

        if (ca == null) {
            addError(group, hs, dv, bs, null,
                    "⛔ BS " + bs.getHoTen() + " chỉ định ngoài giờ ("   + time + ")");
            return;
        }

        boolean coLich = phanCongRepo
                .existsByBacSi_IdAndThuAndCaLamViec_Id(
                        bs.getId(),
                        getThu(time),
                        ca.getId()
                );

        if (!coLich) {
            addError(group, hs, dv, bs, null,
                    "⛔ BS " + bs.getHoTen() + " không có lịch làm việc");
            return;
        }

        boolean dungVaiTro = phanCongRepo
                .existsByBacSi_IdAndThuAndCaLamViec_IdAndVaiTro(
                        bs.getId(),
                        getThu(time),
                        ca.getId(),
                        vaiTro
                );

        if (!dungVaiTro) {
            addError(group, hs, dv, bs, null,
                    "⛔ BS " + bs.getHoTen() + " không đúng vai trò " + vaiTro);
        }
    }
    /* ========================= CHECK TIME ========================= */

    private void checkThoiGianHoSo(xml1 hs, List<CaLamViec> caList, ErrorKCBGroup group) {
        LocalDateTime vao = parse(hs.getNgayVao());
        LocalDateTime ra  = parse(hs.getNgayRa());

        if (vao == null) { addError(group, hs, null, null, null, "Thiếu hoặc sai ngày vào viện"); return; }
        if (ra  == null) { addError(group, hs, null, null, null, "Thiếu hoặc sai ngày ra viện"); return; }
        if (!vao.isBefore(ra)) addError(group, hs, null, null, null, "Ngày ra viện phải sau ngày vào viện");

        if (findCa(vao.toLocalTime(), caList) == null)
            addError(group, hs, null, null, null, "Giờ vào viện ngoài giờ làm việc: " + hs.getNgayVao());

        if (findCa(ra.toLocalTime(), caList) == null)
            addError(group, hs, null, null, null, "Giờ ra viện ngoài giờ làm việc: " + hs.getNgayRa());
    }

    private void checkThuocTheoNgay(xml1 hs, List<xml3> dsDv, List<xml2> dsThuoc,
                                ErrorKCBGroup group, Map<String, BacSi> bacSiMap) {
        if (dsThuoc == null || dsThuoc.isEmpty()) return;

        Map<LocalDate, List<xml3>> dvByDate = dsDv.stream()
                .map(dv -> Map.entry(parse(dv.getNgayKq()), dv))
                .filter(e -> e.getKey() != null)
                .collect(Collectors.groupingBy(e -> e.getKey().toLocalDate(),
                        Collectors.mapping(Map.Entry::getValue, Collectors.toList())));

        Map<LocalDate, List<xml2>> thuocByDate = dsThuoc.stream()
                .map(t -> Map.entry(parse(t.getNgayYl()), t))
                .filter(e -> e.getKey() != null)
                .collect(Collectors.groupingBy(e -> e.getKey().toLocalDate(),
                        Collectors.mapping(Map.Entry::getValue, Collectors.toList())));

        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

        for (LocalDate date : thuocByDate.keySet()) {
            List<xml3> dvTrongNgay = dvByDate.get(date);
            if (dvTrongNgay == null || dvTrongNgay.isEmpty()) continue;

            LocalDateTime maxKQ = dvTrongNgay.stream()
                    .map(dv -> parse(dv.getNgayKq()))
                    .filter(Objects::nonNull)
                    .max(LocalDateTime::compareTo)
                    .orElse(null);
            if (maxKQ == null) continue;

            for (xml2 thuoc : thuocByDate.get(date)) {
            LocalDateTime tYL = parse(thuoc.getNgayYl());
            if (tYL == null || tYL.isAfter(maxKQ)) continue;

            ErrorKCBDetail err = new ErrorKCBDetail();
            err.setMaLk(hs.getMaLk());
            err.setMaBn(hs.getMaBn());
            err.setHoten(hs.getHoTen());
            err.setMaDichVu(thuoc.getMaThuoc());
            err.setTenDichVu(thuoc.getTenThuoc());

            // 🔥 convert CCHN → tên
            String cchn = normalizeCCHN(thuoc.getMaBacSi());
            BacSi bs = cchn != null ? bacSiMap.get(cchn) : null;
            String tenBs = bs != null ? bs.getHoTen() : thuoc.getMaBacSi();

            err.setMaBsCd(tenBs);
            err.setMaBsTh(tenBs);
            err.setMaBsDocKq(tenBs);

            err.setNgayYl(thuoc.getNgayYl());
            err.setNgayThyl(thuoc.getNgayThYl());
            err.setErrorDetail("Ngày " + date +
                    " - Thuốc YL: " + tYL.format(fmt) +
                    " phải sau CLS cuối [" + dvTrongNgay.get(0).getMaDichVu() + "]" +
                    " | KQ CLS: " + maxKQ.format(fmt));

            group.addError(err);
        }
    }
}

    private void checkThoiGianDVKT(xml1 hs, xml3 dv, DichVuKyThuat dvkt,
                               ErrorKCBGroup group, LocalDateTime congKhamEndTime,
                               Map<String, xml4> xml4Map,
                               Map<String, BacSi> bacSiMap,
                               boolean skipDongBo) {
    try {
        boolean congKham = laCongKham(dvkt);
        LocalDateTime yl = parse(dv.getNgayYl());
        LocalDateTime thyl = parse(dv.getNgayThYl());
        LocalDateTime kq = parse(dv.getNgayKq());

        LocalDateTime start = congKham ? yl : thyl;
        LocalDateTime end = kq;

        if (!congKham && yl != null && thyl != null && !yl.isBefore(thyl)) {
            addErrorDVKT(group, hs, dv, xml4Map, bacSiMap,
                    "YL (" + dv.getNgayYl() + ") phải < THYL (" + dv.getNgayThYl() + ")");
        }

        if (!congKham && congKhamEndTime != null && yl != null && !skipDongBo) {
            long diff = Duration.between(congKhamEndTime, yl).toMinutes();

            if (diff < 1)
                addErrorDVKT(group, hs, dv, xml4Map, bacSiMap,
                        "CLS phải sau công khám ≥1p | KQ khám: "
                                + congKhamEndTime.format(FMT)
                                + ", YL: " + yl.format(FMT));

            if (diff > 60)
                addErrorDVKT(group, hs, dv, xml4Map, bacSiMap,
                        "CLS không đồng bộ công khám (" + diff + " phút)");
        }

        if (start != null && end != null
                && dvkt.getThoiGianMin() != null
                && dvkt.getThoiGianMax() != null) {

            long diff = Duration.between(start, end).toMinutes();

            if (diff < dvkt.getThoiGianMin() || diff > dvkt.getThoiGianMax()) {
                addErrorDVKT(group, hs, dv, xml4Map, bacSiMap,
                        "Thời gian DV lệch " + diff + "p (chuẩn: "
                                + dvkt.getThoiGianMin() + "-" + dvkt.getThoiGianMax() + ")");
            }
        }

        if (start == null || end == null) {
            addErrorDVKT(group, hs, dv, xml4Map, bacSiMap,
                    "Thiếu thời gian YL/THYL/KQ");
        }

    } catch (Exception e) {
        addErrorDVKT(group, hs, dv, xml4Map, bacSiMap,
                "Lỗi xử lý thời gian: " + e.getMessage());
    }
}

    private void checkMaLoaiKcbThuThuat(xml1 hs,
                                            List<xml3> dsDv,
                                            Map<String, DichVuKyThuat> dvktMap,
                                            ErrorKCBGroup group) {

            if (hs == null || dsDv == null || dsDv.isEmpty()) return;

            boolean hasThuThuat = dsDv.stream()
                    .map(dv -> dvktMap.get(dv.getMaDichVu()))
                    .filter(Objects::nonNull)
                    .anyMatch(this::laThuThuat);

            boolean hasCongKham0816 = dsDv.stream()
                    .anyMatch(dv -> "08.16".equals(dv.getMaDichVu())
                            && laCongKham(dvktMap.get(dv.getMaDichVu())));

            if (hasThuThuat && hasCongKham0816 &&
                    !MA_LOAI_KCB_NGOAI_TRU.equals(hs.getMaLoaiKcb())) {

                addError(group, hs, null, null, null,
                        "Có công khám (YHCT) và phát sinh thủ thuật → mã loại KCB phải là (điều trị ngoại trú)");
            }
        }

    /**
 * Kiểm tra BS chỉ định và BS thực hiện trong hồ sơ:
 * 1. Nếu hồ sơ có công khám, xác định BS chỉ định chuẩn.
 * 2. BS thực hiện của công khám phải trùng BS chỉ định.
 * 3. Các DV khác trong hồ sơ phải dùng cùng BS chỉ định chuẩn.
 */
private void checkBsChuanTrongHoSo(xml1 hs, List<xml3> dsDv,
                                   Map<String, DichVuKyThuat> dvktMap,
                                   Map<String, BacSi> bacSiMap,
                                   ErrorKCBGroup group) {
    // Tìm công khám đầu tiên
    Optional<xml3> congKhamOpt = dsDv.stream()
            .filter(dv -> laCongKham(dvktMap.get(dv.getMaDichVu())))
            .findFirst();

    if (congKhamOpt.isEmpty()) return; // không có công khám → bỏ qua

    xml3 congKham = congKhamOpt.get();

    String cchnBscdChuan = normalizeCCHN(congKham.getMaBacSi());
    BacSi bsTHChuan = Optional.ofNullable(congKham.getNguoiThucHien())
                              .map(this::normalizeCCHN)
                              .map(bacSiMap::get)
                              .orElse(null);

    // Kiểm tra BS TH công khám có trùng BS chỉ định không
    if (bsTHChuan != null && cchnBscdChuan != null && !cchnBscdChuan.equals(bsTHChuan.getCchn())) {
        addError(group, hs, congKham, null, bsTHChuan,
                 "BS TH công khám phải trùng BS chỉ định");
    }

    // Kiểm tra các DV khác trong hồ sơ có cùng BS chỉ định chuẩn
    for (xml3 dv : dsDv) {
        if (dv == congKham) continue; // bỏ qua công khám

        String cchnCd = normalizeCCHN(dv.getMaBacSi());
        if (cchnCd != null && !cchnCd.equals(cchnBscdChuan)) {
            addError(group, hs, dv, null, null,
                     "BS chỉ định phải trùng với BS chỉ định công khám: " + cchnBscdChuan);
        }
    }
}

    /* ========================= MAIN ========================= */

    public void validate(xml1 hs, List<xml3> dsDv, List<xml2> dsThuoc,
                         Map<String, DichVuKyThuat> dvktMap, Map<String, xml4> xml4Map,
                         ErrorKCBGroup group) {
        if (hs == null || dsDv == null || dsDv.isEmpty()) return;

        List<CaLamViec> caList = caRepo.findByHoatDong(true);
        Map<String, BacSi> bacSiMap = bacSiRepo.findAll().stream()
                .collect(Collectors.toMap(bs -> normalizeCCHN(bs.getCchn()), bs -> bs, (a, b) -> a));
        
        // Kiểm tra BS chỉ định chuẩn trong hồ sơ nếu có công khám
        checkBsChuanTrongHoSo(hs, dsDv, dvktMap, bacSiMap, group);
        checkThoiGianHoSo(hs, caList, group);
        checkMaLoaiKcbThuThuat(hs, dsDv, dvktMap, group);


        // Kiểm tra thuốc < CLS || công khám, nếu có làm thủ thuật thì bỏ qua kh check
        boolean hasThuThuat = dsDv.stream()
            .map(dv -> dvktMap.get(dv.getMaDichVu()))
            .filter(Objects::nonNull)
            .anyMatch(this::laThuThuat);

        if (!hasThuThuat) {
            checkThuocTheoNgay(hs, dsDv, dsThuoc, group, bacSiMap);
        }

        LocalDateTime congKhamEndTime = dsDv.stream()
                .filter(dv -> laCongKham(dvktMap.get(dv.getMaDichVu())))
                .map(dv -> parse(dv.getNgayKq()))
                .filter(Objects::nonNull)
                .max(LocalDateTime::compareTo)
                .orElse(null);

        boolean skipDongBo = dsDv.stream()
                .anyMatch(dv -> "08.16".equals(dv.getMaDichVu()) && laCongKham(dvktMap.get(dv.getMaDichVu())));

        for (xml3 dv : dsDv) {

            DichVuKyThuat dvkt = dvktMap.get(dv.getMaDichVu());
            if (dvkt == null) {
                addError(group, hs, dv, null, null, "Không tồn tại DVKT");
                continue;
            }

            checkThoiGianDVKT(hs, dv, dvkt, group, congKhamEndTime, xml4Map, bacSiMap, skipDongBo);

            // ✅ LUÔN dùng ngày Y LỆNH
            LocalDateTime time = parse(dv.getNgayYl());

            // ✅ tìm ca theo giờ YL
            CaLamViec ca = time != null ? findCa(time.toLocalTime(), caList) : null;

            // ================== BS THỰC HIỆN ==================
            BacSi bsTH = Optional.ofNullable(dv.getNguoiThucHien())
                    .map(this::normalizeCCHN)
                    .map(bacSiMap::get)
                    .orElse(null);

            if (bsTH == null && dv.getNguoiThucHien() != null && !dv.getNguoiThucHien().isBlank()) {
                addError(group, hs, dv, null, null, "Không tồn tại BS TH: " + dv.getNguoiThucHien());
            } else if (dv.getNguoiThucHien() == null || dv.getNguoiThucHien().isBlank()) {
                addError(group, hs, dv, null, null, "Thiếu CCHN BS thực hiện");
            }

            // ================== BS ĐỌC ==================
            xml4 kq = xml4Map.get(hs.getMaLk() + "_" + dv.getMaDichVu());
            BacSi bsDoc = null;

            if (kq != null) {
                String cchnDoc = kq.getMaBsDocKq();

                if (cchnDoc == null || cchnDoc.isBlank()) {
                    addError(group, hs, dv, bsTH, null, "Thiếu BS đọc KQ");
                } else {
                    bsDoc = bacSiMap.get(normalizeCCHN(cchnDoc));

                    if (bsDoc == null) {
                        addError(group, hs, dv, bsTH, null, "Không tồn tại BS đọc: " + cchnDoc);
                    }
                }
            }

            // ================== CHECK GIỜ + PHÂN CÔNG ==================

            if (bsTH != null) {
                checkGioLamViecBacSi(
                        bsTH,
                        time,
                        ca,
                        VaiTroBacSi.THUC_HIEN,
                        hs,
                        dv,
                        group
                );
            }

            if (bsDoc != null) {
                checkGioLamViecBacSi(
                        bsDoc,
                        time,
                        ca,
                        VaiTroBacSi.DOC_KQ,
                        hs,
                        dv,
                        group
                );
            }

            // ================== CHECK PHÂN CÔNG ==================
        if (bsTH != null) {
            checkPhanCong(bsTH, dvkt, time, ca, VaiTroBacSi.THUC_HIEN, hs, dv, group);
        }

        if (bsDoc != null) {
            checkPhanCong(bsDoc, dvkt, time, ca, VaiTroBacSi.DOC_KQ, hs, dv, group);
        }
        }
    }
}


