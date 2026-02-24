package com.checkxmlbv01.websitecheckxmlbv01.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.checkxmlbv01.websitecheckxmlbv01.domain.BacSi;

@Repository
public interface BacSiRepository extends JpaRepository<BacSi, Long> {

    boolean existsByMaBacSiAndCoSoId(String maBacSi, Long coSoId);
}
