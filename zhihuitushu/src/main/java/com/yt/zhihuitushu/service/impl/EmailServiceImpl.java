package com.yt.zhihuitushu.service.impl;

import com.yt.zhihuitushu.service.EmailService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

/**
 * 邮件服务实现类
 */
@Slf4j
@Service
public class EmailServiceImpl implements EmailService {

    @Resource
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String from;

    @Override
    public void sendVerificationCode(String to, String code) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setTo(to);
        message.setSubject("智慧图书 - 验证码");
        message.setText("您的验证码是：" + code + "，有效期 5 分钟，请勿泄露给他人。");

        mailSender.send(message);
        log.info("验证码邮件已发送至 {}", to);
    }
}
