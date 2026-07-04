package com.yt.zhihuitushu.service;

import com.yt.zhihuitushu.entity.LoginUser;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface TokenService {

    String createToken(LoginUser user, boolean rememberMe);

    void setTokenCookie(HttpServletResponse response, String token, boolean rememberMe);

    void clearTokenCookie(HttpServletResponse response);

    String getTokenFromCookie(HttpServletRequest request);

    LoginUser parseToken(String token);
}