package com.yt.zhihuitushu.dto.response;

import com.yt.zhihuitushu.entity.LoginUser;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginResponse {

    private LoginUser user;
}