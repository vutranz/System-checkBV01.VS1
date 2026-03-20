package com.checkxmlbv01.websitecheckxmlbv01.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.checkxmlbv01.websitecheckxmlbv01.domain.ChuyenKhoa;

import java.util.Optional;

public interface ChuyenKhoaRepository extends JpaRepository<ChuyenKhoa, Long> {

    Optional<com.checkxmlbv01.websitecheckxmlbv01.domain.ChuyenKhoa> findByMaChuyenKhoa(String maChuyenKhoa);

    boolean existsByMaChuyenKhoa(String maChuyenKhoa);
}
