package com.shanyangcode.infinitechat.authenticationservice.exception;

import com.shanyangcode.infinitechat.authenticationservice.constants.user.ErrorEnum;
import lombok.Getter;

import static com.shanyangcode.infinitechat.authenticationservice.constants.user.ErrorEnum.SYSTEM_ERROR;

@Getter
public class UserException extends RuntimeException {

    private final int code;

    public UserException(String message) {
        super(message);
        this.code = SYSTEM_ERROR.getCode();
    }

    public UserException(ErrorEnum errorEnum) {
        super(errorEnum.getMessage());
        this.code = errorEnum.getCode();
    }

    public UserException(String message, ErrorEnum errorEnum) {
        super(message);
        this.code = errorEnum.getCode();
    }

}
