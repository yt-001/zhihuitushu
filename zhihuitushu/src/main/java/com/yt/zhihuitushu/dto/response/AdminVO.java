package com.yt.zhihuitushu.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdminVO {

    private Long id;

    private String username;

    private String realName;

    private String phone;

    private String email;

    private String avatar;

    private String role;

    private Integer status;
}