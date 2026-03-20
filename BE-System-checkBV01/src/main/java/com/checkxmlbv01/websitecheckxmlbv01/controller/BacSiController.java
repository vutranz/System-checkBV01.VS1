package com.checkxmlbv01.websitecheckxmlbv01.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.checkxmlbv01.websitecheckxmlbv01.domain.BacSi;
import com.checkxmlbv01.websitecheckxmlbv01.domain.DTO.RestResponse;
import com.checkxmlbv01.websitecheckxmlbv01.service.BacSi.BacSiService;

@RestController
@RequestMapping("/api/v1/bac-si")
public class BacSiController {

    private final BacSiService bacSiService;

    public BacSiController(BacSiService bacSiService) {
        this.bacSiService = bacSiService;
    }

    @PostMapping
    public RestResponse<BacSi> create(@RequestBody BacSi bacSi) {
        try {
            BacSi result = bacSiService.create(bacSi);
            return RestResponse.success(result);
        } catch (Exception e) {
            return RestResponse.error("Tạo bác sĩ thất bại",
                    List.of(e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public RestResponse<BacSi> update(@PathVariable Long id,
                                      @RequestBody BacSi bacSi) {
        try {
            BacSi result = bacSiService.update(id, bacSi);
            return RestResponse.success(result);
        } catch (Exception e) {
            return RestResponse.error("Cập nhật thất bại",
                    List.of(e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public RestResponse<Void> delete(@PathVariable Long id) {
        try {
            bacSiService.delete(id);
            return RestResponse.success(null);
        } catch (Exception e) {
            return RestResponse.error("Xoá thất bại",
                    List.of(e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    public RestResponse<BacSi> getById(@PathVariable Long id) {
        try {
            return RestResponse.success(bacSiService.getById(id));
        } catch (Exception e) {
            return RestResponse.error("Không tìm thấy",
                    List.of(e.getMessage()));
        }
    }

    @GetMapping
    public RestResponse<List<BacSi>> getAll() {
        return RestResponse.success(bacSiService.getAll());
    }
}
