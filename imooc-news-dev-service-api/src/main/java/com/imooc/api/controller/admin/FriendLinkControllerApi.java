package com.imooc.api.controller.admin;

import com.imooc.grace.result.GraceJSONResult;
import com.imooc.pojo.bo.SaveFriendLinkBO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Api(value = "首页友情链接维护")
@RequestMapping("friendLinkMng")
public interface FriendLinkControllerApi {

    @ApiOperation(value = "新增修改友情链接")
    @PostMapping("saveOrUpdateFriendLink")
    // BindingResult可能是验证SaveFriendLinkBO里的那些东西的
    // 比如 @NotNULL 之类的??
    public GraceJSONResult saveOrUpdateFriendLink(@RequestBody @Valid SaveFriendLinkBO bo,
                                                  BindingResult result);

    @ApiOperation(value = "查询友情链接列表")
    @PostMapping("getFriendLinkList")
    public GraceJSONResult getFriendLinkList();

    @ApiOperation(value = "删除友情链接列表")
    @PostMapping("delete")
    public GraceJSONResult delete(@RequestParam String linkId);

    @ApiOperation(value = "首页查询友情链接列表")
    @GetMapping("portal/list")
    public GraceJSONResult queryPortalALlFriendList();

}
