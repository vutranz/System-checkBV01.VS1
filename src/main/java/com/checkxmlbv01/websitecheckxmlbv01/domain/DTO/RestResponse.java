package com.checkxmlbv01.websitecheckxmlbv01.domain.DTO;

import java.util.List;

import org.apache.poi.ss.formula.functions.T;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RestResponse<T> {

    private boolean success;
    private String message;
    private List<String> errors;
    private T data;

    public static <T> RestResponse<T> success(T data) {
        RestResponse<T> res = new RestResponse<>();
        res.success = true;
        res.data = data;
        return res;
    }

    public static <T> RestResponse<T> error(String message, List<String> errors) {
        RestResponse<T> res = new RestResponse<>();
        res.success = false;
        res.message = message;
        res.errors = errors;
        return res;
    }
}


