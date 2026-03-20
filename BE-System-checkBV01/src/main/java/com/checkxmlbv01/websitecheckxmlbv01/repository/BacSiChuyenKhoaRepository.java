package com.checkxmlbv01.websitecheckxmlbv01.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.checkxmlbv01.websitecheckxmlbv01.domain.BacSiChuyenKhoa;

public interface BacSiChuyenKhoaRepository 
        extends JpaRepository<BacSiChuyenKhoa, Long> {
     boolean existsByBacSiIdAndChuyenKhoaId(Long bacSiId, Long chuyenKhoaId);

    List<BacSiChuyenKhoa> findByBacSiId(Long bacSiId);

}
