package com.yt.zhihuitushu.controller;

import com.yt.zhihuitushu.dto.request.LoginDTO;
import com.yt.zhihuitushu.dto.request.RegisterDTO;
import com.yt.zhihuitushu.dto.request.ResetPasswordDTO;
import com.yt.zhihuitushu.dto.request.SendCodeDTO;
import com.yt.zhihuitushu.dto.response.ApiResponse;
import com.yt.zhihuitushu.dto.response.LoginResponse;
import com.yt.zhihuitushu.entity.LoginUser;
import com.yt.zhihuitushu.service.TokenService;
import com.yt.zhihuitushu.service.UserService;
import com.yt.zhihuitushu.service.VerifyCodeService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Resource
    private UserService userService;

    @Resource
    private VerifyCodeService verifyCodeService;

    @Resource
    private TokenService tokenService;

    @PostMapping("/send-code")
    public ApiResponse<Void> sendCode(@Valid @RequestBody SendCodeDTO dto) {
        verifyCodeService.sendCode(dto.getAccount(), dto.getType());
        return ApiResponse.success("验证码发送成功", null);
    }

    @PostMapping("/register")
    public ApiResponse<LoginResponse> register(@Valid @RequestBody RegisterDTO dto, HttpServletResponse response) {
        LoginUser user = userService.register(dto);
        
        String token = tokenService.createToken(user, false);
        tokenService.setTokenCookie(response, token, false);
        
        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setUser(user);
        return ApiResponse.success("注册成功", loginResponse);
    }

    @PostMapping("/login")
    public ApiResponse<LoginResponse> login(@Valid @RequestBody LoginDTO dto, HttpServletResponse response) {
        LoginUser user = userService.login(dto);
        
        String token = tokenService.createToken(user, dto.getRememberMe());
        tokenService.setTokenCookie(response, token, dto.getRememberMe());
        
        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setUser(user);
        return ApiResponse.success("登录成功", loginResponse);
    }

    @PostMapping("/reset-password")
    public ApiResponse<Void> resetPassword(@Valid @RequestBody ResetPasswordDTO dto) {
        userService.resetPassword(dto);
        return ApiResponse.success("密码修改成功，请重新登录", null);
    }

    @PostMapping("/logout")
    public ApiResponse<Void> logout(HttpServletResponse response) {
        tokenService.clearTokenCookie(response);
        return ApiResponse.success("退出成功", null);
    }

    @GetMapping("/me")
    public ApiResponse<LoginUser> getCurrentUser(@RequestAttribute(value = "loginUser", required = false) LoginUser loginUser) {
        if (loginUser == null) {
            return ApiResponse.error(401, "未登录");
        }
        return ApiResponse.success(loginUser);
    }
}