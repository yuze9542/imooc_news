package com.imooc.article.service.Impl;

import com.github.pagehelper.PageHelper;
import com.imooc.api.BaseService;
import com.imooc.article.mapper.ArticleMapper;
import com.imooc.article.mapper.ArticleMapperCustom;
import com.imooc.article.service.ArticleService;
import com.imooc.enums.ArticleAppointType;
import com.imooc.enums.ArticleReviewStatus;
import com.imooc.enums.YesOrNo;
import com.imooc.exception.GraceException;
import com.imooc.grace.result.ResponseStatusEnum;
import com.imooc.pojo.Article;
import com.imooc.pojo.Category;
import com.imooc.pojo.bo.NewArticleBO;
import com.imooc.utils.PagedGridResult;
import org.apache.commons.lang3.StringUtils;
import org.n3r.idworker.Sid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.List;

@Service
public class ArticleServiceImpl extends BaseService implements ArticleService {

    @Autowired
    private ArticleMapper articleMapper;
    @Autowired
    private ArticleMapperCustom articleMapperCustom;

    @Autowired
    private Sid sid;

    @Override
    @Transactional
    public void createArticle(NewArticleBO bo, Category category) {
        String articleId = sid.nextShort();     // 随机设置文章id
        Article article = new Article();
        BeanUtils.copyProperties(bo, article);
        article.setId(articleId);
        article.setCommentCounts(0);

        article.setCategoryId(category.getId());
        article.setArticleStatus(ArticleReviewStatus.REVIEWING.type);
        article.setReadCounts(0);
        article.setIsDelete(YesOrNo.NO.type);
        article.setCreateTime(new Date());
        article.setUpdateTime(new Date());

        if (article.getIsAppoint() == ArticleAppointType.TIMING.type) {// 定时发布
            article.setPublishTime(bo.getPublishTime());
        } else if (article.getIsAppoint() == ArticleAppointType.IMMEDIATELY.type) {
            article.setPublishTime(new Date());
        }

        int insert = articleMapper.insert(article);
        if (insert!=1){
            GraceException.display(ResponseStatusEnum.ARTICLE_CREATE_ERROR);
        }

        //TODO 通过阿里云服务智能实现对文本检测
        // 智能检测通过则标记状态为通过 不通过则需要人工审核
        // 此处直接不通过
        if(true){
            // 不通过
            this.updateArticlesStatus(articleId,ArticleReviewStatus.WAITING_MANUAL.type);
        }else {
            // 通过
            this.updateArticlesStatus(articleId,ArticleReviewStatus.SUCCESS.type);
        }

    }

    @Override
    @Transactional
    public void updateAppointToPublish() {

        articleMapperCustom.updateAppointToPublish();
    }

    @Override
    public PagedGridResult queryMyArticleList(String userId, String keyword, Integer status, Date startDate, Date endDate, Integer page, Integer pageSize) {

        Example example = new Example(Article.class);
        example.orderBy("createTime").desc();
        Example.Criteria criteria = example.createCriteria();

        if (StringUtils.isNotBlank(keyword)){
            // 模糊查询
            criteria.andLike("title","%"+keyword+"%");
        }
        criteria.andEqualTo("publishUserId",userId);
        if (ArticleReviewStatus.isArticleStatusValid(status)){
            criteria.andEqualTo("articleStatus",status);
        }
        if (status != null && status == 12){
            criteria.andEqualTo("articleStatus",ArticleReviewStatus.REVIEWING.type)
                    .orEqualTo("articleStatus",ArticleReviewStatus.WAITING_MANUAL.type);
        }

        criteria.andEqualTo("isDelete",YesOrNo.NO.type);

        if (startDate!=null){
            criteria.andGreaterThanOrEqualTo("publishTime",startDate);
        }
        if (endDate!=null){
            criteria.andLessThanOrEqualTo("publishTime",endDate);
        }

        PageHelper.startPage(page,pageSize);

        List<Article> articles = articleMapper.selectByExample(example);
        return setterPagedGrid(articles,page);
    }

    @Override
    @Transactional
    public void updateArticlesStatus(String articleId, Integer status) {
        Example example = new Example(Article.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("id",articleId);
        Article updateArticle = new Article();
        updateArticle.setArticleStatus(status);
        int i = articleMapper.updateByExampleSelective(updateArticle, example);
        if (i!=1){
            GraceException.display(ResponseStatusEnum.ARTICLE_REVIEW_ERROR);
        }
    }

    @Override
    public PagedGridResult queryAllList(Integer status, Integer page, Integer pageSize) {

        Example example = new Example(Article.class);
        example.orderBy("createTime").desc();

        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("articleStatus",status);
        if (ArticleReviewStatus.isArticleStatusValid(status)) {
            criteria.andEqualTo("articleStatus", status);
        }

        // 审核中是机审和人审核的两个状态，所以需要单独判断
        if (status != null && status == 12) {
            criteria.andEqualTo("articleStatus", ArticleReviewStatus.REVIEWING.type)
                    .orEqualTo("articleStatus", ArticleReviewStatus.WAITING_MANUAL.type);
        }

        //isDelete 必须是0
        criteria.andEqualTo("isDelete", YesOrNo.NO.type);

        /**
         * page: 第几页
         * pageSize: 每页显示条数
         */
        PageHelper.startPage(page, pageSize);
        List<Article> list = articleMapper.selectByExample(example);
        return setterPagedGrid(list, page);
    }

    @Override
    public void deleteArticle(String userId, String articleId) {
        Example example = new Example(Article.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("id",articleId);
        criteria.andEqualTo("publishUserId",userId);
        Article article = new Article();
        article.setIsDelete(YesOrNo.YES.type);

        int result = articleMapper.updateByExampleSelective(article, example);
        if (result != 1) {
            GraceException.display(ResponseStatusEnum.ARTICLE_DELETE_ERROR);
        }
    }

    @Override
    public void withdrawArticle(String userId, String articleId) {
        Example example = new Example(Article.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("id",articleId);
        criteria.andEqualTo("publishUserId",userId);
        Article article = new Article();
        article.setArticleStatus(ArticleReviewStatus.WITHDRAW.type);

        int result = articleMapper.updateByExampleSelective(article, example);
        if (result != 1) {
            GraceException.display(ResponseStatusEnum.ARTICLE_DELETE_ERROR);
        }
    }
}
