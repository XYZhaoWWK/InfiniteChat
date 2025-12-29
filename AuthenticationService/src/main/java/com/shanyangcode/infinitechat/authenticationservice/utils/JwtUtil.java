package com.shanyangcode.infinitechat.authenticationservice.utils;

import com.shanyangcode.infinitechat.authenticationservice.constants.common.TimeOutEnum;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.util.StringUtils;

import java.time.Duration;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import static com.shanyangcode.infinitechat.authenticationservice.constants.config.ConfigEnum.TOKEN_SECRET_KEY;

public class JwtUtil {

//  1.加密阶段，产生jwt

    private static final Duration expiration = Duration.ofHours(TimeOutEnum.JWT_TIME_OUT.getTimeOut());

    public static String generate(String userId) {
        Date expiryDate = new Date(System.currentTimeMillis() + expiration.toMillis());
        return Jwts.builder()
                .setSubject(userId)
                .setIssuedAt(new Date())
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, TOKEN_SECRET_KEY.getText())
                .compact();
    }

//  2.解密阶段，解析jwt

    public static Claims parse(String token) throws Exception {
        if (StringUtils.isEmpty(token)) {
            throw new Exception("token is empty");
        }
        Claims claims = null;
        claims = Jwts.parser()
                .setSigningKey(TOKEN_SECRET_KEY.getText())
                .parseClaimsJws(token)
                .getBody();

        return claims;
    }

}
