package com.yt.zhihuitushu.service.impl;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dm.model.v20151123.SingleSendMailRequest;
import com.aliyuncs.dm.model.v20151123.SingleSendMailResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.yt.zhihuitushu.service.EmailService;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * 邮件服务实现类
 * 使用阿里云邮件推送服务
 */
@Slf4j
@Service
public class EmailServiceImpl implements EmailService {

    @Value("${aliyun.email.region-id}")
    private String regionId;

    @Value("${aliyun.email.access-key-id}")
    private String accessKeyId;

    @Value("${aliyun.email.access-key-secret}")
    private String accessKeySecret;

    @Value("${aliyun.email.account-name}")
    private String accountName;

    private IAcsClient client;

    @PostConstruct
    public void init() {
        try {
            IClientProfile profile = DefaultProfile.getProfile(regionId, accessKeyId, accessKeySecret);
            client = new DefaultAcsClient(profile);
            log.info("阿里云邮件推送客户端初始化成功");
        } catch (Exception e) {
            log.error("阿里云邮件推送客户端初始化失败", e);
            throw new IllegalStateException("阿里云邮件推送客户端初始化失败，无法启动邮件服务", e);
        }
    }

    @Override
    public void sendVerificationCode(String to, String code) {
        if (client == null) {
            throw new IllegalStateException("邮件服务客户端未初始化，请检查阿里云配置");
        }

        try {
            SingleSendMailRequest request = new SingleSendMailRequest();
            request.setMethod(MethodType.POST);
            request.setAccountName(accountName);
            request.setFromAlias("智慧图书");
            request.setAddressType(1);
            request.setTagName("验证码邮件");
            request.setReplyToAddress(true);
            request.setToAddress(to);
            request.setSubject("智慧图书 - 验证码");
            request.setHtmlBody("您的验证码是：<strong>" + code + "</strong>，有效期 5 分钟，请勿泄露给他人。");

            SingleSendMailResponse response = client.getAcsResponse(request);
            log.info("验证码邮件已发送至 {}，RequestId: {}", to, response.getRequestId());
        } catch (ClientException e) {
            log.error("发送验证码邮件失败，收件人: {}, 错误码: {}, 错误信息: {}",
                to, e.getErrCode(), e.getErrMsg(), e);
            throw new RuntimeException("发送邮件失败: " + e.getErrMsg(), e);
        }
    }
}