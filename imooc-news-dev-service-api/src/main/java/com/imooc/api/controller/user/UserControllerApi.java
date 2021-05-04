package com.imooc.api.controller.user;

import com.imooc.grace.result.GraceJSONResult;
import com.imooc.pojo.bo.UpdateUserInfoBO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;


@Api(value = "用户相关信息 ")
@RequestMapping("user")
public interface UserControllerApi {

    /**
     * 获得用户基本信息
     * @return
     */
    @PostMapping("/getUserInfo")
    @ApiOperation(value = "用户基本信息",httpMethod = "POST")
    public GraceJSONResult getUserInfo(@RequestParam String userId);

    /**
     * 获得用户账户信息
     * @return
     */
    @PostMapping("/getAccountInfo")
    @ApiOperation(value = "用户相关信息",httpMethod = "POST")
    public GraceJSONResult getAccountInfo(@RequestParam String userId);

    /**
     * 获得短信验证码
     * @return
     */
    @PostMapping("/updateUserInfo")
    @ApiOperation(value = "完善用户信息",httpMethod = "POST")
    public GraceJSONResult updateUserInfo(@RequestBody @Valid UpdateUserInfoBO updateUserInfoBO,
                                          BindingResult result);

}
