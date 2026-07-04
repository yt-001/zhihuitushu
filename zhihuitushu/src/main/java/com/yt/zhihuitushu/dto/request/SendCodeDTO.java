package com.yt.zhihuitushu.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SendCodeDTO {

    @NotBlank(message = "账号不能为空")
    private String account;

    @NotBlank(message = "类型不能为空")
    private String type;
}