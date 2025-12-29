package com.shanyangcode.infinitechat.authenticationservice.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.shanyangcode.infinitechat.authenticationservice.data.user.login.LoginRequest;
import com.shanyangcode.infinitechat.authenticationservice.data.user.login.LoginResponse;
import com.shanyangcode.infinitechat.authenticationservice.data.user.register.RegisterRequest;
import com.shanyangcode.infinitechat.authenticationservice.data.user.register.RegisterResponse;
import com.shanyangcode.infinitechat.authenticationservice.model.User;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author zhaoxinyu
* @description 针对表【user(用户表)】的数据库操作Service
* @createDate 2025-12-23 16:38:36
*/
public interface UserService extends IService<User> {

    default User getOnly(QueryWrapper<User> queryWrapper, boolean throwEx) {
        queryWrapper.last("limit 1");
        return this.getOne(queryWrapper, throwEx);
    }

    RegisterResponse register(RegisterRequest registerRequest);

    LoginResponse login(LoginRequest loginRequest);

}
