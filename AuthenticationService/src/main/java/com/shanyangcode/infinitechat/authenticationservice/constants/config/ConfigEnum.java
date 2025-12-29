package com.shanyangcode.infinitechat.authenticationservice.constants.config;

import lombok.Getter;

@Getter
public enum ConfigEnum {

    TOKEN_SECRET_KEY("tokenSecretKey", "goat");

    private final String key;
    private final String text;

    ConfigEnum(String key, String text) {
        this.key = key;
        this.text = text;
    }
}
