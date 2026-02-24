package com.checkxmlbv01.websitecheckxmlbv01.ultil.error.Exception;

import com.checkxmlbv01.websitecheckxmlbv01.ultil.error.BaseException;

public class CoSoAlreadyExistsException extends BaseException {
    public CoSoAlreadyExistsException() {
        super("CO_SO_DUPLICATE", "Mã cơ sở đã tồn tại");
    }
}


