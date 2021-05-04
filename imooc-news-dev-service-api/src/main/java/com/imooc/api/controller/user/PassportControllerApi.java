package com.imooc.api.controller.user;

import com.imooc.grace.result.GraceJSONResult;
import com.imooc.pojo.bo.RegisterLoginBO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;


@Api(value = "用户注册登录")
@RequestMapping("passport")
public interface PassportControllerApi {

    /**
     * 获得短信验证码
     * @return
     */
    @GetMapping("getSMSCode")
    @ApiOperation(value = "获得短信验证码",httpMethod = "GET")
    public GraceJSONResult getSMSCode(@RequestParam String mobile,
                                      HttpServletRequest request);

    @ApiOperation(value = "一键注册登录接口", notes = "一键注册登录接口", httpMethod = "POST")
    @PostMapping("/doLogin")
    public GraceJSONResult doLogin(@RequestBody @Valid RegisterLoginBO registerLoginBO,
                                   BindingResult result,
                                   HttpServletRequest request,
                                   HttpServletResponse response);

    /**
     * 退出登录
     * @return
     */
    @PostMapping("logout")
    @ApiOperation(value = "退出登录接口",httpMethod = "POST")
    public GraceJSONResult logout(@RequestParam String userId,
                                   HttpServletRequest request,
                                   HttpServletResponse response);
}
