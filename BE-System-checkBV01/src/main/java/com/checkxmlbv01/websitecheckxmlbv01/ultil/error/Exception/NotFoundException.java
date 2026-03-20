package com.checkxmlbv01.websitecheckxmlbv01.ultil.error.Exception;

import com.checkxmlbv01.websitecheckxmlbv01.ultil.error.BaseException;

public class NotFoundException extends BaseException {

    public NotFoundException(String message) {
        super(message, "NOT_FOUND");
    }
}
