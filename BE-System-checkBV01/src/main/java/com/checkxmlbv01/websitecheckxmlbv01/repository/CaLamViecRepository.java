package com.checkxmlbv01.websitecheckxmlbv01.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.checkxmlbv01.websitecheckxmlbv01.domain.CaLamViec;

public interface CaLamViecRepository extends JpaRepository<CaLamViec, Long> {

    boolean existsByTenCa(String tenCa);
    List<CaLamViec> findByHoatDong(Boolean hoatDong);

}
