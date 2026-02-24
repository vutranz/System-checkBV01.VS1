package com.checkxmlbv01.websitecheckxmlbv01.service;


import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.checkxmlbv01.websitecheckxmlbv01.domain.CoSo;
import com.checkxmlbv01.websitecheckxmlbv01.domain.NguoiDung;
import com.checkxmlbv01.websitecheckxmlbv01.domain.NguoiDungCoSo;
import com.checkxmlbv01.websitecheckxmlbv01.domain.DTO.NguoiDungCoSo.NguoiDungCoSoResDTO;
import com.checkxmlbv01.websitecheckxmlbv01.domain.DTO.NguoiDungCoSo.UpdateNguoiDungCoSoReqDTO;
import com.checkxmlbv01.websitecheckxmlbv01.repository.CoSoRepository;
import com.checkxmlbv01.websitecheckxmlbv01.repository.NguoiDungRepository;
import com.checkxmlbv01.websitecheckxmlbv01.repository.NguoiDungCoSoRepository;
import com.checkxmlbv01.websitecheckxmlbv01.ultil.error.ErrorCodes;
import com.checkxmlbv01.websitecheckxmlbv01.ultil.error.Exception.BusinessException;

@Service
public class NguoiDungCoSoService {

    private final NguoiDungCoSoRepository nguoiDungCoSoRepository;
    private final NguoiDungRepository nguoiDungRepository;
    private final CoSoRepository coSoRepository;

    public NguoiDungCoSoService(
            NguoiDungCoSoRepository nguoiDungCoSoRepository,
            NguoiDungRepository nguoiDungRepository,
            CoSoRepository coSoRepository) {

        this.nguoiDungCoSoRepository = nguoiDungCoSoRepository;
        this.nguoiDungRepository = nguoiDungRepository;
        this.coSoRepository = coSoRepository;
    }

    // CREATE
    public NguoiDungCoSoResDTO assign(Long nguoiDungId, Long coSoId) {

        if (nguoiDungCoSoRepository
                .existsByNguoiDungIdAndCoSoId(nguoiDungId, coSoId)) {
            throw new BusinessException(ErrorCodes.USER_ALREADY_ASSIGNED);
        }

        NguoiDung nguoiDung = nguoiDungRepository.findById(nguoiDungId)
                .orElseThrow(() -> new BusinessException(ErrorCodes.USER_NOT_FOUND));

        CoSo coSo = coSoRepository.findById(coSoId)
                .orElseThrow(() -> new BusinessException(ErrorCodes.COSO_NOT_FOUND));

        NguoiDungCoSo entity = new NguoiDungCoSo();
        entity.setNguoiDung(nguoiDung);
        entity.setCoSo(coSo);
        entity.setGanLuc(LocalDateTime.now());

        return toResponse(nguoiDungCoSoRepository.save(entity));
    }

    // READ ALL
    public List<NguoiDungCoSoResDTO> getAll() {
        return nguoiDungCoSoRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    // READ BY ID
    public NguoiDungCoSoResDTO getById(Long id) {
        NguoiDungCoSo entity = nguoiDungCoSoRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCodes.ASSIGNMENT_NOT_FOUND));

        return toResponse(entity);
    }

    // DELETE
    public void delete(Long id) {

        if (!nguoiDungCoSoRepository.existsById(id)) {
            throw new BusinessException(ErrorCodes.ASSIGNMENT_NOT_FOUND);
        }

        nguoiDungCoSoRepository.deleteById(id);
    }

    // MAPPER
    private NguoiDungCoSoResDTO toResponse(NguoiDungCoSo entity) {
        return new NguoiDungCoSoResDTO(
                entity.getId(),
                entity.getNguoiDung().getId(),
                entity.getNguoiDung().getTenDangNhap(),
                entity.getCoSo().getId(),
                entity.getCoSo().getTenCoSo(),
                entity.getGanLuc()
        );
    }

    // UPDATE FULL
    public NguoiDungCoSoResDTO update(Long id, UpdateNguoiDungCoSoReqDTO request) {

        NguoiDungCoSo entity = nguoiDungCoSoRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCodes.ASSIGNMENT_NOT_FOUND));

        NguoiDung nguoiDung = nguoiDungRepository.findById(request.getNguoiDungId())
                .orElseThrow(() -> new BusinessException(ErrorCodes.USER_NOT_FOUND));

        CoSo coSo = coSoRepository.findById(request.getCoSoId())
                .orElseThrow(() -> new BusinessException(ErrorCodes.COSO_NOT_FOUND));

        entity.setNguoiDung(nguoiDung);
        entity.setCoSo(coSo);
        entity.setGanLuc(LocalDateTime.now());

        return toResponse(nguoiDungCoSoRepository.save(entity));
    }
}
