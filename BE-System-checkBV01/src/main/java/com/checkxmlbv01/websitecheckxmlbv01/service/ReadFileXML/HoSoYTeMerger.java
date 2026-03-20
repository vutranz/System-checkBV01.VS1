package com.checkxmlbv01.websitecheckxmlbv01.service.ReadFileXML;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.checkxmlbv01.websitecheckxmlbv01.domain.xml.xml1;
import com.checkxmlbv01.websitecheckxmlbv01.domain.xml.xml2;
import com.checkxmlbv01.websitecheckxmlbv01.domain.xml.xml3;
import com.checkxmlbv01.websitecheckxmlbv01.domain.xml.xml4;

public class HoSoYTeMerger {

    public static List<HoSoKhamBenh> merge(
            List<xml1> xml1List,
            List<xml2> xml2List,
            List<xml3> xml3List,
            List<xml4> xml4List
    ) {

        Map<String, HoSoKhamBenh> map = new LinkedHashMap<>();

        // ===== XML1 =====
        for (xml1 x1 : xml1List) {
            String maLk = x1.getMaLk();

            HoSoKhamBenh hs = map.computeIfAbsent(
                    maLk,
                    k -> new HoSoKhamBenh()
            );

            hs.setThongTinChung(x1);
        }

        // ===== XML2 =====
        for (xml2 x2 : xml2List) {
            String maLk = x2.getMaLk();

            HoSoKhamBenh hs = map.computeIfAbsent(
                    maLk,
                    k -> new HoSoKhamBenh()
            );

            hs.getDsThuoc().add(x2);
        }

        // ===== XML3 =====
        for (xml3 x3 : xml3List) {
            String maLk = x3.getMaLk();

            HoSoKhamBenh hs = map.computeIfAbsent(
                    maLk,
                    k -> new HoSoKhamBenh()
            );

            hs.getDsDichVu().add(x3);
        }

        // ===== XML4 =====
        for (xml4 x4 : xml4List) {
            String maLk = x4.getMaLk();

            HoSoKhamBenh hs = map.computeIfAbsent(
                    maLk,
                    k -> new HoSoKhamBenh()
            );

            hs.getDsCls().add(x4);
        }

        
        List<HoSoKhamBenh> validList = map.values().stream()
        .filter(hs -> hs.getThongTinChung() != null)
        .toList();

        return validList;
    }
}
