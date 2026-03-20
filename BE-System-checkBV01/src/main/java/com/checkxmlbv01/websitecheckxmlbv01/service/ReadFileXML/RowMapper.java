package com.checkxmlbv01.websitecheckxmlbv01.service.ReadFileXML;

import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;

public interface RowMapper<T> {
    T map(Row row, DataFormatter formatter);
}
