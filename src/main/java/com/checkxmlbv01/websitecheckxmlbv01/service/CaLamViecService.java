package com.checkxmlbv01.websitecheckxmlbv01.service;

import java.time.LocalTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.checkxmlbv01.websitecheckxmlbv01.domain.CaLamViec;
import com.checkxmlbv01.websitecheckxmlbv01.domain.CoSo;
import com.checkxmlbv01.websitecheckxmlbv01.domain.DTO.CaLamViecDTO.CaLamViecReqDTO;
import com.checkxmlbv01.websitecheckxmlbv01.domain.DTO.CaLamViecDTO.CaLamViecResDTO;
import com.checkxmlbv01.websitecheckxmlbv01.domain.DTO.CaLamViecDTO.CaLamViecUpdateDTO;
import com.checkxmlbv01.websitecheckxmlbv01.repository.CaLamViecRepository;
import com.checkxmlbv01.websitecheckxmlbv01.repository.CoSoRepository;
import com.checkxmlbv01.websitecheckxmlbv01.ultil.error.ErrorCodes;
import com.checkxmlbv01.websitecheckxmlbv01.ultil.error.Exception.BusinessException;

@Service
public class CaLamViecService {

    private final CaLamViecRepository repository;
    private final CoSoRepository coSoRepository;

    public CaLamViecService(CaLamViecRepository repository,
                            CoSoRepository coSoRepository) {
        this.repository = repository;
        this.coSoRepository = coSoRepository;
    }

    // ================= CREATE =================
    public CaLamViecResDTO create(CaLamViecReqDTO request) {

        CoSo coSo = coSoRepository.findById(request.getCoSoId())
                .orElseThrow(() -> new BusinessException(ErrorCodes.COSO_NOT_FOUND));

        validateTime(request.getGioBatDau(), request.getGioKetThuc());

        if (repository.existsOverlap(
                request.getCoSoId(),
                request.getGioBatDau(),
                request.getGioKetThuc())) {
            throw new BusinessException(ErrorCodes.CA_LAM_VIEC_OVERLAP);
        }

        CaLamViec entity = new CaLamViec();
        entity.setCoSo(coSo);
        entity.setTenCa(request.getTenCa());
        entity.setGioBatDau(request.getGioBatDau());
        entity.setGioKetThuc(request.getGioKetThuc());

        return toDTO(repository.save(entity));
    }

    // ================= GET BY ID =================
    public CaLamViecResDTO getById(Long id) {
        return toDTO(findEntity(id));
    }

    // ================= GET LIST =================
    public List<CaLamViecResDTO> getByCoSo(Long coSoId) {
        return repository.findByCoSoId(coSoId)
                .stream()
                .map(this::toDTO)
                .toList();
    }

    // ================= UPDATE =================
    public CaLamViecResDTO update(Long id, CaLamViecUpdateDTO request) {

        CaLamViec entity = findEntity(id);

        LocalTime start = request.getGioBatDau() != null
                ? request.getGioBatDau()
                : entity.getGioBatDau();

        LocalTime end = request.getGioKetThuc() != null
                ? request.getGioKetThuc()
                : entity.getGioKetThuc();

        validateTime(start, end);

        if (repository.existsOverlapForUpdate(
                entity.getCoSo().getId(),
                id,
                start,
                end)) {
            throw new BusinessException(ErrorCodes.CA_LAM_VIEC_OVERLAP);
        }

        if (request.getTenCa() != null)
            entity.setTenCa(request.getTenCa());

        entity.setGioBatDau(start);
        entity.setGioKetThuc(end);

        return toDTO(repository.save(entity));
    }

    // ================= DELETE =================
    public void delete(Long id) {

        CaLamViec entity = findEntity(id);

        repository.delete(entity);
    }

    // ================= PRIVATE =================
    private CaLamViec findEntity(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCodes.ID_INVALID));
    }

    private void validateTime(LocalTime start, LocalTime end) {
        if (start == null || end == null || !start.isBefore(end)) {
            throw new BusinessException(ErrorCodes.TIME_INVALID);
        }
    }

    private CaLamViecResDTO toDTO(CaLamViec entity) {
        return new CaLamViecResDTO(
                entity.getId(),
                entity.getCoSo().getId(),
                entity.getTenCa(),
                entity.getGioBatDau(),
                entity.getGioKetThuc()
        );
    }
}
