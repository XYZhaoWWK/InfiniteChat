package com.shanyangcode.infinitechat.authenticationservice.exception;

import com.shanyangcode.infinitechat.authenticationservice.constants.user.ErrorEnum;
import lombok.Getter;

import static com.shanyangcode.infinitechat.authenticationservice.constants.user.ErrorEnum.SYSTEM_ERROR;

@Getter
public class CodeException extends RuntimeException {
    private final int code;

    public CodeException(String message) {
        super(message);
        this.code = SYSTEM_ERROR.getCode();
    }

    public CodeException(ErrorEnum errorEnum) {
        super(errorEnum.getMessage());
        this.code = errorEnum.getCode();
    }

    public CodeException(String message, ErrorEnum errorEnum) {
        super(message);
        this.code = errorEnum.getCode();
    }
}
