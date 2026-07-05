package com.yt.zhihuitushu.service;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * 异步邮件服务
 * 用于异步发送邮件，避免邮件发送超时影响前端响应
 */
@Slf4j
@Service
public class AsyncEmailService {

    @Resource
    private EmailService emailService;

    /**
     * 异步发送验证码邮件
     *
     * @param to    收件人邮箱
     * @param code  验证码
     */
    @Async
    public void sendVerificationCodeAsync(String to, String code) {
        try {
            emailService.sendVerificationCode(to, code);
            log.info("异步验证码邮件发送成功，收件人: {}", to);
        } catch (Exception e) {
            log.error("异步验证码邮件发送失败，收件人: {}", to, e);
        }
    }
}