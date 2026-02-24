package com.checkxmlbv01.websitecheckxmlbv01.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.checkxmlbv01.websitecheckxmlbv01.domain.ChuyenKhoa;
import com.checkxmlbv01.websitecheckxmlbv01.domain.CoSo;
import com.checkxmlbv01.websitecheckxmlbv01.domain.DTO.ChuyenKhoaDTO.ChuyenKhoaReqDTO;
import com.checkxmlbv01.websitecheckxmlbv01.domain.DTO.ChuyenKhoaDTO.ChuyenKhoaResDTO;
import com.checkxmlbv01.websitecheckxmlbv01.domain.DTO.ChuyenKhoaDTO.ChuyenKhoaUpdateDTO;
import com.checkxmlbv01.websitecheckxmlbv01.repository.ChuyenKhoaRepository;
import com.checkxmlbv01.websitecheckxmlbv01.repository.CoSoRepository;
import com.checkxmlbv01.websitecheckxmlbv01.ultil.error.ErrorCodes;
import com.checkxmlbv01.websitecheckxmlbv01.ultil.error.Exception.BusinessException;

@Service
public class ChuyenKhoaService {

    private final ChuyenKhoaRepository chuyenKhoaRepository;
    private final CoSoRepository coSoRepository;

    public ChuyenKhoaService(ChuyenKhoaRepository chuyenKhoaRepository, CoSoRepository coSoRepository){
        this.chuyenKhoaRepository=chuyenKhoaRepository;
        this.coSoRepository=coSoRepository;
    }

    

    public ChuyenKhoaResDTO create(ChuyenKhoaReqDTO request) {

        CoSo coSo = coSoRepository.findById(request.getCoSoId())
                .orElseThrow(() -> new BusinessException(ErrorCodes.COSO_NOT_FOUND));

        if (chuyenKhoaRepository.existsByMaChuyenKhoaAndCoSoId(
                request.getMaChuyenKhoa(),
                request.getCoSoId())) {

            throw new BusinessException(ErrorCodes.ID_INVALID); 
        }

        ChuyenKhoa entity = new ChuyenKhoa();
        entity.setCoSo(coSo);
        entity.setMaChuyenKhoa(request.getMaChuyenKhoa());
        entity.setTenChuyenKhoa(request.getTenChuyenKhoa());
        entity.setHoatDong(
                request.getHoatDong() != null ? request.getHoatDong() : true
        );

        chuyenKhoaRepository.save(entity);

        return toDTO(entity);
    }

    public ChuyenKhoaResDTO update(Long id, ChuyenKhoaUpdateDTO request) {

    ChuyenKhoa entity = findEntity(id);

    if (request.getMaChuyenKhoa() != null) {
        entity.setMaChuyenKhoa(request.getMaChuyenKhoa());
    }

    if (request.getTenChuyenKhoa() != null) {
        entity.setTenChuyenKhoa(request.getTenChuyenKhoa());
    }

    if (request.getHoatDong() != null) {
        entity.setHoatDong(request.getHoatDong());
    }

    ChuyenKhoa saved = chuyenKhoaRepository.save(entity);

    return toDTO(saved);
}

    public List<ChuyenKhoaResDTO> getAll() {
        return chuyenKhoaRepository.findAll()
                .stream()
                .map(this::toDTO)
                .toList();
    }

    public ChuyenKhoaResDTO getById(Long id) {
        return toDTO(findEntity(id));
    }

    public void delete(Long id) {
        ChuyenKhoa entity = findEntity(id);
        chuyenKhoaRepository.delete(entity);
    }

    private ChuyenKhoa findEntity(Long id) {
    return chuyenKhoaRepository.findById(id)
            .orElseThrow(() -> new BusinessException(ErrorCodes.ID_INVALID));
    }

    private ChuyenKhoaResDTO toDTO(ChuyenKhoa entity) {
        return new ChuyenKhoaResDTO(
                entity.getId(),
                entity.getCoSo().getId(),
                entity.getMaChuyenKhoa(),
                entity.getTenChuyenKhoa(),
                entity.getHoatDong()
        );
    }
}
