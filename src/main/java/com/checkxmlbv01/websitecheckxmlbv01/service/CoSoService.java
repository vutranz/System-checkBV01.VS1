package com.checkxmlbv01.websitecheckxmlbv01.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.checkxmlbv01.websitecheckxmlbv01.domain.CoSo;
import com.checkxmlbv01.websitecheckxmlbv01.domain.DTO.CoSoDTO.CoSoReqDTO;
import com.checkxmlbv01.websitecheckxmlbv01.domain.DTO.CoSoDTO.CoSoResDTO;
import com.checkxmlbv01.websitecheckxmlbv01.repository.CoSoRepository;
import com.checkxmlbv01.websitecheckxmlbv01.ultil.error.Exception.CoSoAlreadyExistsException;

@Service
public class CoSoService {

    private final CoSoRepository coSoRepository;

    public CoSoService(CoSoRepository coSoRepository) {
        this.coSoRepository = coSoRepository;
    }

    public CoSoResDTO create(CoSoReqDTO dto) {

        if (coSoRepository.existsByMaCoSo(dto.getMaCoSo())) {
            throw new CoSoAlreadyExistsException();
        }

        CoSo coSo = new CoSo();
        coSo.setMaCoSo(dto.getMaCoSo());
        coSo.setTenCoSo(dto.getTenCoSo());
        coSo.setTaoLuc(LocalDateTime.now());

        CoSo saved = coSoRepository.save(coSo);

        return toResponse(saved);
    }

    public CoSoResDTO getById(Long id) {
        CoSo coSo = coSoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy cơ sở"));

        return toResponse(coSo);
    }

    public List<CoSoResDTO> getAll() {
        return coSoRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public CoSoResDTO update(Long id, CoSoReqDTO dto) {

        CoSo coSo = coSoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy cơ sở"));

        coSo.setMaCoSo(dto.getMaCoSo());
        coSo.setTenCoSo(dto.getTenCoSo());

        CoSo updated = coSoRepository.save(coSo);

        return toResponse(updated);
    }

    public void delete(Long id) {

        if (!coSoRepository.existsById(id)) {
            throw new RuntimeException("Không tìm thấy cơ sở");
        }

        coSoRepository.deleteById(id);
    }

    private CoSoResDTO toResponse(CoSo coSo) {
        return new CoSoResDTO(
                coSo.getId(),
                coSo.getMaCoSo(),
                coSo.getTenCoSo(),
                coSo.getTaoLuc()
        );
    }
}

