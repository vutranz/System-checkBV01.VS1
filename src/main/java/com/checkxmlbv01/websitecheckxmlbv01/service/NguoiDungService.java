package com.checkxmlbv01.websitecheckxmlbv01.service;

import java.time.LocalDateTime;
import java.util.List;


import org.springframework.stereotype.Service;

import com.checkxmlbv01.websitecheckxmlbv01.domain.NguoiDung;
import com.checkxmlbv01.websitecheckxmlbv01.domain.DTO.NguoiDungDTO.NguoiDungReqDTO;
import com.checkxmlbv01.websitecheckxmlbv01.domain.DTO.NguoiDungDTO.NguoiDungResDTO;
import com.checkxmlbv01.websitecheckxmlbv01.repository.NguoiDungRepository;
import com.checkxmlbv01.websitecheckxmlbv01.ultil.error.ErrorCodes;
import com.checkxmlbv01.websitecheckxmlbv01.ultil.error.StorageException;
import com.checkxmlbv01.websitecheckxmlbv01.ultil.error.Exception.BusinessException;


@Service
public class NguoiDungService {

    private final NguoiDungRepository nguoiDungRepository;

    public NguoiDungService(NguoiDungRepository nguoiDungRepository) {
        this.nguoiDungRepository = nguoiDungRepository;
    }

    public NguoiDungResDTO create(NguoiDungReqDTO dto) {

        if (nguoiDungRepository.existsByTenDangNhap(dto.getTenDangNhap())) {
            throw new BusinessException(ErrorCodes.USERNAME_DUPLICATE);
        }

        NguoiDung nd = new NguoiDung();
        nd.setTenDangNhap(dto.getTenDangNhap());
        nd.setMatKhau(dto.getMatKhau()); 
        nd.setHoTen(dto.getHoTen());
        nd.setHoatDong(dto.getHoatDong() != null ? dto.getHoatDong() : true);
        nd.setTaoLuc(LocalDateTime.now());

       NguoiDung saved = nguoiDungRepository.save(nd);

        return toRes(saved);
    }


    public List<NguoiDungResDTO> getAll() {
        return nguoiDungRepository.findAll()
                .stream()
                .map(this::toRes)
                .toList();
    }


    public NguoiDungResDTO getById(Long id) {

        if (id == null || id <= 0) {
            throw new BusinessException(ErrorCodes.ID_INVALID);
        }

        NguoiDung nd = nguoiDungRepository.findById(id)
                .orElseThrow(() ->
                        new BusinessException(ErrorCodes.USER_NOT_FOUND)
                );

        return toRes(nd);
    }


    public NguoiDungResDTO update(Long id, NguoiDungReqDTO dto) {

        NguoiDung nd = nguoiDungRepository.findById(id)
                .orElseThrow(() ->
                        new BusinessException(ErrorCodes.USER_NOT_FOUND)
                );

        nd.setHoTen(dto.getHoTen());
        nd.setHoatDong(dto.getHoatDong());
        nd.setCapNhatLuc(LocalDateTime.now());

        try {
            return toRes(nguoiDungRepository.save(nd));
        } catch (Exception e) {
            throw new StorageException();
        }
    }

    public void delete(Long id) {

        if (!nguoiDungRepository.existsById(id)) {
            throw new BusinessException(ErrorCodes.USER_NOT_FOUND);
        }

        try {
            nguoiDungRepository.deleteById(id);
        } catch (Exception e) {
            throw new StorageException();
        }
    }


    private NguoiDungResDTO toRes(NguoiDung nd) {
        return new NguoiDungResDTO(
                nd.getId(),
                nd.getTenDangNhap(),
                nd.getHoTen(),
                nd.getHoatDong(),
                nd.getTaoLuc(),
                nd.getCapNhatLuc()
        );
    }
}



