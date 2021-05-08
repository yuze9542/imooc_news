package com.imooc.article.service;

import com.imooc.pojo.Article;
import com.imooc.pojo.Category;
import com.imooc.pojo.bo.NewArticleBO;
import com.imooc.utils.PagedGridResult;

import java.util.Date;
import java.util.List;

public interface ArticleService {

    // 发布文章
    public void createArticle(NewArticleBO bo, Category category);

    // 更新定时发布为及时发布
    public void updateAppointToPublish();

    // 用户列表  --->   查询我的文章列表
    public PagedGridResult queryMyArticleList(String userId, String keyword, Integer status,
                                              Date startDate, Date endDate,
                                              Integer page, Integer pageSize);

    // 更改文章状态
    public void updateArticlesStatus(String articleId,Integer status);

    PagedGridResult queryAllList(Integer status, Integer page, Integer pageSize);

    void deleteArticle(String userId, String articleId);

    void withdrawArticle(String userId, String articleId);
}
