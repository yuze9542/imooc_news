package com.imooc.api.controller.user;

import com.imooc.api.config.MyServiceList;
import com.imooc.api.controller.user.fallback.UserControllerFactoryFallback;
import com.imooc.grace.result.GraceJSONResult;
import com.imooc.pojo.bo.UpdateUserInfoBO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;


@Api(value = "用户相关信息 ")
@RequestMapping("user")
// 服务端降级方法
@FeignClient(value = MyServiceList.SERVICE_USER,fallbackFactory = UserControllerFactoryFallback.class)
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
    public GraceJSONResult updateUserInfo(@RequestBody @Valid UpdateUserInfoBO updateUserInfoBO);

    /**
     * restTemplate
     * @return
     */
    @GetMapping("/getUserByIds")
    @ApiOperation(value = "查询用户的ids查询用户列表",httpMethod = "GET")
    public GraceJSONResult getUserByIds(@RequestParam String userIds);

}
