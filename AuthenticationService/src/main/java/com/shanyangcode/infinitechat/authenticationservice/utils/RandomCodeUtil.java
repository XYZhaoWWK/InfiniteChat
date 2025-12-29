package com.shanyangcode.infinitechat.authenticationservice.utils;

public class RandomCodeUtil {

    public String getRandomCode() {
        return String.valueOf((int)((Math.random() * 9 + 1) * 100000));
    }

}
