package com.checkxmlbv01.websitecheckxmlbv01.service.DichVuKyThuat;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.checkxmlbv01.websitecheckxmlbv01.domain.ChuyenKhoa;
import com.checkxmlbv01.websitecheckxmlbv01.domain.DichVuKyThuat;
import com.checkxmlbv01.websitecheckxmlbv01.domain.NhomDVKT;
import com.checkxmlbv01.websitecheckxmlbv01.domain.DTO.DichVuKyThuat.DichVuKyThuatRequest;
import com.checkxmlbv01.websitecheckxmlbv01.domain.DTO.DichVuKyThuat.DichVuKyThuatResponse;
import com.checkxmlbv01.websitecheckxmlbv01.domain.DTO.DichVuKyThuat.DichVuKyThuatTreeResponse;
import com.checkxmlbv01.websitecheckxmlbv01.repository.ChuyenKhoaRepository;
import com.checkxmlbv01.websitecheckxmlbv01.repository.DichVuKyThuatRepository;
import com.checkxmlbv01.websitecheckxmlbv01.repository.NhomDVKTRepository;

@Service
public class DichVuKyThuatServiceImpl implements DichVuKyThuatService {

    private final DichVuKyThuatRepository repository;
    private final NhomDVKTRepository nhomRepository;
    private final ChuyenKhoaRepository chuyenKhoaRepository;

     public DichVuKyThuatServiceImpl(DichVuKyThuatRepository repository,NhomDVKTRepository nhomRepository, ChuyenKhoaRepository chuyenKhoaRepository) {
        this.repository = repository;
        this.nhomRepository = nhomRepository;
        this.chuyenKhoaRepository = chuyenKhoaRepository;
    }
    @Override
    public DichVuKyThuatResponse create(DichVuKyThuatRequest request) {

        if (repository.existsByMaDvkt(request.getMaDvkt())) {
            throw new RuntimeException("Mã DVKT đã tồn tại");
        }

        validateThoiGian(request);

        NhomDVKT nhom = nhomRepository.findById(request.getNhomDvktId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy nhóm"));

        ChuyenKhoa ck = chuyenKhoaRepository.findById(request.getChuyenKhoaId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy chuyên khoa"));

        DichVuKyThuat parent = null;
        if (request.getDvktChaId() != null) {
            parent = repository.findById(request.getDvktChaId())
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy DVKT cha"));
        }

        DichVuKyThuat entity = new DichVuKyThuat();
        entity.setMaDvkt(request.getMaDvkt());
        entity.setTenDvkt(request.getTenDvkt());
        entity.setThoiGianMin(request.getThoiGianMin());
        entity.setThoiGianMax(request.getThoiGianMax());
        entity.setHoatDong(
                request.getHoatDong() != null ? request.getHoatDong() : true
        );
        entity.setNhomDVKT(nhom);
        entity.setChuyenKhoa(ck);
        entity.setDvktCha(parent);

        return toResponse(repository.save(entity));
    }

    @Override
    public DichVuKyThuatResponse update(Long id, DichVuKyThuatRequest request) {

    DichVuKyThuat entity = repository.findById(id)
            .orElseThrow(() -> new RuntimeException("Không tìm thấy DVKT"));

    // check unique mã
    if (!entity.getMaDvkt().equals(request.getMaDvkt())
            && repository.existsByMaDvkt(request.getMaDvkt())) {

        throw new RuntimeException("Mã DVKT đã tồn tại");
    }

    validateThoiGian(request);

    // update nhóm
    NhomDVKT nhom = nhomRepository.findById(request.getNhomDvktId())
            .orElseThrow(() -> new RuntimeException("Không tìm thấy nhóm"));

    // update chuyên khoa
    ChuyenKhoa ck = chuyenKhoaRepository.findById(request.getChuyenKhoaId())
            .orElseThrow(() -> new RuntimeException("Không tìm thấy chuyên khoa"));

    // update parent
    DichVuKyThuat parent = null;

    if (request.getDvktChaId() != null) {

        if (request.getDvktChaId().equals(id)) {
            throw new RuntimeException("DVKT không thể là cha của chính nó");
        }

        parent = repository.findById(request.getDvktChaId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy DVKT cha"));

        validateNoCycle(id, parent);
    }

    entity.setMaDvkt(request.getMaDvkt());
    entity.setTenDvkt(request.getTenDvkt());
    entity.setThoiGianMin(request.getThoiGianMin());
    entity.setThoiGianMax(request.getThoiGianMax());
    entity.setHoatDong(
            request.getHoatDong() != null ? request.getHoatDong() : entity.getHoatDong()
    );
    entity.setNhomDVKT(nhom);
    entity.setChuyenKhoa(ck);
    entity.setDvktCha(parent);

    return toResponse(repository.save(entity));
}

private void validateNoCycle(Long currentId, DichVuKyThuat parent) {

    while (parent != null) {

        if (parent.getId().equals(currentId)) {
            throw new RuntimeException("Không thể tạo vòng lặp trong cây DVKT");
        }

        parent = parent.getDvktCha();
    }
}

    @Override
    public void delete(Long id) {
        DichVuKyThuat entity = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy DVKT"));
        entity.setHoatDong(false); // soft delete
        repository.save(entity);
    }

    @Override
    public List<DichVuKyThuatResponse> getAll() {
        return repository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public List<DichVuKyThuatResponse> getRoot() {
        return repository.findByDvktChaIsNullAndHoatDongTrue()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public List<DichVuKyThuatResponse> getChildren(Long parentId) {
        return repository.findByDvktChaIdAndHoatDongTrue(parentId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public DichVuKyThuatResponse getById(Long id) {
        return repository.findById(id)
                .map(this::toResponse)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy DVKT"));
    }

    private void validateThoiGian(DichVuKyThuatRequest request) {
        if (request.getThoiGianMin() != null
                && request.getThoiGianMax() != null
                && request.getThoiGianMin() > request.getThoiGianMax()) {

            throw new RuntimeException("Thời gian min phải nhỏ hơn max");
        }
    }

    @Override
    public List<DichVuKyThuatTreeResponse> getTree() {

        List<DichVuKyThuat> all = repository.findByHoatDongTrue();

        // convert sang TreeResponse
        Map<Long, DichVuKyThuatTreeResponse> map = all.stream()
                .map(this::toTreeResponse)
                .collect(Collectors.toMap(
                        DichVuKyThuatTreeResponse::getId,
                        dv -> dv
                ));

        List<DichVuKyThuatTreeResponse> roots = new ArrayList<>();

        for (DichVuKyThuat entity : all) {

            DichVuKyThuatTreeResponse current = map.get(entity.getId());

            if (entity.getDvktCha() == null) {
                roots.add(current);
            } else {
                Long parentId = entity.getDvktCha().getId();
                DichVuKyThuatTreeResponse parent = map.get(parentId);

                if (parent != null) {
                    parent.getChildren().add(current);
                }
            }
        }

        return roots;
    }

    private DichVuKyThuatTreeResponse toTreeResponse(DichVuKyThuat entity) {

        DichVuKyThuatTreeResponse res = new DichVuKyThuatTreeResponse();
        res.setId(entity.getId());
        res.setMaDvkt(entity.getMaDvkt());
        res.setTenDvkt(entity.getTenDvkt());
        res.setHoatDong(entity.getHoatDong());
    
        if (entity.getDvktCha() != null) {
            res.setDvktChaId(entity.getDvktCha().getId());
        }
    
        return res;
    }

    private DichVuKyThuatResponse toResponse(DichVuKyThuat entity) {

        return DichVuKyThuatResponse.builder()
                .id(entity.getId())
                .maDvkt(entity.getMaDvkt())
                .tenDvkt(entity.getTenDvkt())
                .thoiGianMin(entity.getThoiGianMin())
                .thoiGianMax(entity.getThoiGianMax())
                .hoatDong(entity.getHoatDong())
                .nhomDvktId(entity.getNhomDVKT().getId())
                .tenNhom(entity.getNhomDVKT().getTenNhom())
                .chuyenKhoaId(entity.getChuyenKhoa().getId())
                .tenChuyenKhoa(entity.getChuyenKhoa().getTenChuyenKhoa())
                .dvktChaId(entity.getDvktCha() != null ? entity.getDvktCha().getId() : null)
                .tenDvktCha(entity.getDvktCha() != null ? entity.getDvktCha().getTenDvkt() : null)
                .build();
    }

    
}
