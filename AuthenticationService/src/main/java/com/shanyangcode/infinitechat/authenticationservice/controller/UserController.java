package com.shanyangcode.infinitechat.authenticationservice.controller;


import com.shanyangcode.infinitechat.authenticationservice.common.Result;
import com.shanyangcode.infinitechat.authenticationservice.data.user.login.LoginRequest;
import com.shanyangcode.infinitechat.authenticationservice.data.user.login.LoginResponse;
import com.shanyangcode.infinitechat.authenticationservice.data.user.register.RegisterRequest;
import com.shanyangcode.infinitechat.authenticationservice.data.user.register.RegisterResponse;
import com.shanyangcode.infinitechat.authenticationservice.model.User;
import com.shanyangcode.infinitechat.authenticationservice.service.EmailService;
import com.shanyangcode.infinitechat.authenticationservice.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("api/v1/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/hello")
    public String hello() {
        return "Hello World!";
    }

    @PostMapping("/register")
    public Result<RegisterResponse> register(@RequestBody RegisterRequest registerRequest) {
        return Result.OK(userService.register(registerRequest));
    }

    @PostMapping("/login")
    public Result<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        return Result.OK(userService.login(loginRequest));
    }

//    @PatchMapping("/avatar")
//    public Result<>头像更新接口没写

}
