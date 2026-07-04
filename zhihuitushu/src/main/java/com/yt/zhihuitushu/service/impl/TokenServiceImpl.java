package com.yt.zhihuitushu.service.impl;

import com.yt.zhihuitushu.entity.LoginUser;
import com.yt.zhihuitushu.service.TokenService;
import com.yt.zhihuitushu.util.JwtUtil;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Service;

@Service
public class TokenServiceImpl implements TokenService {

    @Resource
    private JwtUtil jwtUtil;

    private static final String TOKEN_COOKIE_NAME = "token";

    @Override
    public String createToken(LoginUser user, boolean rememberMe) {
        return jwtUtil.generateToken(user.getId(), user.getUsername(), user.getRole(), rememberMe);
    }

    @Override
    public void setTokenCookie(HttpServletResponse response, String token, boolean rememberMe) {
        int maxAge = rememberMe ? 7 * 24 * 60 * 60 : 2 * 60 * 60;
        String cookieHeader = String.format("%s=%s; HttpOnly; Path=/; SameSite=Strict; Max-Age=%d",
            TOKEN_COOKIE_NAME, token, maxAge);
        response.addHeader("Set-Cookie", cookieHeader);
    }

    @Override
    public void clearTokenCookie(HttpServletResponse response) {
        response.addHeader("Set-Cookie", TOKEN_COOKIE_NAME + "=; HttpOnly; Path=/; SameSite=Strict; Max-Age=0");
    }

    @Override
    public String getTokenFromCookie(HttpServletRequest request) {
        String cookieHeader = request.getHeader("Cookie");
        if (cookieHeader == null) {
            return null;
        }
        String[] cookies = cookieHeader.split(";");
        for (String cookie : cookies) {
            cookie = cookie.trim();
            if (cookie.startsWith(TOKEN_COOKIE_NAME + "=")) {
                return cookie.substring(TOKEN_COOKIE_NAME.length() + 1);
            }
        }
        return null;
    }

    @Override
    public LoginUser parseToken(String token) {
        if (token == null || !jwtUtil.isTokenValid(token)) {
            return null;
        }
        LoginUser user = new LoginUser();
        user.setId(jwtUtil.getUserIdFromToken(token));
        user.setUsername(jwtUtil.getUsernameFromToken(token));
        user.setRole(jwtUtil.getRoleFromToken(token));
        return user;
    }
}