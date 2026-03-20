package com.checkxmlbv01.websitecheckxmlbv01;
import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import com.checkxmlbv01.websitecheckxmlbv01.domain.DichVuKyThuat;
import com.checkxmlbv01.websitecheckxmlbv01.domain.ErrorGroup.ErrorKCBDetail;
import com.checkxmlbv01.websitecheckxmlbv01.domain.ErrorGroup.ErrorKCBGroup;
import com.checkxmlbv01.websitecheckxmlbv01.domain.xml.xml1;
import com.checkxmlbv01.websitecheckxmlbv01.domain.xml.xml2;
import com.checkxmlbv01.websitecheckxmlbv01.domain.xml.xml3;
import com.checkxmlbv01.websitecheckxmlbv01.domain.xml.xml4;
import com.checkxmlbv01.websitecheckxmlbv01.repository.DichVuKyThuatRepository;
import com.checkxmlbv01.websitecheckxmlbv01.service.Import.ImportService;
import com.checkxmlbv01.websitecheckxmlbv01.service.ReadFileXML.HoSoKhamBenh;
import com.checkxmlbv01.websitecheckxmlbv01.service.validation.ThoiGianValidator;


@SpringBootApplication
public class WebsiteCheckXmlBv01Application {

    public static void main(String[] args) throws Exception {

        ConfigurableApplicationContext context =
                SpringApplication.run(WebsiteCheckXmlBv01Application.class, args);

        ImportService importService = context.getBean(ImportService.class);
        ThoiGianValidator validator = context.getBean(ThoiGianValidator.class);
        DichVuKyThuatRepository dvktRepo = context.getBean(DichVuKyThuatRepository.class);

        /* ======================================
           Load XML files
        ====================================== */

        MultipartFile xml1 = loadFile("src/main/resources/data_KCB_XML1/4750_xml1.xls");
        MultipartFile xml2 = loadFile("src/main/resources/data_KCB_XML1/4750_xml2.xls");
        MultipartFile xml3 = loadFile("src/main/resources/data_KCB_XML1/4750_xml3.xls");
        MultipartFile xml4 = loadFile("src/main/resources/data_KCB_XML1/4750_xml4.xls");

        /* ======================================
           Import hồ sơ
        ====================================== */

        importService.importHoSo(xml1, xml2, xml3, xml4);

        List<HoSoKhamBenh> list = importService.getAll();

        /* ======================================
           Load DVKT -> Map
        ====================================== */

        Map<String, DichVuKyThuat> dvktMap =
                dvktRepo.findAll()
                        .stream()
                        .collect(Collectors.toMap(
                                DichVuKyThuat::getMaDvkt,
                                dv -> dv
                        ));

        System.out.println("===== CHECK BV01 =====");

        int totalError = 0;

        /* ======================================
           Validate hồ sơ
        ====================================== */

        for (HoSoKhamBenh hs : list) {

            xml1 thongTin = hs.getThongTinChung();
            List<xml3> dsDv = hs.getDsDichVu();

            // ✅ THUỐC (XML2)
            List<xml2> dsThuoc = hs.getDsThuoc();

            Map<String, xml4> xml4Map =
                    hs.getDsCls()
                            .stream()
                            .collect(Collectors.toMap(
                                    kq -> kq.getMaLk() + "_" + kq.getMaDichVu(),
                                    kq -> kq,
                                    (a, b) -> a
                            ));

            ErrorKCBGroup group = new ErrorKCBGroup();

            // ✅ GỌI VALIDATE MỚI
            validator.validate(
                    thongTin,
                    dsDv,
                    dsThuoc,
                    dvktMap,
                    xml4Map,
                    group
            );

            if (!group.getErrors().isEmpty()) {

                System.out.println("\nHồ sơ lỗi: " + thongTin.getMaLk());

                for (ErrorKCBDetail e : group.getErrors()) {

                    System.out.println(
                            "DV: " + (e.getTenDichVu() != null ? e.getTenDichVu() : "THUỐC")
                                    + " | Lỗi: " + e.getErrorDetail()
                    );
                }

                // ✅ cộng tổng lỗi
                totalError += group.getErrors().size();
            }
        }

        System.out.println("\n===== END =====");
        System.out.println("Tổng lỗi: " + totalError);
    }

    /* ======================================
       Helper load file
    ====================================== */

    private static MultipartFile loadFile(String path) throws Exception {

        File f = new File(path);

        return new MockMultipartFile(
                f.getName(),
                new FileInputStream(f)
        );
    }
}