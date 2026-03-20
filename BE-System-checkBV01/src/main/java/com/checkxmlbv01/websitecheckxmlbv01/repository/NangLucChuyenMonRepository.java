package com.checkxmlbv01.websitecheckxmlbv01.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.checkxmlbv01.websitecheckxmlbv01.domain.NangLucChuyenMon;
import com.checkxmlbv01.websitecheckxmlbv01.domain.enums.VaiTroBacSi;

public interface NangLucChuyenMonRepository 
        extends JpaRepository<NangLucChuyenMon, Long> {

    boolean existsByBacSiIdAndDvktIdAndVaiTro(
            Long bacSiId,
            Long dvktId,
            VaiTroBacSi vaiTro
    );

    List<NangLucChuyenMon> findByBacSiId(Long bacSiId);

    List<NangLucChuyenMon> findByDvktId(Long dvktId);
}
