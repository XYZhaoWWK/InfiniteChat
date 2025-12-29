package com.shanyangcode.infinitechat.authenticationservice.constants.user;

import lombok.Getter;

@Getter
public enum ErrorEnum {

    REGISTER_ERROR(40001, "注册失败，用户已存在"),
    DATABASE_ERROR(40003, "数据库异常"),
    LOGIN_ERROR(40004, "登录异常"),
    CODE_ERROR(40002, "验证码错误"),
    SUCCESS(200, "ok"),
    SYSTEM_ERROR(50000, "系统内部异常");

    private final int code;
    private final String message;

    ErrorEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
