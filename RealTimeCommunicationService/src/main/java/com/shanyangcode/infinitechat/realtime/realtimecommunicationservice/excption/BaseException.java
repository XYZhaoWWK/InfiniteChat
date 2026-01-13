package com.shanyangcode.infinitechat.realtime.realtimecommunicationservice.excption;
public class BaseException extends RuntimeException{
    public BaseException(){}

    public BaseException(String msg){
        super(msg);
    }
}