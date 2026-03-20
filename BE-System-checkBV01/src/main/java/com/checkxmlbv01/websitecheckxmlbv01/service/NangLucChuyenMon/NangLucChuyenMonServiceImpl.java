package com.checkxmlbv01.websitecheckxmlbv01.service.NangLucChuyenMon;

import java.util.List;

import org.springframework.stereotype.Service;

import com.checkxmlbv01.websitecheckxmlbv01.domain.BacSi;
import com.checkxmlbv01.websitecheckxmlbv01.domain.DichVuKyThuat;
import com.checkxmlbv01.websitecheckxmlbv01.domain.NangLucChuyenMon;
import com.checkxmlbv01.websitecheckxmlbv01.domain.DTO.NangLucChuyenMon.NangLucChuyenMonRequest;
import com.checkxmlbv01.websitecheckxmlbv01.domain.DTO.NangLucChuyenMon.NangLucChuyenMonResponse;
import com.checkxmlbv01.websitecheckxmlbv01.repository.BacSiRepository;
import com.checkxmlbv01.websitecheckxmlbv01.repository.DichVuKyThuatRepository;
import com.checkxmlbv01.websitecheckxmlbv01.repository.NangLucChuyenMonRepository;


@Service
public class NangLucChuyenMonServiceImpl 
        implements NangLucChuyenMonService {

    private final NangLucChuyenMonRepository repository;
    private final BacSiRepository bacSiRepository;
    private final DichVuKyThuatRepository dvktRepository;
    
     public NangLucChuyenMonServiceImpl(NangLucChuyenMonRepository repository,BacSiRepository bacSiRepository,DichVuKyThuatRepository dvktRepository) {
        this.repository = repository;
        this.bacSiRepository = bacSiRepository;
        this.dvktRepository = dvktRepository;
    }

    @Override
    public NangLucChuyenMonResponse create(
            NangLucChuyenMonRequest request) {

        if (repository.existsByBacSiIdAndDvktIdAndVaiTro(
                request.getBacSiId(),
                request.getDvktId(),
                request.getVaiTro())) {

            throw new RuntimeException(
                    "Bác sĩ đã có năng lực này với vai trò này");
        }

        BacSi bacSi = bacSiRepository.findById(request.getBacSiId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy bác sĩ"));

        DichVuKyThuat dvkt = dvktRepository.findById(request.getDvktId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy DVKT"));

        NangLucChuyenMon entity = new NangLucChuyenMon();
        entity.setBacSi(bacSi);
        entity.setDvkt(dvkt);
        entity.setVaiTro(request.getVaiTro());

        return toResponse(repository.save(entity));
    }

    @Override
    public NangLucChuyenMonResponse update(
            Long id,
            NangLucChuyenMonRequest request) {

        NangLucChuyenMon entity = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy năng lực"));

        if (repository.existsByBacSiIdAndDvktIdAndVaiTro(
                request.getBacSiId(),
                request.getDvktId(),
                request.getVaiTro())) {

            throw new RuntimeException(
                    "Bác sĩ đã có năng lực này với vai trò này");
        }

        BacSi bacSi = bacSiRepository.findById(request.getBacSiId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy bác sĩ"));

        DichVuKyThuat dvkt = dvktRepository.findById(request.getDvktId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy DVKT"));

        entity.setBacSi(bacSi);
        entity.setDvkt(dvkt);
        entity.setVaiTro(request.getVaiTro());

        return toResponse(repository.save(entity));
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }

    @Override
    public List<NangLucChuyenMonResponse> getByBacSi(Long bacSiId) {
        return repository.findByBacSiId(bacSiId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public List<NangLucChuyenMonResponse> getByDvkt(Long dvktId) {
        return repository.findByDvktId(dvktId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public List<NangLucChuyenMonResponse> getAll() {
        return repository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    private NangLucChuyenMonResponse toResponse(
            NangLucChuyenMon entity) {

        return NangLucChuyenMonResponse.builder()
                .id(entity.getId())
                .bacSiId(entity.getBacSi().getId())
                .tenBacSi(entity.getBacSi().getHoTen())
                .dvktId(entity.getDvkt().getId())
                .tenDvkt(entity.getDvkt().getTenDvkt())
                .vaiTro(entity.getVaiTro())
                .build();
    }
}
