package com.imooc.article.controller;

import com.imooc.api.BaseController;
import com.imooc.api.controller.article.CommentsControllerApi;
import com.imooc.article.service.CommentsPortalService;
import com.imooc.grace.result.GraceJSONResult;
import com.imooc.pojo.bo.CommentReplyBO;
import com.imooc.pojo.bo.NewArticleBO;
import com.imooc.pojo.vo.AppUserVO;
import com.imooc.utils.PagedGridResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
public class CommentsController extends BaseController implements CommentsControllerApi {

    @Autowired
    private CommentsPortalService commentsPortalService;

    @Override
    public GraceJSONResult createComment(@Valid CommentReplyBO bo, BindingResult result) {

        // 1  判断错误
        if (result.hasErrors()){
            Map<String, String> errors = getErrors(result);
            return GraceJSONResult.errorMap(errors);
        }
        
        // 2 根据留言用户id查询他的昵称 
        // 用于存入数据表进行字段的冗余处理 
        // 从而避免多表查询
        String userId = bo.getCommentUserId();
        
        // 3 发起restTemplate请求
        Set<String> idSet = new HashSet<>();
        idSet.add(userId);
        String nickname = getPublisherList(idSet).get(0).getNickname();

        // 4 信息保存到数据库
        commentsPortalService.createComments(bo.getArticleId(),
                bo.getFatherId(),
                bo.getContent(),
                userId,
                nickname);

        return GraceJSONResult.ok();
    }

    @Override
    public GraceJSONResult commentCounts(String articleId) {
        Integer counts = getCountsFromRedis(REDIS_ARTICLE_COMMENT_COUNTS+":"+articleId);
        return GraceJSONResult.ok(counts);
    }

    @Override
    public GraceJSONResult list(String articleId, Integer page, Integer pageSize) {
        if (page == null)
            page = 1;
        if (pageSize == null)
            pageSize = 10;
        PagedGridResult gridResult = commentsPortalService.queryArticleComments(articleId, page, pageSize);
        return GraceJSONResult.ok(gridResult);
    }

}
