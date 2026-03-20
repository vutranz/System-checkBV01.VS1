package com.checkxmlbv01.websitecheckxmlbv01.service.PhanCongNangLuc;

import java.util.List;

import org.springframework.stereotype.Service;

import com.checkxmlbv01.websitecheckxmlbv01.domain.BacSi;
import com.checkxmlbv01.websitecheckxmlbv01.domain.CaLamViec;
import com.checkxmlbv01.websitecheckxmlbv01.domain.DichVuKyThuat;
import com.checkxmlbv01.websitecheckxmlbv01.domain.PhanCongNangLuc;
import com.checkxmlbv01.websitecheckxmlbv01.domain.DTO.PhanCongNangLuc.PhanCongNangLucRequest;
import com.checkxmlbv01.websitecheckxmlbv01.domain.DTO.PhanCongNangLuc.PhanCongNangLucResponse;
import com.checkxmlbv01.websitecheckxmlbv01.domain.enums.ThuTrongTuan;
import com.checkxmlbv01.websitecheckxmlbv01.repository.BacSiRepository;
import com.checkxmlbv01.websitecheckxmlbv01.repository.CaLamViecRepository;
import com.checkxmlbv01.websitecheckxmlbv01.repository.DichVuKyThuatRepository;
import com.checkxmlbv01.websitecheckxmlbv01.repository.LichLamViecTheoThuRepository;
import com.checkxmlbv01.websitecheckxmlbv01.repository.NangLucChuyenMonRepository;
import com.checkxmlbv01.websitecheckxmlbv01.repository.PhanCongNangLucRepository;


@Service

public class PhanCongNangLucServiceImpl 
        implements PhanCongNangLucService {

    private final PhanCongNangLucRepository repository;
    private final BacSiRepository bacSiRepository;
    private final CaLamViecRepository caRepository;
    private final DichVuKyThuatRepository dvktRepository;
    private final NangLucChuyenMonRepository nangLucRepository;
    private final LichLamViecTheoThuRepository lichRepository;
    
    public PhanCongNangLucServiceImpl(PhanCongNangLucRepository repository, 
                                        BacSiRepository bacSiRepository, CaLamViecRepository caRepository,
                                    DichVuKyThuatRepository dvktRepository,NangLucChuyenMonRepository nangLucRepository,
                                LichLamViecTheoThuRepository lichRepository) {
        this.repository = repository;
        this.bacSiRepository = bacSiRepository;
        this.caRepository = caRepository;
        this.dvktRepository = dvktRepository;
        this.nangLucRepository = nangLucRepository;
        this.lichRepository = lichRepository;
    }
    @Override
    public PhanCongNangLucResponse create(
            PhanCongNangLucRequest request) {

        if (repository.existsByBacSiIdAndThuAndCaLamViecIdAndDvktIdAndVaiTro(
                request.getBacSiId(),
                request.getThu(),
                request.getCaLamViecId(),
                request.getDvktId(),
                request.getVaiTro())) {

            throw new RuntimeException("Phân công đã tồn tại");
        }

        // Validate năng lực
        if (!nangLucRepository.existsByBacSiIdAndDvktIdAndVaiTro(
                request.getBacSiId(),
                request.getDvktId(),
                request.getVaiTro())) {

            throw new RuntimeException(
                    "Bác sĩ chưa có năng lực DVKT với vai trò này");
        }

        // Validate lịch làm việc
        if (!lichRepository.existsByBacSiIdAndThuAndCaLamViecId(
                request.getBacSiId(),
                request.getThu(),
                request.getCaLamViecId())) {

            throw new RuntimeException(
                    "Bác sĩ chưa có lịch làm việc ở ca này");
        }

        BacSi bacSi = bacSiRepository.findById(request.getBacSiId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy bác sĩ"));

        CaLamViec ca = caRepository.findById(request.getCaLamViecId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy ca"));

        DichVuKyThuat dvkt = dvktRepository.findById(request.getDvktId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy DVKT"));

        PhanCongNangLuc entity = new PhanCongNangLuc();
        entity.setBacSi(bacSi);
        entity.setThu(request.getThu());
        entity.setCaLamViec(ca);
        entity.setDvkt(dvkt);
        entity.setVaiTro(request.getVaiTro());

        return toResponse(repository.save(entity));
    }

    @Override
    public PhanCongNangLucResponse update(
            Long id,
            PhanCongNangLucRequest request) {

        PhanCongNangLuc entity = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy phân công"));

        entity.setThu(request.getThu());
        entity.setVaiTro(request.getVaiTro());

        return toResponse(repository.save(entity));
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }

    @Override
    public List<PhanCongNangLucResponse> getAll() {
        return repository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public List<PhanCongNangLucResponse> getByBacSi(Long bacSiId) {
        return repository.findByBacSiId(bacSiId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public List<PhanCongNangLucResponse> getByDvkt(Long dvktId) {
        return repository.findByDvktId(dvktId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public List<PhanCongNangLucResponse> getByThuAndCa(
            ThuTrongTuan thu,
            Long caLamViecId) {

        return repository.findByThuAndCaLamViecId(thu, caLamViecId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    private PhanCongNangLucResponse toResponse(
            PhanCongNangLuc entity) {

        return PhanCongNangLucResponse.builder()
                .id(entity.getId())
                .bacSiId(entity.getBacSi().getId())
                .tenBacSi(entity.getBacSi().getHoTen())
                .thu(entity.getThu())
                .caLamViecId(entity.getCaLamViec().getId())
                .tenCa(entity.getCaLamViec().getTenCa())
                .dvktId(entity.getDvkt().getId())
                .tenDvkt(entity.getDvkt().getTenDvkt())
                .vaiTro(entity.getVaiTro())
                .build();
    }
}
