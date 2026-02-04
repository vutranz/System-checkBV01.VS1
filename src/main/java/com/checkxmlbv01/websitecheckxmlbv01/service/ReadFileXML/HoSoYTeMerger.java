package com.checkxmlbv01.websitecheckxmlbv01.service.ReadFileXML;

import java.util.ArrayList;
import java.util.HashMap;
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

        // 1️⃣ XML1 – tạo hồ sơ gốc
        for (xml1 x1 : xml1List) {
            HoSoKhamBenh hs = new HoSoKhamBenh();
            hs.setThongTinChung(x1);
            map.put(x1.getMaLk(), hs);
        }

        // 2️⃣ XML2 – thuốc
        for (xml2 x2 : xml2List) {
            HoSoKhamBenh hs = map.get(x2.getMaLk());
            if (hs != null) {
                hs.getDsThuoc().add(x2);
            }
        }

        // 3️⃣ XML3 – dịch vụ
        for (xml3 x3 : xml3List) {
            HoSoKhamBenh hs = map.get(x3.getMaLk());
            if (hs != null) {
                hs.getDsDichVu().add(x3);
            }
        }

        // 4️⃣ XML4 – CLS
        for (xml4 x4 : xml4List) {
            HoSoKhamBenh hs = map.get(x4.getMaLk());
            if (hs != null) {
                hs.getDsCls().add(x4);
            }
        }

        return new ArrayList<>(map.values());
    }
}
