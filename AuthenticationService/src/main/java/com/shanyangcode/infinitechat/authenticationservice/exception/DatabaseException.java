package com.shanyangcode.infinitechat.authenticationservice.exception;

import com.shanyangcode.infinitechat.authenticationservice.constants.user.ErrorEnum;

import static com.shanyangcode.infinitechat.authenticationservice.constants.user.ErrorEnum.SYSTEM_ERROR;

public class DatabaseException extends RuntimeException {
    private final int code;

    public DatabaseException(String message) {
        super(message);
        this.code = SYSTEM_ERROR.getCode();
    }

    public DatabaseException(ErrorEnum errorEnum) {
        super(errorEnum.getMessage());
        this.code = errorEnum.getCode();
    }

    public DatabaseException(String message, ErrorEnum errorEnum) {
        super(message);
        this.code = errorEnum.getCode();
    }
}
