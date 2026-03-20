package com.checkxmlbv01.websitecheckxmlbv01.service.validation;

import java.util.List;
import java.util.Map;

import com.checkxmlbv01.websitecheckxmlbv01.domain.BacSi;
import com.checkxmlbv01.websitecheckxmlbv01.domain.CaLamViec;
import com.checkxmlbv01.websitecheckxmlbv01.domain.DichVuKyThuat;
import com.checkxmlbv01.websitecheckxmlbv01.domain.ErrorGroup.ErrorKCBGroup;
import com.checkxmlbv01.websitecheckxmlbv01.domain.xml.xml1;
import com.checkxmlbv01.websitecheckxmlbv01.domain.xml.xml2;
import com.checkxmlbv01.websitecheckxmlbv01.domain.xml.xml3;
import com.checkxmlbv01.websitecheckxmlbv01.domain.xml.xml4;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ValidationContext {

    private xml1 hoSo;
    private List<xml3> dsDv;
    private List<xml2> dsThuoc;

    private Map<String, DichVuKyThuat> dvktMap;
    private Map<String, xml4> xml4Map;

    private Map<String, BacSi> bacSiMap;
    private List<CaLamViec> caList;

    private ErrorKCBGroup errorGroup;
}
