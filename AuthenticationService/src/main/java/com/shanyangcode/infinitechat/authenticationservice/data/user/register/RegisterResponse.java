package com.shanyangcode.infinitechat.authenticationservice.data.user.register;


import lombok.Data;
import lombok.experimental.Accessors;

@Data
//链式编程
@Accessors(chain = true)
public class RegisterResponse {

    private String phone;
    private String email;

}
