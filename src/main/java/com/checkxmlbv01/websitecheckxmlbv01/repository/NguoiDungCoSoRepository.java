package com.checkxmlbv01.websitecheckxmlbv01.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.checkxmlbv01.websitecheckxmlbv01.domain.NguoiDungCoSo;

@Repository
public interface NguoiDungCoSoRepository extends JpaRepository<NguoiDungCoSo, Long> {
    boolean existsByNguoiDungIdAndCoSoId(Long nguoiDungId, Long coSoId);

    Optional<NguoiDungCoSo> findByNguoiDungIdAndCoSoId(Long nguoiDungId, Long coSoId);

    List<NguoiDungCoSo> findByNguoiDungId(Long nguoiDungId);

    List<NguoiDungCoSo> findByCoSoId(Long coSoId);
}
