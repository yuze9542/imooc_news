package com.imooc.article.controller;

import com.imooc.api.BaseController;
import com.imooc.api.controller.article.ArticleControllerApi;
import com.imooc.article.service.ArticleService;
import com.imooc.enums.ArticleCoverType;
import com.imooc.enums.ArticleReviewStatus;
import com.imooc.enums.YesOrNo;
import com.imooc.grace.result.GraceJSONResult;
import com.imooc.grace.result.ResponseStatusEnum;
import com.imooc.pojo.Article;
import com.imooc.pojo.Category;
import com.imooc.pojo.bo.NewArticleBO;
import com.imooc.utils.JsonUtils;
import com.imooc.utils.PagedGridResult;
import io.swagger.models.auth.In;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
public class ArticleController extends BaseController implements ArticleControllerApi {

    @Autowired
    private ArticleService articleService;

    @Override
    public GraceJSONResult createArticle(@Valid NewArticleBO bo, BindingResult result) {

        // 1  判断错误
        if (result.hasErrors()){
            Map<String, String> errors = getErrors(result);
            return GraceJSONResult.errorMap(errors);
        }

        // 2 判断文章封面类型 单图必填 纯文字则设置空
        if (bo.getArticleType() == ArticleCoverType.ONE_IMAGE.type) {
            if (StringUtils.isBlank(bo.getArticleCover())){
                return GraceJSONResult.errorCustom(ResponseStatusEnum.ARTICLE_COVER_NOT_EXIST_ERROR);
            }
        }else if (bo.getArticleType() == ArticleCoverType.WORDS.type){
            // 纯文字不要封面
            bo.setArticleCover("");
        }
        Category t = null;
         // 3 判断分类id是否存在
        String allCatJson = redis.get(REDIS_ALL_CATEGORY);
        if (StringUtils.isBlank(allCatJson)){
            return GraceJSONResult.errorCustom(ResponseStatusEnum.SYSTEM_OPERATION_ERROR);
        }else {
            List<Category> categories = JsonUtils.jsonToList(allCatJson, Category.class);

            for (Category c:categories){
                if (c.getId() == bo.getCategoryId()){
                    t = c;
                    break;
                }
            }
            if (t == null){
                return GraceJSONResult.errorCustom(ResponseStatusEnum.CATEGORY_EXIST_ERROR);
            }
        }

        // 4
        articleService.createArticle(bo,t);
        return GraceJSONResult.ok();
    }

    @Override
    public GraceJSONResult queryMyList(String userId, String keyword, Integer status,
                                       Date startDate, Date endDate,
                                       Integer page, Integer pageSize) {
        if (StringUtils.isBlank(userId)){
            return GraceJSONResult.errorCustom(ResponseStatusEnum.ARTICLE_QUERY_PARAMS_ERROR);
        }
        if (page == null){
            page = 1;
        }
        if (pageSize == null){
            page = 10;
        }

        PagedGridResult gridResult = articleService.queryMyArticleList(userId, keyword, status, startDate, endDate, page, pageSize);


        return GraceJSONResult.ok(gridResult);
    }

    @Override
    public GraceJSONResult queryAllList(Integer status, Integer page, Integer pageSize) {

        if (page == null){
            page = 1;
        }

        if (pageSize == null){
            pageSize = 10;
        }
        PagedGridResult gridResult = articleService.queryAllList(status, page, pageSize);
        return GraceJSONResult.ok(gridResult);
    }

    @Override
    public GraceJSONResult doReview(String articleId, Integer passOrNot) {
        Integer pendingStatus = null;
        if (passOrNot == YesOrNo.YES.type){
            // 审核成功
            pendingStatus = ArticleReviewStatus.SUCCESS.type;
        }else if (passOrNot == YesOrNo.NO.type){
            // 审核不通过
            pendingStatus = ArticleReviewStatus.FAILED.type;
        }else {
            GraceJSONResult.errorCustom(ResponseStatusEnum.ARTICLE_REVIEW_ERROR);
        }

        articleService.updateArticlesStatus(articleId,pendingStatus);

        return GraceJSONResult.ok();
    }

    @Override
    public GraceJSONResult delete(String userId, String articleId) {
        articleService.deleteArticle(userId, articleId);
        return GraceJSONResult.ok();
    }

    @Override
    public GraceJSONResult withdraw(String userId, String articleId) {
        articleService.withdrawArticle(userId, articleId);
        return GraceJSONResult.ok();
    }


}
