package com.checkxmlbv01.websitecheckxmlbv01.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.checkxmlbv01.websitecheckxmlbv01.domain.ChuyenKhoa;

@Repository
public interface ChuyenKhoaRepository extends JpaRepository<ChuyenKhoa, Long> {

    boolean existsByMaChuyenKhoaAndCoSoId(String maChuyenKhoa, Long coSoId);

}
