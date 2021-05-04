package com.imooc.pojo.bo;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 添加管理人员的BO
 */
@Data
public class NewAdminBO {
    @NotNull
    private String username;
    @NotNull
    private String adminName;
    @NotNull
    private String password;
    private String confirmPassword;
    private String img64;
    private String faceId;


}
