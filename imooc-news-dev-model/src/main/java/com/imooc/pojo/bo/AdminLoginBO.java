package com.imooc.pojo.bo;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 管理员登录的BO
 */
@Data
public class AdminLoginBO {
    @NotNull
    private String username;
    @NotNull
    private String password;
    private String img64;


}
