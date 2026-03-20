package com.checkxmlbv01.websitecheckxmlbv01.service.LichLamViecTheoThu;

import java.util.List;

import org.springframework.stereotype.Service;

import com.checkxmlbv01.websitecheckxmlbv01.domain.BacSi;
import com.checkxmlbv01.websitecheckxmlbv01.domain.CaLamViec;
import com.checkxmlbv01.websitecheckxmlbv01.domain.LichLamViecTheoThu;
import com.checkxmlbv01.websitecheckxmlbv01.domain.DTO.LichLamViecTheoThu.LichLamViecTheoThuRequest;
import com.checkxmlbv01.websitecheckxmlbv01.domain.DTO.LichLamViecTheoThu.LichLamViecTheoThuResponse;
import com.checkxmlbv01.websitecheckxmlbv01.repository.BacSiRepository;
import com.checkxmlbv01.websitecheckxmlbv01.repository.CaLamViecRepository;
import com.checkxmlbv01.websitecheckxmlbv01.repository.LichLamViecTheoThuRepository;

@Service
public class LichLamViecTheoThuServiceImpl 
        implements LichLamViecTheoThuService {

    private final LichLamViecTheoThuRepository repository;
    private final BacSiRepository bacSiRepository;
    private final CaLamViecRepository caLamViecRepository;
    
    public LichLamViecTheoThuServiceImpl(LichLamViecTheoThuRepository repository, 
        BacSiRepository bacSiRepository, CaLamViecRepository caLamViecRepository) {
        this.repository = repository;
        this.bacSiRepository = bacSiRepository;
        this.caLamViecRepository = caLamViecRepository;
    }

    @Override
    public LichLamViecTheoThuResponse create(
            LichLamViecTheoThuRequest request) {

        if (repository.existsByBacSiIdAndThuAndCaLamViecId(
                request.getBacSiId(),
                request.getThu(),
                request.getCaLamViecId())) {

            throw new RuntimeException(
                    "Bác sĩ đã có lịch làm việc ở ca này trong ngày này");
        }

        BacSi bacSi = bacSiRepository.findById(request.getBacSiId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy bác sĩ"));

        CaLamViec caLamViec = caLamViecRepository.findById(
                request.getCaLamViecId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy ca làm việc"));

        LichLamViecTheoThu entity = new LichLamViecTheoThu();
        entity.setBacSi(bacSi);
        entity.setThu(request.getThu());
        entity.setCaLamViec(caLamViec);

        return toResponse(repository.save(entity));
    }

    @Override
    public LichLamViecTheoThuResponse update(
            Long id,
            LichLamViecTheoThuRequest request) {

        LichLamViecTheoThu entity = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy lịch"));

        if (repository.existsByBacSiIdAndThuAndCaLamViecId(
                request.getBacSiId(),
                request.getThu(),
                request.getCaLamViecId())) {

            throw new RuntimeException(
                    "Bác sĩ đã có lịch làm việc ở ca này trong ngày này");
        }

        BacSi bacSi = bacSiRepository.findById(request.getBacSiId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy bác sĩ"));

        CaLamViec caLamViec = caLamViecRepository.findById(
                request.getCaLamViecId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy ca làm việc"));

        entity.setBacSi(bacSi);
        entity.setThu(request.getThu());
        entity.setCaLamViec(caLamViec);

        return toResponse(repository.save(entity));
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }

    @Override
    public List<LichLamViecTheoThuResponse> getByBacSi(Long bacSiId) {
        return repository.findByBacSiId(bacSiId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public List<LichLamViecTheoThuResponse> getAll() {
        return repository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    private LichLamViecTheoThuResponse toResponse(
            LichLamViecTheoThu entity) {

        return LichLamViecTheoThuResponse.builder()
                .id(entity.getId())
                .bacSiId(entity.getBacSi().getId())
                .tenBacSi(entity.getBacSi().getHoTen())
                .thu(entity.getThu())
                .caLamViecId(entity.getCaLamViec().getId())
                .tenCa(entity.getCaLamViec().getTenCa())
                .build();
    }
}