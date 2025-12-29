package com.shanyangcode.infinitechat.authenticationservice.service.impl;

import com.shanyangcode.infinitechat.authenticationservice.constants.common.SMSConstant;
import com.shanyangcode.infinitechat.authenticationservice.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.Duration;

import static com.shanyangcode.infinitechat.authenticationservice.constants.user.registerConstant.REGISTER_CODE;

@Service
public class EmailServiceImpl implements EmailService {

    @Autowired
    private JavaMailSender mailSender;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public void sendCode(String targetEmail) {
        String verifyCode = getRandomCode();
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("zhaoxy710@foxmail.com");
        message.setTo(targetEmail);
        message.setSubject("登录验证码");
        message.setText("您的验证码是：" + verifyCode + "，5分钟内有效。");
        stringRedisTemplate.opsForValue().set(
                REGISTER_CODE + targetEmail,
                String.valueOf(verifyCode),
                Duration.ofMinutes(SMSConstant.SMS_EXPIRE_TIME));
        mailSender.send(message);
    }

    public String getRandomCode() {
        return String.valueOf((int)((Math.random() * 9 + 1) * 100000));
    }
}
