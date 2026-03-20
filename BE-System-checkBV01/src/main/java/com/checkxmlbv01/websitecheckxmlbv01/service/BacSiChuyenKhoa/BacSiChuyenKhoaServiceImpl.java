package com.checkxmlbv01.websitecheckxmlbv01.service.BacSiChuyenKhoa;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.checkxmlbv01.websitecheckxmlbv01.domain.BacSi;
import com.checkxmlbv01.websitecheckxmlbv01.domain.BacSiChuyenKhoa;
import com.checkxmlbv01.websitecheckxmlbv01.domain.ChuyenKhoa;
import com.checkxmlbv01.websitecheckxmlbv01.domain.DTO.BacSiChuyenKhoa.BacSiChuyenKhoaRequest;
import com.checkxmlbv01.websitecheckxmlbv01.domain.DTO.BacSiChuyenKhoa.BacSiChuyenKhoaResponse;
import com.checkxmlbv01.websitecheckxmlbv01.repository.BacSiChuyenKhoaRepository;
import com.checkxmlbv01.websitecheckxmlbv01.repository.BacSiRepository;
import com.checkxmlbv01.websitecheckxmlbv01.repository.ChuyenKhoaRepository;


@Service
public class BacSiChuyenKhoaServiceImpl implements BacSiChuyenKhoaService {

    private final BacSiRepository bacSiRepository;
    private final ChuyenKhoaRepository chuyenKhoaRepository;
    private final BacSiChuyenKhoaRepository repository;

    public BacSiChuyenKhoaServiceImpl(BacSiRepository bacSiRepository,ChuyenKhoaRepository chuyenKhoaRepository,BacSiChuyenKhoaRepository repository) {
        this.bacSiRepository = bacSiRepository;
        this.chuyenKhoaRepository = chuyenKhoaRepository;
        this.repository = repository;
    }
    // ================= CREATE =================
    @Override
    public BacSiChuyenKhoaResponse create(BacSiChuyenKhoaRequest request) {

        if (repository.existsByBacSiIdAndChuyenKhoaId(
                request.getBacSiId(),
                request.getChuyenKhoaId())) {

            throw new RuntimeException("Bác sĩ đã được gán chuyên khoa này");
        }

        BacSi bacSi = bacSiRepository.findById(request.getBacSiId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy bác sĩ"));

        ChuyenKhoa chuyenKhoa = chuyenKhoaRepository.findById(request.getChuyenKhoaId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy chuyên khoa"));

        BacSiChuyenKhoa entity = new BacSiChuyenKhoa();
        entity.setBacSi(bacSi);
        entity.setChuyenKhoa(chuyenKhoa);
        entity.setLaChuyenKhoaChinh(request.getLaChuyenKhoaChinh());
        entity.setGanLuc(LocalDateTime.now());

        return toResponse(repository.save(entity));
    }

    // ================= DELETE =================
    @Override
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("Không tìm thấy bản ghi");
        }
        repository.deleteById(id);
    }

    // ================= GET BY BAC SI =================
    @Override
    public List<BacSiChuyenKhoaResponse> getByBacSi(Long bacSiId) {

        return repository.findByBacSiId(bacSiId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    // ================= GET ALL =================
    @Override
    public List<BacSiChuyenKhoaResponse> getAll() {

        return repository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public BacSiChuyenKhoaResponse update(Long id, BacSiChuyenKhoaRequest request) {

        BacSiChuyenKhoa existing = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy bản ghi"));

        // Nếu đổi bác sĩ
        if (!existing.getBacSi().getId().equals(request.getBacSiId())) {

            BacSi bacSi = bacSiRepository.findById(request.getBacSiId())
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy bác sĩ"));

            existing.setBacSi(bacSi);
        }

        // Nếu đổi chuyên khoa
        if (!existing.getChuyenKhoa().getId().equals(request.getChuyenKhoaId())) {

            if (repository.existsByBacSiIdAndChuyenKhoaId(
                    request.getBacSiId(),
                    request.getChuyenKhoaId())) {

                throw new RuntimeException("Bác sĩ đã có chuyên khoa này");
            }

            ChuyenKhoa chuyenKhoa = chuyenKhoaRepository.findById(request.getChuyenKhoaId())
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy chuyên khoa"));

            existing.setChuyenKhoa(chuyenKhoa);
        }

        existing.setLaChuyenKhoaChinh(request.getLaChuyenKhoaChinh());
        existing.setGanLuc(LocalDateTime.now());

        return toResponse(repository.save(existing));
    }

    // ================= MAPPER =================
    private BacSiChuyenKhoaResponse toResponse(BacSiChuyenKhoa entity) {

        return BacSiChuyenKhoaResponse.builder()
                .id(entity.getId())
                .bacSiId(entity.getBacSi().getId())
                .tenBacSi(entity.getBacSi().getHoTen())
                .chuyenKhoaId(entity.getChuyenKhoa().getId())
                .tenChuyenKhoa(entity.getChuyenKhoa().getTenChuyenKhoa())
                .laChuyenKhoaChinh(entity.getLaChuyenKhoaChinh())
                .ganLuc(entity.getGanLuc())
                .build();
    }
}
