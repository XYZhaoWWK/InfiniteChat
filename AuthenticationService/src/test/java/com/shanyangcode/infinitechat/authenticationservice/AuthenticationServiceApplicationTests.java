package com.shanyangcode.infinitechat.authenticationservice;

import com.shanyangcode.infinitechat.authenticationservice.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class AuthenticationServiceApplicationTests {

    @Autowired
    private UserService userService;

    @Test
    void contextLoads() {
    }

    @Test
    void testUserService() {
        userService.getById(1);
        System.out.println(userService.getById(1));
    }

}
