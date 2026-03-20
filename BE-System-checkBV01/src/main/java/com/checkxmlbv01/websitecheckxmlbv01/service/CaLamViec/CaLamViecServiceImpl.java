package com.checkxmlbv01.websitecheckxmlbv01.service.CaLamViec;

import java.util.List;

import org.springframework.stereotype.Service;

import com.checkxmlbv01.websitecheckxmlbv01.domain.CaLamViec;
import com.checkxmlbv01.websitecheckxmlbv01.domain.DTO.CaLamViec.CaLamViecRequest;
import com.checkxmlbv01.websitecheckxmlbv01.domain.DTO.CaLamViec.CaLamViecResponse;
import com.checkxmlbv01.websitecheckxmlbv01.repository.CaLamViecRepository;

@Service
public class CaLamViecServiceImpl implements CaLamViecService {

    private final CaLamViecRepository repository;

    public CaLamViecServiceImpl(CaLamViecRepository repository) {
        this.repository = repository;
    }
    @Override
    public CaLamViecResponse create(CaLamViecRequest request) {

        if (repository.existsByTenCa(request.getTenCa())) {
            throw new RuntimeException("Tên ca đã tồn tại");
        }

        if (request.getGioKetThuc().isBefore(request.getGioBatDau())) {
            throw new RuntimeException("Giờ kết thúc phải sau giờ bắt đầu");
        }

        CaLamViec entity = new CaLamViec();
        entity.setTenCa(request.getTenCa());
        entity.setGioBatDau(request.getGioBatDau());
        entity.setGioKetThuc(request.getGioKetThuc());
        entity.setHoatDong(
                request.getHoatDong() != null ? request.getHoatDong() : true
        );

        return toResponse(repository.save(entity));
    }

    @Override
    public CaLamViecResponse update(Long id, CaLamViecRequest request) {

        CaLamViec entity = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy ca làm việc"));

        if (!entity.getTenCa().equals(request.getTenCa())
                && repository.existsByTenCa(request.getTenCa())) {

            throw new RuntimeException("Tên ca đã tồn tại");
        }

        if (request.getGioKetThuc().isBefore(request.getGioBatDau())) {
            throw new RuntimeException("Giờ kết thúc phải sau giờ bắt đầu");
        }

        entity.setTenCa(request.getTenCa());
        entity.setGioBatDau(request.getGioBatDau());
        entity.setGioKetThuc(request.getGioKetThuc());
        entity.setHoatDong(request.getHoatDong());

        return toResponse(repository.save(entity));
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }

    @Override
    public List<CaLamViecResponse> getAll() {
        return repository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public CaLamViecResponse getById(Long id) {
        return repository.findById(id)
                .map(this::toResponse)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy ca làm việc"));
    }

    private CaLamViecResponse toResponse(CaLamViec entity) {
        return CaLamViecResponse.builder()
                .id(entity.getId())
                .tenCa(entity.getTenCa())
                .gioBatDau(entity.getGioBatDau())
                .gioKetThuc(entity.getGioKetThuc())
                .hoatDong(entity.getHoatDong())
                .build();
    }
}
