package com.imooc.api.controller.article;

import com.imooc.grace.result.GraceJSONResult;
import com.imooc.pojo.bo.CommentReplyBO;
import com.imooc.pojo.bo.NewArticleBO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Date;

@Api(value = "评论相关业务的Controller")
@RequestMapping("comment")
public interface CommentsControllerApi {

    @PostMapping("/createComment")
    @ApiOperation(value = "用户发表评论", notes = "用户发表评论", httpMethod = "POST")
    public GraceJSONResult createComment(@RequestBody @Valid CommentReplyBO bo,
                                         BindingResult result);

    @GetMapping("/commentCounts")
    @ApiOperation(value = "用户评论数", notes = "用户发表评论", httpMethod = "GET")
    public GraceJSONResult commentCounts(@RequestParam String articleId);

    @GetMapping("/list")
    @ApiOperation(value = "查询文章所有评论页表", notes = "用户发表评论", httpMethod = "GET")
    public GraceJSONResult list(@RequestParam String articleId,
                                @RequestParam Integer page,
                                @RequestParam Integer pageSize);

}
