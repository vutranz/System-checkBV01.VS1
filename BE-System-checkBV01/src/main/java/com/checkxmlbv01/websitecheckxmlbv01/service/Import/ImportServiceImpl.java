package com.checkxmlbv01.websitecheckxmlbv01.service.Import;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.checkxmlbv01.websitecheckxmlbv01.domain.DichVuKyThuat;
import com.checkxmlbv01.websitecheckxmlbv01.domain.ErrorGroup.ErrorKCBGroup;
import com.checkxmlbv01.websitecheckxmlbv01.domain.xml.xml1;
import com.checkxmlbv01.websitecheckxmlbv01.domain.xml.xml2;
import com.checkxmlbv01.websitecheckxmlbv01.domain.xml.xml3;
import com.checkxmlbv01.websitecheckxmlbv01.domain.xml.xml4;
import com.checkxmlbv01.websitecheckxmlbv01.repository.DichVuKyThuatRepository;
import com.checkxmlbv01.websitecheckxmlbv01.service.ReadFileXML.ExcelReader_XML1;
import com.checkxmlbv01.websitecheckxmlbv01.service.ReadFileXML.ExcelReader_XML2;
import com.checkxmlbv01.websitecheckxmlbv01.service.ReadFileXML.ExcelReader_XML3;
import com.checkxmlbv01.websitecheckxmlbv01.service.ReadFileXML.ExcelReader_XML4;
import com.checkxmlbv01.websitecheckxmlbv01.service.ReadFileXML.GenericExcelReader;
import com.checkxmlbv01.websitecheckxmlbv01.service.ReadFileXML.HoSoKhamBenh;
import com.checkxmlbv01.websitecheckxmlbv01.service.ReadFileXML.HoSoYTeMerger;
import com.checkxmlbv01.websitecheckxmlbv01.service.validation.ThoiGianValidator;

@Service
public class ImportServiceImpl implements ImportService {

    // 🔥 Lưu tạm trong RAM
    private List<HoSoKhamBenh> hoSoList = new ArrayList<>();

     private final ThoiGianValidator validator;
    private final ExcelExportService excelService;
    private final DichVuKyThuatRepository dvktRepo;

    public ImportServiceImpl(ThoiGianValidator validator,
                             ExcelExportService excelService,
                             DichVuKyThuatRepository dvktRepo) {
        this.validator = validator;
        this.excelService = excelService;
        this.dvktRepo = dvktRepo;
    }

    @Override
    public void importHoSo(
            MultipartFile xml1File,
            MultipartFile xml2File,
            MultipartFile xml3File,
            MultipartFile xml4File
    ) throws Exception {

       try {
        List<xml1> xml1List = GenericExcelReader.read(
                xml1File.getInputStream(),
                65,
                new ExcelReader_XML1()
        );

        List<xml2> xml2List = GenericExcelReader.read(
                xml2File.getInputStream(),
                19,
                new ExcelReader_XML2()
        );

        List<xml3> xml3List = GenericExcelReader.read(
                xml3File.getInputStream(),
                24,
                new ExcelReader_XML3()
        );

        List<xml4> xml4List = GenericExcelReader.read(
                xml4File.getInputStream(),
                29,
                new ExcelReader_XML4()
        );

        hoSoList = HoSoYTeMerger.merge(xml1List, xml2List, xml3List, xml4List);

    } catch (Exception e) {
        throw new RuntimeException("❌ File Excel không hợp lệ hoặc bị lỗi!");
    }
    }


    @Override
    public List<HoSoKhamBenh> getAll() {
        return hoSoList;
    }

    @Override
    public HoSoKhamBenh findByMaLk(String maLk) {
        return hoSoList.stream()
                .filter(hs -> hs.getThongTinChung() != null &&
                        maLk.equals(hs.getThongTinChung().getMaLk()))
                .findFirst()
                .orElse(null);
    }

     // ================= EXPORT EXCEL =================
    @Override
    public ByteArrayInputStream exportExcel() throws Exception {

        ErrorKCBGroup group = new ErrorKCBGroup();

        // 👉 load DVKT 1 lần (tối ưu)
        Map<String, DichVuKyThuat> dvktMap =
                dvktRepo.findAll()
                        .stream()
                        .collect(Collectors.toMap(
                                DichVuKyThuat::getMaDvkt,
                                dv -> dv
                        ));

        for (HoSoKhamBenh hs : hoSoList) {

            xml1 thongTin = hs.getThongTinChung();
            List<xml3> dsDv = hs.getDsDichVu();
            List<xml2> dsThuoc = hs.getDsThuoc();

            // 👉 map xml4
           List<xml4> dsXml4 = Optional.ofNullable(hs.getDsCls())
                .orElse(Collections.emptyList());

        Map<String, xml4> xml4Map = dsXml4.stream()
                .collect(Collectors.toMap(
                        x -> thongTin.getMaLk() + "_" + x.getMaDichVu(),
                        x -> x,
                        (a, b) -> a
                ));

            // 👉 gọi validator của bạn (KHÔNG SỬA)
            validator.validate(
                    thongTin,
                    dsDv,
                    dsThuoc,
                    dvktMap,
                    xml4Map,
                    group
            );
        }

        // 👉 xuất excel
        return excelService.export(group);
    }
}
