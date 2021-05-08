package com.imooc.article.service;

import com.imooc.pojo.Category;
import com.imooc.pojo.bo.NewArticleBO;
import com.imooc.utils.PagedGridResult;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;

public interface CommentsPortalService {

    public void createComments(String articleId,
                               String fatherCommentId,
                               String content,
                               String userId,
                               String nickname,
                               String face);

    public PagedGridResult queryArticleComments(String articleId,
                                                Integer page, Integer pageSize);

    public PagedGridResult  queryCommentsByWriterId(String writerId, Integer page, Integer pageSize);

    public void deleteCommentByWriter(String writerId, String commentId);
}
