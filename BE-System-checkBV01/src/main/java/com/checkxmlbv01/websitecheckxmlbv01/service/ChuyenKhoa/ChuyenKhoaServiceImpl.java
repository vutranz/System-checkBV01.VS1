package com.checkxmlbv01.websitecheckxmlbv01.service.ChuyenKhoa;

import org.springframework.stereotype.Service;

import com.checkxmlbv01.websitecheckxmlbv01.domain.ChuyenKhoa;
import com.checkxmlbv01.websitecheckxmlbv01.domain.DTO.ChuyenKhoa.ChuyenKhoaRequest;
import com.checkxmlbv01.websitecheckxmlbv01.domain.DTO.ChuyenKhoa.ChuyenKhoaResponse;
import com.checkxmlbv01.websitecheckxmlbv01.repository.ChuyenKhoaRepository;

import java.util.List;

@Service
public class ChuyenKhoaServiceImpl implements ChuyenKhoaService {

    private final ChuyenKhoaRepository repository;

    public ChuyenKhoaServiceImpl(ChuyenKhoaRepository repository) {
        this.repository = repository;
    }

   public ChuyenKhoaResponse create(ChuyenKhoaRequest request) {

        if (repository.existsByMaChuyenKhoa(request.getMaChuyenKhoa())) {
            throw new RuntimeException("Mã chuyên khoa đã tồn tại");
        }

        ChuyenKhoa entity = new ChuyenKhoa();
        entity.setMaChuyenKhoa(request.getMaChuyenKhoa());
        entity.setTenChuyenKhoa(request.getTenChuyenKhoa());
        entity.setHoatDong(request.getHoatDong());

        if (request.getParentId() != null) {
            ChuyenKhoa parent = repository.findById(request.getParentId())
                    .orElseThrow(() -> new RuntimeException("Chuyên khoa cha không tồn tại"));

            entity.setChuyenKhoaCha(parent);
        }

        return toResponse(repository.save(entity));
    }

    public ChuyenKhoaResponse update(Long id, ChuyenKhoaRequest request) {

        ChuyenKhoa existing = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy chuyên khoa"));

        // Check mã nếu thay đổi
        if (!existing.getMaChuyenKhoa().equals(request.getMaChuyenKhoa())) {
            if (repository.existsByMaChuyenKhoa(request.getMaChuyenKhoa())) {
                throw new RuntimeException("Mã chuyên khoa đã tồn tại");
            }
            existing.setMaChuyenKhoa(request.getMaChuyenKhoa());
        }

        existing.setTenChuyenKhoa(request.getTenChuyenKhoa());
        existing.setHoatDong(request.getHoatDong());

        // xử lý cha
        if (request.getParentId() != null) {

            if (request.getParentId().equals(id)) {
                throw new RuntimeException("Không thể gán chính nó làm cha");
            }

            ChuyenKhoa parent = repository.findById(request.getParentId())
                    .orElseThrow(() -> new RuntimeException("Chuyên khoa cha không tồn tại"));

            existing.setChuyenKhoaCha(parent);

        } else {
            existing.setChuyenKhoaCha(null);
        }

        return toResponse(repository.save(existing));
    }

    @Override
    public List<ChuyenKhoaResponse> getAll() {
        return repository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public ChuyenKhoaResponse getById(Long id) {
        ChuyenKhoa entity = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy chuyên khoa"));
        return toResponse(entity);
    }

    @Override
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("Không tìm thấy chuyên khoa");
        }
        repository.deleteById(id);
    }

    private ChuyenKhoaResponse toResponse(ChuyenKhoa entity) {
        return ChuyenKhoaResponse.builder()
                .id(entity.getId())
                .maChuyenKhoa(entity.getMaChuyenKhoa())
                .tenChuyenKhoa(entity.getTenChuyenKhoa())
                .hoatDong(entity.getHoatDong())
                .parentId(entity.getChuyenKhoaCha() != null
                        ? entity.getChuyenKhoaCha().getId()
                        : null)
                .build();
    }
}
