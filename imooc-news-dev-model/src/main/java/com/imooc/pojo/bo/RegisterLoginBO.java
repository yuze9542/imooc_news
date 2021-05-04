package com.imooc.pojo.bo;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class RegisterLoginBO {

    @NotNull(message = "手机号不能为空")
    private String mobile;
    @NotNull(message = "验证码不为空")
    private String smsCode;
}
