package com.shanyangcode.infinitechat.authenticationservice.data.user.login;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class LoginRequest {

    private String phone;
    private String password;

}
