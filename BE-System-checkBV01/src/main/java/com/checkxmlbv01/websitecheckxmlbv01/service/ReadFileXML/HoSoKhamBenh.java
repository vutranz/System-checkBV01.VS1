package com.checkxmlbv01.websitecheckxmlbv01.service.ReadFileXML;

import java.util.ArrayList;
import java.util.List;

import com.checkxmlbv01.websitecheckxmlbv01.domain.xml.xml1;
import com.checkxmlbv01.websitecheckxmlbv01.domain.xml.xml2;
import com.checkxmlbv01.websitecheckxmlbv01.domain.xml.xml3;
import com.checkxmlbv01.websitecheckxmlbv01.domain.xml.xml4;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HoSoKhamBenh {
    
    private xml1 thongTinChung;          // 1
    private List<xml2> dsThuoc = new ArrayList<>();
    private List<xml3> dsDichVu = new ArrayList<>();
    private List<xml4> dsCls = new ArrayList<>();

    public HoSoKhamBenh(xml1 xml1) {
        this.thongTinChung = xml1;
    }
}

