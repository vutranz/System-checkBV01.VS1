package com.checkxmlbv01.websitecheckxmlbv01.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.checkxmlbv01.websitecheckxmlbv01.domain.LichLamViecTheoThu;
import com.checkxmlbv01.websitecheckxmlbv01.domain.enums.ThuTrongTuan;

public interface LichLamViecTheoThuRepository 
        extends JpaRepository<LichLamViecTheoThu, Long> {

    boolean existsByBacSiIdAndThuAndCaLamViecId(
            Long bacSiId,
            ThuTrongTuan thu,
            Long caLamViecId
    );

    List<LichLamViecTheoThu> findByBacSiId(Long bacSiId);
}
