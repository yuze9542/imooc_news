package com.imooc.article.service.Impl;

import com.github.pagehelper.PageHelper;
import com.imooc.api.BaseService;
import com.imooc.article.mapper.ArticleMapper;
import com.imooc.article.service.ArticlePortalService;
import com.imooc.enums.ArticleReviewStatus;
import com.imooc.enums.YesOrNo;
import com.imooc.pojo.Article;
import com.imooc.pojo.vo.ArticleDetailVO;
import com.imooc.utils.PagedGridResult;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@Service
public class ArticlePortalServiceImpl extends BaseService implements ArticlePortalService {

    @Autowired
    private ArticleMapper articleMapper;

    @Override
    public PagedGridResult queryIndexArticleList(String keyword, Integer category, Integer page, Integer pageSize) {

        Example example = new Example(Article.class);
        example.orderBy("publishTime").desc();
        Example.Criteria criteria = example.createCriteria();

        // 硬性条件
        criteria.andEqualTo("isAppoint",YesOrNo.NO.type);   // 即时发布的
        criteria.andEqualTo("isDelete",YesOrNo.NO.type);
        criteria.andEqualTo("articleStatus",ArticleReviewStatus.SUCCESS.type);

        // 可选条件
        if (StringUtils.isNotBlank(keyword)){
            criteria.andLike("title","%"+keyword+"%");
        }
        if (category != null){
            criteria.andEqualTo("categoryId",category);
        }

        PageHelper.startPage(page,pageSize);
        List<Article> list = articleMapper.selectByExample(example);
        return setterPagedGrid(list,page);
    }

    @Override
    public List<Article> queryHotList() {
        Example example = new Example(Article.class);
        Example.Criteria criteria = serDefaultArticleExample(example);
        PageHelper.startPage(1,5);
        List<Article> list = articleMapper.selectByExample(example);
        return list;
    }

    @Override
    public PagedGridResult queryArticleListOfWriter(String writerId, Integer page, Integer pageSize) {
        Example example = new Example(Article.class);
        Example.Criteria criteria = serDefaultArticleExample(example);
        criteria.andEqualTo("publishUserId",writerId);
        PageHelper.startPage(page,pageSize);
        List<Article> list = articleMapper.selectByExample(example);
        PagedGridResult gridResult = setterPagedGrid(list, page);
        return gridResult;
    }

    @Override
    public PagedGridResult queryGoodArticleListOfWriter(String writerId) {
        Example articleExample = new Example(Article.class);
        articleExample.orderBy("publishTime").desc();

        Example.Criteria criteria = serDefaultArticleExample(articleExample);
        criteria.andEqualTo("publishUserId", writerId);

        /**
         * page: 第几页
         * pageSize: 每页显示条数
         */
        PageHelper.startPage(1, 5);
        List<Article> list = articleMapper.selectByExample(articleExample);
        return setterPagedGrid(list, 1);
    }

    @Override
    public ArticleDetailVO queryDetail(String articleId) {

        Article article = new Article();
        article.setId(articleId);

        // 硬性条件
        article.setIsDelete(YesOrNo.NO.type);
        article.setIsAppoint(YesOrNo.NO.type);
        article.setArticleStatus(ArticleReviewStatus.SUCCESS.type);

        Article result = articleMapper.selectOne(article);
        ArticleDetailVO articleDetailVO = new ArticleDetailVO();
        BeanUtils.copyProperties(result,articleDetailVO);
        articleDetailVO.setCover(result.getArticleCover());
        return articleDetailVO;
    }

    private Example.Criteria serDefaultArticleExample(Example example){
        example.orderBy("publishTime").desc();
        Example.Criteria criteria = example.createCriteria();

        // 硬性条件
        criteria.andEqualTo("isAppoint",YesOrNo.NO.type);   // 即时发布的
        criteria.andEqualTo("isDelete",YesOrNo.NO.type);
        criteria.andEqualTo("articleStatus",ArticleReviewStatus.SUCCESS.type);
        return criteria;
    }
}
