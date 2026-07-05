package com.yt.zhihuitushu.service.impl;

import com.yt.zhihuitushu.service.EmailService;
import com.yt.zhihuitushu.service.RedisService;
import com.yt.zhihuitushu.service.VerifyCodeService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

@Slf4j
@Service
public class VerifyCodeServiceImpl implements VerifyCodeService {

    @Resource
    private RedisService redisService;

    @Resource
    private EmailService emailService;

    private static final String VERIFY_CODE_PREFIX = "verify_code:";
    private static final String VERIFY_CODE_SEND_PREFIX = "verify_code_send:";
    private static final int CODE_LENGTH = 6;
    private static final int CODE_EXPIRE_MINUTES = 5;
    private static final int SEND_COOLDOWN_SECONDS = 60;

    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");

    @Override
    public String generateCode(String account) {
        StringBuilder code = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < CODE_LENGTH; i++) {
            code.append(random.nextInt(10));
        }
        String codeValue = code.toString();
        redisService.set(VERIFY_CODE_PREFIX + account, codeValue, CODE_EXPIRE_MINUTES, TimeUnit.MINUTES);
        return codeValue;
    }

    @Override
    public boolean verifyCode(String account, String code) {
        String storedCode = redisService.get(VERIFY_CODE_PREFIX + account);
        if (storedCode == null) {
            return false;
        }
        boolean verified = storedCode.equals(code);
        if (verified) {
            redisService.delete(VERIFY_CODE_PREFIX + account);
        }
        return verified;
    }

    @Override
    public boolean canSend(String account) {
        String key = VERIFY_CODE_SEND_PREFIX + account;
        return !redisService.exists(key);
    }

    @Override
    public void recordSend(String account) {
        redisService.set(VERIFY_CODE_SEND_PREFIX + account, "1", SEND_COOLDOWN_SECONDS, TimeUnit.SECONDS);
    }

    @Override
    public void sendCode(String account, String type) {
        if (!canSend(account)) {
            throw new RuntimeException("发送频率过高，请稍后再试");
        }
        String code = generateCode(account);
        recordSend(account);

        // 当 type 为 email 且账号为邮箱格式时，真实发送邮件
        if ("email".equalsIgnoreCase(type) && EMAIL_PATTERN.matcher(account).matches()) {
            try {
                emailService.sendVerificationCode(account, code);
            } catch (Exception e) {
                log.error("发送验证码邮件到 {} 失败", account, e);
                // 发送失败时清除已生成的验证码，避免用户收不到码但系统认为已发送
                redisService.delete(VERIFY_CODE_PREFIX + account);
                throw new RuntimeException("验证码邮件发送失败，请稍后重试");
            }
        }
    }
}
