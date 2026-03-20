package com.checkxmlbv01.websitecheckxmlbv01.domain.ErrorGroup;

import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErrorKCBGroup {

    private String maLk;

    private List<ErrorKCBDetail> errors = new ArrayList<>();

    public ErrorKCBGroup(String maLk) {
        this.maLk = maLk;
    }

    public void addError(ErrorKCBDetail error) {

        if (error != null
                && error.getErrorDetail() != null
                && !error.getErrorDetail().isBlank()) {

            this.errors.add(error);
        }
    }
}
