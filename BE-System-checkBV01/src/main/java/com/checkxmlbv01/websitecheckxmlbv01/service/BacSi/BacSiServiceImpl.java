package com.checkxmlbv01.websitecheckxmlbv01.service.BacSi;

import java.util.List;

import org.springframework.stereotype.Service;

import com.checkxmlbv01.websitecheckxmlbv01.domain.BacSi;
import com.checkxmlbv01.websitecheckxmlbv01.repository.BacSiRepository;

@Service
public class BacSiServiceImpl implements BacSiService {

    private final BacSiRepository bacSiRepository;

    public BacSiServiceImpl(BacSiRepository bacSiRepository) {
        this.bacSiRepository = bacSiRepository;
    }

    @Override
    public BacSi create(BacSi bacSi) {

        if (bacSiRepository.existsByCchn(bacSi.getCchn())) {
            throw new RuntimeException("Mã bác sĩ đã tồn tại");
        }

        return bacSiRepository.save(bacSi);
    }

    @Override
    public BacSi update(Long id, BacSi bacSi) {

        BacSi existing = bacSiRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy bác sĩ"));

        existing.setHoTen(bacSi.getHoTen());
        existing.setCchn(bacSi.getCchn());
        existing.setHoatDong(bacSi.getHoatDong());

        return bacSiRepository.save(existing);
    }

    @Override
    public void delete(Long id) {

        BacSi existing = bacSiRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy bác sĩ"));

        bacSiRepository.delete(existing);
    }

    @Override
    public BacSi getById(Long id) {
        return bacSiRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy bác sĩ"));
    }

    @Override
    public List<BacSi> getAll() {
        return bacSiRepository.findAll();
    }
}
