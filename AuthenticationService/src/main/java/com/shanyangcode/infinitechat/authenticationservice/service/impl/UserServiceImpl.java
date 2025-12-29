package com.shanyangcode.infinitechat.authenticationservice.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.Snowflake;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shanyangcode.infinitechat.authenticationservice.constants.user.ErrorEnum;
import com.shanyangcode.infinitechat.authenticationservice.constants.user.registerConstant;
import com.shanyangcode.infinitechat.authenticationservice.data.user.login.LoginRequest;
import com.shanyangcode.infinitechat.authenticationservice.data.user.login.LoginResponse;
import com.shanyangcode.infinitechat.authenticationservice.data.user.register.RegisterRequest;
import com.shanyangcode.infinitechat.authenticationservice.data.user.register.RegisterResponse;
import com.shanyangcode.infinitechat.authenticationservice.exception.CodeException;
import com.shanyangcode.infinitechat.authenticationservice.exception.DatabaseException;
import com.shanyangcode.infinitechat.authenticationservice.exception.LoginException;
import com.shanyangcode.infinitechat.authenticationservice.exception.UserException;
import com.shanyangcode.infinitechat.authenticationservice.model.User;
import com.shanyangcode.infinitechat.authenticationservice.service.UserService;
import com.shanyangcode.infinitechat.authenticationservice.mapper.UserMapper;
import com.shanyangcode.infinitechat.authenticationservice.utils.JwtUtil;
import com.shanyangcode.infinitechat.authenticationservice.utils.NicknameGeneratorUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import cn.hutool.core.util.IdUtil;
import org.springframework.util.DigestUtils;

/**
* @author zhaoxinyu
* @description 针对表【user(用户表)】的数据库操作Service实现
* @createDate 2025-12-23 16:38:36
*/
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService{

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private StringRedisTemplate redisTemplate;

    @Override
    public RegisterResponse register(RegisterRequest registerRequest) {
        String email = registerRequest.getEmail();
        String redisCode = redisTemplate.opsForValue().get(registerConstant.REGISTER_CODE + email);
        if (isRegistered(email)) {
            throw new UserException(ErrorEnum.REGISTER_ERROR);
        }
        if (redisCode == null || !redisCode.equals(registerRequest.getCode())) {
            throw new CodeException(ErrorEnum.CODE_ERROR);
        }
        Snowflake snowflake = IdUtil.getSnowflake(1, 1);
        String encryptedPassword = DigestUtils.md5DigestAsHex(registerRequest.getPassword().getBytes());
        User user = new User();
        user.setPhone(registerRequest.getPhone());
        user.setEmail(email);
        user.setPassword(encryptedPassword);
        user.setUserId(String.valueOf(snowflake.nextId()));
        user.setUserName(NicknameGeneratorUtil.generateNickname());
//        boolean save = this.save(user);
        int insert = userMapper.insert(user);
        if (insert != 1) {
            throw new DatabaseException(ErrorEnum.DATABASE_ERROR);
        }
        return new RegisterResponse().setEmail(email).setPhone(registerRequest.getPhone());
    }

    @Override
    public LoginResponse login(LoginRequest loginRequest) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("phone", loginRequest.getPhone());
        User one = this.getOnly(queryWrapper, true);
        String encryptedPassword = DigestUtils.md5DigestAsHex(loginRequest.getPassword().getBytes());
        if (one == null || !encryptedPassword.equals(one.getPassword())) {
            throw new LoginException(ErrorEnum.LOGIN_ERROR);
        }
        LoginResponse loginResponse = new LoginResponse();
        BeanUtil.copyProperties(one, loginResponse);
//        jwt令牌：json web token
//        header.payload.signature
        String jwt = JwtUtil.generate(one.getUserId());
        loginResponse.setToken(jwt);
        return loginResponse;
    }

    private boolean isRegistered(String email) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("email", email);
        long count = this.count(queryWrapper);
        return count > 0;
    }

}




