package com.checkxmlbv01.websitecheckxmlbv01.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.checkxmlbv01.websitecheckxmlbv01.domain.CoSo;
import com.checkxmlbv01.websitecheckxmlbv01.domain.NhomDVKT;
import com.checkxmlbv01.websitecheckxmlbv01.domain.DTO.NhomDVKT.NhomDVKTReqDTO;
import com.checkxmlbv01.websitecheckxmlbv01.domain.DTO.NhomDVKT.NhomDVKTResDTO;
import com.checkxmlbv01.websitecheckxmlbv01.domain.DTO.NhomDVKT.NhomDVKTUpdateDTO;
import com.checkxmlbv01.websitecheckxmlbv01.repository.CoSoRepository;
import com.checkxmlbv01.websitecheckxmlbv01.repository.NhomDVKTRepository;
import com.checkxmlbv01.websitecheckxmlbv01.ultil.error.ErrorCodes;
import com.checkxmlbv01.websitecheckxmlbv01.ultil.error.StorageException;
import com.checkxmlbv01.websitecheckxmlbv01.ultil.error.Exception.BusinessException;


@Service
public class NhomDVKTService {

    private final NhomDVKTRepository nhomDVKTRepository;
    private final CoSoRepository coSoRepository;

    public NhomDVKTService(NhomDVKTRepository nhomDVKTRepository,
                           CoSoRepository coSoRepository) {
        this.nhomDVKTRepository = nhomDVKTRepository;
        this.coSoRepository = coSoRepository;
    }

    public NhomDVKTResDTO create(NhomDVKTReqDTO request) {

        CoSo coSo = coSoRepository.findById(request.getCoSoId())
                .orElseThrow(() ->
                        new BusinessException(ErrorCodes.COSO_NOT_FOUND)
                );

        NhomDVKT entity = new NhomDVKT();
        entity.setTenNhom(request.getTenNhom());
        entity.setCoSo(coSo);

        NhomDVKT saved = nhomDVKTRepository.save(entity);

        return toDTO(saved);
    }

    public List<NhomDVKTResDTO> getAll() {
        return nhomDVKTRepository.findAll()
                .stream()
                .map(this::toDTO)
                .toList();
    }

    public NhomDVKTResDTO getById(Long id) {

        if (id == null || id <= 0) {
            throw new BusinessException(ErrorCodes.ID_INVALID);
        }

        return toDTO(findEntity(id));
    }

    public NhomDVKTResDTO update(Long id, NhomDVKTUpdateDTO request) {

        NhomDVKT entity = findEntity(id);

        entity.setTenNhom(request.getTenNhom());

        try {
            NhomDVKT saved = nhomDVKTRepository.save(entity);
            return toDTO(saved);
        } catch (Exception e) {
            throw new StorageException();
        }
    }

    public void delete(Long id) {

        if (!nhomDVKTRepository.existsById(id)) {
            throw new BusinessException(ErrorCodes.NHOM_DVKT_NOT_FOUND);
        }

        try {
            nhomDVKTRepository.deleteById(id);
        } catch (Exception e) {
            throw new StorageException();
        }
    }

    private NhomDVKT findEntity(Long id) {
        return nhomDVKTRepository.findById(id)
                .orElseThrow(() ->
                        new BusinessException(ErrorCodes.NHOM_DVKT_NOT_FOUND)
                );
    }

    private NhomDVKTResDTO toDTO(NhomDVKT entity) {
        return new NhomDVKTResDTO(
                entity.getId(),
                entity.getTenNhom(),
                entity.getCoSo().getId()
        );
    }
}