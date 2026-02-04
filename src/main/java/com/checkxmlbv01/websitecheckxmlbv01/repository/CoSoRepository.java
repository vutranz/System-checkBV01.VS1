package com.checkxmlbv01.websitecheckxmlbv01.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.checkxmlbv01.websitecheckxmlbv01.domain.CoSo;

@Repository
public interface CoSoRepository extends JpaRepository<CoSo, Long> {

    
} 
