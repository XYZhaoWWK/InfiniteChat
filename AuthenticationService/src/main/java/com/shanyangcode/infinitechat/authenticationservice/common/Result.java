package com.shanyangcode.infinitechat.authenticationservice.common;


import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.http.HttpStatus;

@Data
@Accessors(chain = true)
public class Result<T> {

    private int code;

    private String msg;

    private T data;

    public static <T> Result<T> OK(T data) {
        Result<T> objectResult = new Result<>();
        return objectResult.setCode(HttpStatus.OK.value()).setData(data);
    }

    public static <T> Result<T> DatabaseError(String message) {
        Result<T> objectResult = new Result<>();
        return objectResult.setCode(HttpStatus.INTERNAL_SERVER_ERROR.value()).setMsg(message);
    }

    public static <T> Result<T> ServerError(String message) {
        Result<T> objectResult = new Result<>();
        return objectResult.setCode(HttpStatus.INTERNAL_SERVER_ERROR.value()).setMsg(message);
    }

    public static <T> Result<T> UserError(int code, String message) {
        Result<T> objectResult = new Result<>();
        return objectResult.setCode(code).setMsg(message);
    }
}
