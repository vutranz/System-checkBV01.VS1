package com.checkxmlbv01.websitecheckxmlbv01.ultil.error.Exception;

import com.checkxmlbv01.websitecheckxmlbv01.ultil.error.BaseException;
import com.checkxmlbv01.websitecheckxmlbv01.ultil.error.ErrorCodes;

public class BusinessException extends BaseException {

    public BusinessException(ErrorCodes errorCodes) {
        super(errorCodes.getCode(), errorCodes.getMessage());
    }

    public BusinessException(String code, String message) {
        super(code, message);
    }
}

