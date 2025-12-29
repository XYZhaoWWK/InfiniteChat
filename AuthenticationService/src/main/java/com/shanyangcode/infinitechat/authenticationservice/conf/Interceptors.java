package com.shanyangcode.infinitechat.authenticationservice.conf;

import io.jsonwebtoken.JwtHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class Interceptors implements WebMvcConfigurer {

    @Autowired
    private SourceHandler sourceHandler;

//    @Autowired
//    private JwtHandler jwtHandler;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(sourceHandler).addPathPatterns("/**");

    }

}
