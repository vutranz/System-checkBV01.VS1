package com.checkxmlbv01.websitecheckxmlbv01.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.checkxmlbv01.websitecheckxmlbv01.domain.CaLamViec;
import com.checkxmlbv01.websitecheckxmlbv01.domain.PhanCongNangLuc;
import com.checkxmlbv01.websitecheckxmlbv01.domain.enums.ThuTrongTuan;
import com.checkxmlbv01.websitecheckxmlbv01.domain.enums.VaiTroBacSi;

public interface PhanCongNangLucRepository 
        extends JpaRepository<PhanCongNangLuc, Long> {

    boolean existsByBacSiIdAndThuAndCaLamViecIdAndDvktIdAndVaiTro(
            Long bacSiId,
            ThuTrongTuan thu,
            Long caLamViecId,
            Long dvktId,
            VaiTroBacSi vaiTro
    );

    boolean existsByBacSiIdAndDvktIdAndThuAndCaLamViecAndVaiTro(
            Long bacSiId,
            Long dvktId,
            ThuTrongTuan thu,
            CaLamViec ca,
            VaiTroBacSi vaiTro
    );

    boolean existsByBacSiIdAndDvktIdAndThuAndCaLamViecIdAndVaiTro(
            Long bacSiId,
            Long dvktId,
            ThuTrongTuan thu,
            Long caLamViecId,
            VaiTroBacSi vaiTro
    );

    boolean existsByBacSi_MaBacSiAndDvktIdAndThuAndCaLamViecIdAndVaiTro(
        String maBacSi,
        Long dvktId,
        ThuTrongTuan thu,
        Long caLamViecId,
        VaiTroBacSi vaiTro
);

boolean existsByBacSi_IdAndDvkt_IdAndThuAndCaLamViec_IdAndVaiTro(
        Long bacSiId,
        Long dvktId,
        ThuTrongTuan thu,
        Long caLamViecId,
        VaiTroBacSi vaiTro
);

boolean existsByBacSi_IdAndThuAndCaLamViec_Id(
            Long bacSiId,
            ThuTrongTuan thu,
            Long caId
    );

    boolean existsByBacSi_IdAndThuAndCaLamViec_IdAndVaiTro(
            Long bacSiId,
            ThuTrongTuan thu,
            Long caId,
            VaiTroBacSi vaiTro
    );

    List<PhanCongNangLuc> findByBacSiId(Long bacSiId);

    List<PhanCongNangLuc> findByDvktId(Long dvktId);

    List<PhanCongNangLuc> findByThuAndCaLamViecId(
            ThuTrongTuan thu,
            Long caLamViecId
    );
}
