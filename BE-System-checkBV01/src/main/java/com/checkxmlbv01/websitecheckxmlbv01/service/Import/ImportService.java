package com.checkxmlbv01.websitecheckxmlbv01.service.Import;

import java.io.ByteArrayInputStream;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.checkxmlbv01.websitecheckxmlbv01.service.ReadFileXML.HoSoKhamBenh;

public interface ImportService {

    void importHoSo(
            MultipartFile xml1File,
            MultipartFile xml2File,
            MultipartFile xml3File,
            MultipartFile xml4File
    ) throws Exception;

    List<HoSoKhamBenh> getAll();

    HoSoKhamBenh findByMaLk(String maLk);
    
    ByteArrayInputStream exportExcel() throws Exception;
}
