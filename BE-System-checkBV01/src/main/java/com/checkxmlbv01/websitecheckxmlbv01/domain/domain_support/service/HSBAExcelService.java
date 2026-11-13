package com.checkxmlbv01.websitecheckxmlbv01.domain.domain_support.service;

import org.apache.poi.ss.usermodel.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
public class HSBAExcelService {

    // ==========================================
    // 1. ĐỌC FILE WINSOFT (Hỗ trợ InputStream / MultipartFile / File Path)
    // ==========================================

    public List<com.checkxmlbv01.websitecheckxmlbv01.domain.domain_support.listHSBA_winSoft> readWinSoftExcel(InputStream inputStream) {
        List<com.checkxmlbv01.websitecheckxmlbv01.domain.domain_support.listHSBA_winSoft> list = new ArrayList<>();
        try (Workbook workbook = WorkbookFactory.create(inputStream)) {

            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rowIterator = sheet.iterator();

            // Tự động nhận diện index cột dựa trên tên tiêu đề (Header)
            int idxMaLk = 22;   
            int idxMaLkt = 24;  

            if (rowIterator.hasNext()) {
                Row headerRow = rowIterator.next();
                System.out.println("===== HEADER FILE WINSOFT =====");
                for (Cell cell : headerRow) {
                    String colName = getCellValueAsString(cell);
                    int colIdx = cell.getColumnIndex();
                    System.out.println("Cột [" + colIdx + "] : '" + colName + "'");

                    String upperName = colName.toUpperCase().replaceAll("[^A-Z0-9_]", "");
                    if (upperName.equals("MA_LK") || upperName.equals("MALK")) {
                        idxMaLk = colIdx;
                    } else if (upperName.equals("MALKT") || upperName.equals("MA_LKT")) {
                        idxMaLkt = colIdx;
                    }
                }
                System.out.println("-> Nhận diện tự động: MA_LK ở cột [" + idxMaLk + "], MaLKT ở cột [" + idxMaLkt + "]");
                System.out.println("===============================");
            }

            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                com.checkxmlbv01.websitecheckxmlbv01.domain.domain_support.listHSBA_winSoft item = new com.checkxmlbv01.websitecheckxmlbv01.domain.domain_support.listHSBA_winSoft();

                item.setMaBn(getCellValueAsString(row.getCell(0)));
                item.setTenBenhNhan(getCellValueAsString(row.getCell(1)));
                item.setNgayVao(getCellValueAsString(row.getCell(2)));
                item.setNgayKb(getCellValueAsString(row.getCell(3)));
                item.setNgayRa(getCellValueAsString(row.getCell(4)));
                item.setSoThe(getCellValueAsString(row.getCell(5)));
                item.setThanhTien(parseBigDecimal(getCellValueAsString(row.getCell(6))));
                item.setBhytTra(parseBigDecimal(getCellValueAsString(row.getCell(7))));
                item.setThuPhi(parseBigDecimal(getCellValueAsString(row.getCell(8))));
                item.setPhuThu(parseBigDecimal(getCellValueAsString(row.getCell(9))));
                item.setMienGiam(parseBigDecimal(getCellValueAsString(row.getCell(10))));
                item.setThucThu(parseBigDecimal(getCellValueAsString(row.getCell(11))));
                item.setBacSy(getCellValueAsString(row.getCell(12)));
                item.setCdBanDau(getCellValueAsString(row.getCell(13)));
                item.setMaTenIcd10(getCellValueAsString(row.getCell(14)));
                item.setMaTenIcd10Kt(getCellValueAsString(row.getCell(15)));
                item.setUserCapNhat(getCellValueAsString(row.getCell(16)));
                item.setNgayLamHs(getCellValueAsString(row.getCell(17)));
                item.setNgayCapNhatHsKb(getCellValueAsString(row.getCell(18)));
                item.setKhoa(getCellValueAsString(row.getCell(19)));
                item.setReportId(getCellValueAsString(row.getCell(20)));
                item.setMaKpXml(getCellValueAsString(row.getCell(21)));
                
                item.setMaLk(getCellValueAsString(row.getCell(idxMaLk)));
                item.setUpd(getCellValueAsString(row.getCell(23)));
                item.setMaLkt(getCellValueAsString(row.getCell(idxMaLkt)));
                item.setTrang(getCellValueAsString(row.getCell(25)));

                list.add(item);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<com.checkxmlbv01.websitecheckxmlbv01.domain.domain_support.listHSBA_winSoft> readWinSoftExcel(String filePath) {
        try (FileInputStream fis = new FileInputStream(new File(filePath))) {
            return readWinSoftExcel(fis);
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public List<com.checkxmlbv01.websitecheckxmlbv01.domain.domain_support.listHSBA_winSoft> readWinSoftExcel(MultipartFile file) {
        try (InputStream is = file.getInputStream()) {
            return readWinSoftExcel(is);
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }


    // ==========================================
    // 2. ĐỌC FILE GATE BHYT
    // ==========================================

    public List<com.checkxmlbv01.websitecheckxmlbv01.domain.domain_support.listHSBA_gateBHYT> readGateBHYTExcel(InputStream inputStream) {
        List<com.checkxmlbv01.websitecheckxmlbv01.domain.domain_support.listHSBA_gateBHYT> list = new ArrayList<>();
        try (Workbook workbook = WorkbookFactory.create(inputStream)) {

            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rowIterator = sheet.iterator();

            if (rowIterator.hasNext()) {
                rowIterator.next(); // Bỏ qua tiêu đề
            }

            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                com.checkxmlbv01.websitecheckxmlbv01.domain.domain_support.listHSBA_gateBHYT item = new com.checkxmlbv01.websitecheckxmlbv01.domain.domain_support.listHSBA_gateBHYT();

                String sttStr = getCellValueAsString(row.getCell(0));
                if (!sttStr.isEmpty()) {
                    try {
                        item.setStt((int) Double.parseDouble(sttStr));
                    } catch (NumberFormatException e) {
                        item.setStt(null);
                    }
                }
                
                item.setMaLienKet(getCellValueAsString(row.getCell(1)));
                item.setMaThe(getCellValueAsString(row.getCell(2)));
                item.setMaBn(getCellValueAsString(row.getCell(3)));
                item.setHoTen(getCellValueAsString(row.getCell(4)));
                item.setNgaySinh(getCellValueAsString(row.getCell(5)));
                item.setGioiTinh(getCellValueAsString(row.getCell(6)));
                item.setNgayVao(getCellValueAsString(row.getCell(7)));
                item.setNgayRa(getCellValueAsString(row.getCell(8)));
                item.setChanDoan(getCellValueAsString(row.getCell(9)));
                item.setTongChi(parseBigDecimal(getCellValueAsString(row.getCell(10))));
                item.setBenhNhanTt(parseBigDecimal(getCellValueAsString(row.getCell(11))));
                item.setBenhNhanCct(parseBigDecimal(getCellValueAsString(row.getCell(12))));
                item.setBaoHiemTt(parseBigDecimal(getCellValueAsString(row.getCell(13))));
                item.setNgayTt(getCellValueAsString(row.getCell(14)));
                item.setNgayGuiHs(getCellValueAsString(row.getCell(15)));
                item.setNgayDeNghiTt(getCellValueAsString(row.getCell(16)));
                item.setTrangThaiHs(getCellValueAsString(row.getCell(17)));
                item.setTrangThaiTt(getCellValueAsString(row.getCell(18)));
                item.setLoaiHs(getCellValueAsString(row.getCell(19)));
                item.setMaLoi(getCellValueAsString(row.getCell(20)));
                item.setMieuTa(getCellValueAsString(row.getCell(21)));

                list.add(item);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<com.checkxmlbv01.websitecheckxmlbv01.domain.domain_support.listHSBA_gateBHYT> readGateBHYTExcel(String filePath) {
        try (FileInputStream fis = new FileInputStream(new File(filePath))) {
            return readGateBHYTExcel(fis);
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public List<com.checkxmlbv01.websitecheckxmlbv01.domain.domain_support.listHSBA_gateBHYT> readGateBHYTExcel(MultipartFile file) {
        try (InputStream is = file.getInputStream()) {
            return readGateBHYTExcel(is);
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }


    // ==========================================
    // 3. HÀM HỖ TRỢ ĐỌC Ô AN TOÀN
    // ==========================================

    private String getCellValueAsString(Cell cell) {
        if (cell == null) {
            return "";
        }
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue().trim();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getDateCellValue().toString();
                } else {
                    double val = cell.getNumericCellValue();
                    if (val == (long) val) {
                        return String.format("%d", (long) val);
                    }
                    return String.valueOf(val);
                }
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                return cell.getCellFormula();
            default:
                return "";
        }
    }

    private BigDecimal parseBigDecimal(String val) {
        if (val == null || val.isEmpty()) {
            return BigDecimal.ZERO;
        }
        try {
            String cleanVal = val.replace(",", "").trim();
            return new BigDecimal(cleanVal);
        } catch (NumberFormatException e) {
            return BigDecimal.ZERO;
        }
    }
}