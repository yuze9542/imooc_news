package com.imooc.api.controller.user;

import com.imooc.grace.result.GraceJSONResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Api(value = "粉丝管理")
@RequestMapping("fans")
public interface MyFansControllerApi {

    @PostMapping("isMeFollowThisWriter")
    @ApiOperation(value = "查询当前用户是否关注作家", httpMethod = "POST")
    public GraceJSONResult isMeFollowThisWriter(@RequestParam String writerId,
                                                @RequestParam String fanId);

    @PostMapping("follow")
    @ApiOperation(value = "用户关注作家,成为粉丝", httpMethod = "POST")
    public GraceJSONResult follow(@RequestParam String writerId,
                                  @RequestParam String fanId);

    @PostMapping("unfollow")
    @ApiOperation(value = "用户取消关注作家,", httpMethod = "POST")
    public GraceJSONResult unfollow(@RequestParam String writerId,
                                    @RequestParam String fanId);

    @ApiOperation(value = "查询所有粉丝列表",httpMethod = "POST")
    @PostMapping("/queryAll")
    public GraceJSONResult queryAll(
            @RequestParam String writerId,
            @ApiParam(name = "page", value = "查询下一页的第几页",required = false)
            @RequestParam Integer page,
            @ApiParam(name = "pageSize", value = "分页查询每一页显示的条数",required = false)
            @RequestParam Integer pageSize );


    @PostMapping("queryRatio")
    @ApiOperation(value = "查询男女粉丝数量,", httpMethod = "POST")
    public GraceJSONResult queryRatio(@RequestParam String writerId);

    @PostMapping("queryRatioByRegion")
    @ApiOperation(value = "查询男女粉丝数量,", httpMethod = "POST")
    public GraceJSONResult queryRatioByRegion(@RequestParam String writerId);
}
