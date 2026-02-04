package com.checkxmlbv01.websitecheckxmlbv01;

import java.util.List;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.checkxmlbv01.websitecheckxmlbv01.domain.xml.xml1;
import com.checkxmlbv01.websitecheckxmlbv01.domain.xml.xml2;
import com.checkxmlbv01.websitecheckxmlbv01.domain.xml.xml3;
import com.checkxmlbv01.websitecheckxmlbv01.domain.xml.xml4;
import com.checkxmlbv01.websitecheckxmlbv01.service.ReadFileXML.ExcelReader_XML1;
import com.checkxmlbv01.websitecheckxmlbv01.service.ReadFileXML.ExcelReader_XML2;
import com.checkxmlbv01.websitecheckxmlbv01.service.ReadFileXML.ExcelReader_XML3;
import com.checkxmlbv01.websitecheckxmlbv01.service.ReadFileXML.ExcelReader_XML4;
import com.checkxmlbv01.websitecheckxmlbv01.service.ReadFileXML.GenericExcelReader;
import com.checkxmlbv01.websitecheckxmlbv01.service.ReadFileXML.HoSoKhamBenh;
import com.checkxmlbv01.websitecheckxmlbv01.service.ReadFileXML.HoSoYTeMerger;

@SpringBootApplication
public class WebsiteCheckXmlBv01Application {

    public static void main(String[] args) {
        SpringApplication.run(WebsiteCheckXmlBv01Application.class, args);

        String path1 = "C:\\Users\\Admin\\Desktop\\checkxmlbv01\\src\\main\\resources\\data_KCB_XML\\4750_xml1.xls";
        String path2 = "C:\\Users\\Admin\\Desktop\\checkxmlbv01\\src\\main\\resources\\data_KCB_XML\\4750_xml2.xls";
        String path3 = "C:\\Users\\Admin\\Desktop\\checkxmlbv01\\src\\main\\resources\\data_KCB_XML\\4750_xml3.xls";
        String path4 = "C:\\Users\\Admin\\Desktop\\checkxmlbv01\\src\\main\\resources\\data_KCB_XML\\4750_xml4.xls";
        
        List<xml1> xml1List = GenericExcelReader.read(path1, 66, new ExcelReader_XML1());
        List<xml2> xml2List = GenericExcelReader.read(path2, 39, new ExcelReader_XML2());
        List<xml3> xml3List = GenericExcelReader.read(path3, 47, new ExcelReader_XML3());
        List<xml4> xml4List = GenericExcelReader.read(path4, 12, new ExcelReader_XML4());

        List<HoSoKhamBenh> hoSoList =
                HoSoYTeMerger.merge(xml1List, xml2List, xml3List, xml4List);

        // ===== IN KẾT QUẢ =====
        for (HoSoKhamBenh hs : hoSoList) {
            xml1 x1 = hs.getThongTinChung();

            System.out.println("=====================================");
            System.out.println("MA_LK     : " + x1.getMaLk());
            System.out.println("HỌ TÊN    : " + x1.getHoTen());
            System.out.println("NGÀY VÀO  : " + x1.getNgayVao());
            System.out.println("NGÀY RA   : " + x1.getNgayRa());

            System.out.println("---- THUỐC ----");
            hs.getDsThuoc().forEach(t ->
                System.out.println("  - " + t.getTenThuoc() + " | SL: " + t.getSoLuong())
            );

            System.out.println("---- DỊCH VỤ ----");
            hs.getDsDichVu().forEach(dv ->
                System.out.println("  - " + dv.getTenDichVu())
            );

            System.out.println("---- CLS ----");
            hs.getDsCls().forEach(cls ->
                System.out.println("  - " + cls.getTenChiSo() + " = " + cls.getGiaTri())
            );
        }
    }
}