package com.yt.zhihuitushu.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginUser {

    private Long id;
    private String username;
    private String avatar;
    private String role;
}