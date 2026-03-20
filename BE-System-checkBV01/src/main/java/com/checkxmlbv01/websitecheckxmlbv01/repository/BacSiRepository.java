package com.checkxmlbv01.websitecheckxmlbv01.repository;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.checkxmlbv01.websitecheckxmlbv01.domain.BacSi;


public interface BacSiRepository extends JpaRepository<BacSi, Long> {


   Optional<BacSi> findByCchn(String cchn);

    boolean existsByCchn(String cchn);

}