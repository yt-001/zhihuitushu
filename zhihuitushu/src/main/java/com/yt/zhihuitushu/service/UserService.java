package com.yt.zhihuitushu.service;

import com.yt.zhihuitushu.dto.request.LoginDTO;
import com.yt.zhihuitushu.dto.request.RegisterDTO;
import com.yt.zhihuitushu.dto.request.ResetPasswordDTO;
import com.yt.zhihuitushu.entity.LoginUser;
import com.yt.zhihuitushu.entity.User;

public interface UserService {

    User findByAccount(String account);

    boolean existsByAccount(String account);

    LoginUser register(RegisterDTO dto);

    LoginUser login(LoginDTO dto);

    void resetPassword(ResetPasswordDTO dto);
}