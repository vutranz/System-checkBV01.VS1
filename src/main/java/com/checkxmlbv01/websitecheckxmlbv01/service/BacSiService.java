package com.checkxmlbv01.websitecheckxmlbv01.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.checkxmlbv01.websitecheckxmlbv01.domain.BacSi;
import com.checkxmlbv01.websitecheckxmlbv01.domain.CoSo;
import com.checkxmlbv01.websitecheckxmlbv01.domain.DTO.BacSiDTO.BacSiReqDTO;
import com.checkxmlbv01.websitecheckxmlbv01.domain.DTO.BacSiDTO.BacSiResDTO;
import com.checkxmlbv01.websitecheckxmlbv01.domain.DTO.BacSiDTO.BacSiUpdateDTO;
import com.checkxmlbv01.websitecheckxmlbv01.repository.BacSiRepository;
import com.checkxmlbv01.websitecheckxmlbv01.repository.CoSoRepository;
import com.checkxmlbv01.websitecheckxmlbv01.ultil.error.ErrorCodes;
import com.checkxmlbv01.websitecheckxmlbv01.ultil.error.Exception.BusinessException;

@Service
public class BacSiService {

    private final BacSiRepository bacSiRepository;
    private final CoSoRepository coSoRepository;

    public BacSiService(BacSiRepository bacSiRepository, CoSoRepository coSoRepository){
        this.bacSiRepository=bacSiRepository;
        this.coSoRepository=coSoRepository;
    }

    public BacSiResDTO create(BacSiReqDTO request) {

        CoSo coSo = coSoRepository.findById(request.getCoSoId())
                .orElseThrow(() -> new BusinessException(ErrorCodes.ID_INVALID));

        if (bacSiRepository.existsByMaBacSiAndCoSoId(
                request.getMaBacSi(),
                request.getCoSoId())) {

            throw new BusinessException(ErrorCodes.BAC_SI_DUPLICATE);
        }

        BacSi entity = new BacSi();
        entity.setCoSo(coSo);
        entity.setMaBacSi(request.getMaBacSi());
        entity.setHoTen(request.getHoTen());
        entity.setCchn(request.getCchn());
        entity.setHoatDong(
                request.getHoatDong() != null ? request.getHoatDong() : true
        );

        bacSiRepository.save(entity);

        return toDTO(entity);
    }

    public BacSiResDTO update(Long id, BacSiUpdateDTO request) {

    BacSi entity = findEntity(id);

    if (request.getMaBacSi() != null) {
        entity.setMaBacSi(request.getMaBacSi());
    }

    if (request.getHoTen() != null) {
        entity.setHoTen(request.getHoTen());
    }

    if (request.getCchn() != null) {
        entity.setCchn(request.getCchn());
    }

    if (request.getHoatDong() != null) {
        entity.setHoatDong(request.getHoatDong());
    }

    BacSi saved = bacSiRepository.save(entity);

    return toDTO(saved);
}

    public List<BacSiResDTO> getAll() {
        return bacSiRepository.findAll()
                .stream()
                .map(this::toDTO)
                .toList();
    }

    public BacSiResDTO getById(Long id) {
        return toDTO(findEntity(id));
    }

    public void delete(Long id) {
        BacSi entity = findEntity(id);
        bacSiRepository.delete(entity);
    }

    private BacSi findEntity(Long id) {
        return bacSiRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCodes.ID_INVALID));
    }

    private BacSiResDTO toDTO(BacSi entity) {
        return new BacSiResDTO(
                entity.getId(),
                entity.getCoSo().getId(),
                entity.getMaBacSi(),
                entity.getHoTen(),
                entity.getCchn(),
                entity.getHoatDong()
        );
    }
}
