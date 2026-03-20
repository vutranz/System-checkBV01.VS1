package com.checkxmlbv01.websitecheckxmlbv01.service.BacSi;

import java.util.List;

import com.checkxmlbv01.websitecheckxmlbv01.domain.BacSi;

public interface BacSiService {

    BacSi create(BacSi bacSi);

    BacSi update(Long id, BacSi bacSi);

    void delete(Long id);

    BacSi getById(Long id);

    List<BacSi> getAll();
}
