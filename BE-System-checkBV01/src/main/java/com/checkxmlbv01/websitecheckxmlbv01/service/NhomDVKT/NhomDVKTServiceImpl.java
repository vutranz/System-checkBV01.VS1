package com.checkxmlbv01.websitecheckxmlbv01.service.NhomDVKT;

import java.util.List;
import org.springframework.stereotype.Service;


import com.checkxmlbv01.websitecheckxmlbv01.domain.NhomDVKT;

import com.checkxmlbv01.websitecheckxmlbv01.domain.DTO.NhomDVKT.NhomDVKTRequest;
import com.checkxmlbv01.websitecheckxmlbv01.domain.DTO.NhomDVKT.NhomDVKTResponse;
import com.checkxmlbv01.websitecheckxmlbv01.repository.NhomDVKTRepository;

import com.checkxmlbv01.websitecheckxmlbv01.ultil.error.Exception.NotFoundException;

@Service
public class NhomDVKTServiceImpl implements NhomDVKTService {

    private final NhomDVKTRepository repository;

     public NhomDVKTServiceImpl(NhomDVKTRepository repository) {
        this.repository = repository;
    }

    @Override
    public NhomDVKTResponse create(NhomDVKTRequest request) {

        if (repository.existsByTenNhom(request.getTenNhom())) {
            throw new RuntimeException("Tên nhóm đã tồn tại");
        }

        NhomDVKT entity = new NhomDVKT();
        entity.setTenNhom(request.getTenNhom());

        return toResponse(repository.save(entity));
    }

    @Override
    public NhomDVKTResponse update(Long id, NhomDVKTRequest request) {

        NhomDVKT entity = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy nhóm"));

        if (!entity.getTenNhom().equals(request.getTenNhom())
                && repository.existsByTenNhom(request.getTenNhom())) {

            throw new RuntimeException("Tên nhóm đã tồn tại");
        }

        entity.setTenNhom(request.getTenNhom());

        return toResponse(repository.save(entity));
    }


    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }

    @Override
    public List<NhomDVKTResponse> getAll() {
        return repository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public NhomDVKTResponse getById(Long id) {
    
        NhomDVKT entity = repository.findById(id)
                .orElseThrow(() ->
                        new NotFoundException("Không tìm thấy nhóm DVKT với id: " + id)
                );
    
        return toResponse(entity);
    }

    private NhomDVKTResponse toResponse(NhomDVKT entity) {
        return NhomDVKTResponse.builder()
                .id(entity.getId())
                .tenNhom(entity.getTenNhom())
                .build();
    }
}
