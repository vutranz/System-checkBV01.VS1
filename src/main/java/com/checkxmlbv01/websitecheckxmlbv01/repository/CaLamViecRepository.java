package com.checkxmlbv01.websitecheckxmlbv01.repository;

import java.time.LocalTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.checkxmlbv01.websitecheckxmlbv01.domain.CaLamViec;

public interface CaLamViecRepository extends JpaRepository<CaLamViec, Long> {
    List<CaLamViec> findByCoSoId(Long coSoId);
    
    @Query("""
        SELECT COUNT(c) > 0
        FROM CaLamViec c
        WHERE c.coSo.id = :coSoId
        AND (:gioBatDau < c.gioKetThuc AND :gioKetThuc > c.gioBatDau)
    """)
    boolean existsOverlap(Long coSoId, LocalTime gioBatDau, LocalTime gioKetThuc);

    @Query("""
        SELECT COUNT(c) > 0
        FROM CaLamViec c
        WHERE c.coSo.id = :coSoId
        AND c.id <> :id
        AND (:gioBatDau < c.gioKetThuc AND :gioKetThuc > c.gioBatDau)
    """)
    boolean existsOverlapForUpdate(Long coSoId, Long id,
                                   LocalTime gioBatDau, LocalTime gioKetThuc);
}
