package com.checkxmlbv01.websitecheckxmlbv01.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.checkxmlbv01.websitecheckxmlbv01.domain.DichVuKyThuat;

public interface DichVuKyThuatRepository 
        extends JpaRepository<DichVuKyThuat, Long> {

    boolean existsByMaDvkt(String maDvkt);

    List<DichVuKyThuat> findByDvktChaIsNullAndHoatDongTrue();

    List<DichVuKyThuat> findByDvktChaIdAndHoatDongTrue(Long parentId);

    List<DichVuKyThuat> findByHoatDongTrue();
}