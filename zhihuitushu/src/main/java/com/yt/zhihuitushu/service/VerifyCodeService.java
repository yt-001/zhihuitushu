package com.yt.zhihuitushu.service;

public interface VerifyCodeService {

    String generateCode(String account);

    boolean verifyCode(String account, String code);

    boolean canSend(String account);

    void recordSend(String account);

    void sendCode(String account, String type);
}