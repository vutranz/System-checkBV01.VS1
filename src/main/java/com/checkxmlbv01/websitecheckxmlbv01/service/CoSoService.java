package com.checkxmlbv01.websitecheckxmlbv01.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.checkxmlbv01.websitecheckxmlbv01.domain.CoSo;
import com.checkxmlbv01.websitecheckxmlbv01.domain.DTO.CoSoResDTO;
import com.checkxmlbv01.websitecheckxmlbv01.repository.CoSoRepository;

@Service
public class CoSoService {
    private final CoSoRepository coSoRepository;

    public CoSoService(CoSoRepository coSoRepository){
        this.coSoRepository=coSoRepository;
    }

    public CoSoResDTO createCoSo(CoSoResDTO dto) {
        CoSo coSo = new CoSo();
        coSo.setMaCoSo(dto.getMaCoSo());
        coSo.setTenCoSo(dto.getTenCoSo());
        coSo.setTaoLuc(LocalDateTime.now());

        CoSo saved = coSoRepository.save(coSo);

        return new CoSoResDTO(
                saved.getId(),
                saved.getMaCoSo(),
                saved.getTenCoSo(),
                saved.getTaoLuc()
        );

    }
}
