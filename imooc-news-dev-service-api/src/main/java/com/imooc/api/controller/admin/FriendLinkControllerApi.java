package com.imooc.api.controller.admin;

import com.imooc.grace.result.GraceJSONResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Api(value = "首页友情链接维护")
@RequestMapping("")
public interface FriendLinkControllerApi {

    @ApiOperation(value = "新增修改友情链接")
    @PostMapping("")
    public GraceJSONResult save();

}
