package com.imooc.article.controller;

import com.imooc.api.BaseController;
import com.imooc.api.controller.article.ArticlePortalControllerApi;
import com.imooc.article.service.ArticlePortalService;
import com.imooc.grace.result.GraceJSONResult;
import com.imooc.pojo.Article;
import com.imooc.pojo.vo.AppUserVO;
import com.imooc.pojo.vo.ArticleDetailVO;
import com.imooc.pojo.vo.IndexArticleVO;
import com.imooc.utils.IPUtil;
import com.imooc.utils.JsonUtils;
import com.imooc.utils.PagedGridResult;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
public class ArticlePortalController extends BaseController implements ArticlePortalControllerApi {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ArticlePortalService articlePortalService;

    @Override
    public GraceJSONResult list(String keyword, Integer category, Integer page, Integer pageSize) {
        if (page == null)
            page = 1;
        if (pageSize == null)
            pageSize = 10;
        PagedGridResult gridResult = articlePortalService.queryIndexArticleList(keyword,category,page, pageSize);

//        List<Article> list = (List<Article>) gridResult.getRows();
//
//        // 1 构建发布者id列表
//        Set idSet = new HashSet();
//        for (Article a:list){
//            String publishUserId = a.getPublishUserId();
//            idSet.add(publishUserId);
//        }
//        // 2 发起远程调用 (restTemplate) 请求用户微服务获得用户(userId 发布者) 的列表
//        String url = "http://user.imoocnews.com:8003/user/getUserByIds?userIds="+ JsonUtils.objectToJson(idSet);
//        ResponseEntity<GraceJSONResult> forEntity = restTemplate.getForEntity(url, GraceJSONResult.class);
//        List<AppUserVO> publisherList = null;
//        if (forEntity.getBody().getStatus() == 200){
//            String userJson = JsonUtils.objectToJson(forEntity.getBody().getData());
//            publisherList = JsonUtils.jsonToList(userJson, AppUserVO.class);
//        }
//
//        // 3 拼接两个List
//        ArrayList<IndexArticleVO> indexArticleList = new ArrayList<>();
//        for (Article a:list){
//            IndexArticleVO indexArticleVO = new IndexArticleVO();
//            BeanUtils.copyProperties(a,indexArticleVO);
//
//            // 3.1 从publisher中获得发布者的基本信息
//            AppUserVO userInfoPublisher = getUserInfoPublisher(a.getPublishUserId(), publisherList);
//            indexArticleVO.setPublisherVO(userInfoPublisher);
//            indexArticleList.add(indexArticleVO);
//        }
//        gridResult.setRows(indexArticleList);

        PagedGridResult result = rebuildArticleGrid(gridResult);
        // END
        return GraceJSONResult.ok(result);
    }

    @Override
    public GraceJSONResult hotList() {
        List<Article> list = articlePortalService.queryHotList();
        return GraceJSONResult.ok(list);
    }

    @Override
    public GraceJSONResult queryArticleListOfWriter(String writerId, Integer page, Integer pageSize) {
        if (page == null)
            page = 1;
        if (pageSize == null)
            pageSize = 10;
        PagedGridResult gridResult = articlePortalService.queryArticleListOfWriter(writerId, page, pageSize);
        return GraceJSONResult.ok(gridResult);
    }

    @Override
    public GraceJSONResult queryGoodArticleListOfWriter(String writerId) {
        PagedGridResult gridResult = articlePortalService.queryGoodArticleListOfWriter(writerId);
        return GraceJSONResult.ok(gridResult);
    }

    @Override
    public GraceJSONResult detail(String articleId) {

        ArticleDetailVO articleDetailVO = articlePortalService.queryDetail(articleId);
        Set idSet = new HashSet();
        idSet.add(articleDetailVO.getPublishUserId());

        List<AppUserVO> publisherList = getPublisherList(idSet);
        if (!publisherList.isEmpty()){
            articleDetailVO.setPublishUserName(publisherList.get(0).getNickname());
        }
        articleDetailVO.setReadCounts(getCountsFromRedis(REDIS_ARTICLE_READ_COUNTS+":"+articleId));
        return GraceJSONResult.ok(articleDetailVO);
    }

    @Override
    public GraceJSONResult readArticle(String articleId,
                                       HttpServletRequest request,
                                       HttpServletResponse response) {
        String userIp = IPUtil.getRequestIp(request);
        // 设置对当前ip的永久存在的key 存入到redis
        // 表示该 ip用户 已经阅读过了
        redis.setnx60m(REDIS_ALREADY_READ+":"+articleId+":"+userIp, userIp);

        redis.increment(REDIS_ARTICLE_READ_COUNTS+":"+articleId,1);

        return null;
    }



    private PagedGridResult rebuildArticleGrid(PagedGridResult gridResult){
        List<Article> list = (List<Article>) gridResult.getRows();

        // 1 构建发布者id列表
        Set idSet = new HashSet();
        List<String> idList = new ArrayList<>();

        for (Article a:list){
            // 1.1 构建文章发布者id
            String publishUserId = a.getPublishUserId();
            idSet.add(publishUserId);
            // 1.2 构建文章id
            idList.add(REDIS_ARTICLE_READ_COUNTS + ":" +a.getId());
        }

        // 发起redis 的批量 mget 批量查询api 获得对应值
        List<String> readCountsRedisList = redis.mget(idList);


        // 2 发起远程调用 (restTemplate) 请求用户微服务获得用户(userId 发布者) 的列表
        String url = "http://user.imoocnews.com:8003/user/getUserByIds?userIds="+ JsonUtils.objectToJson(idSet);
        ResponseEntity<GraceJSONResult> forEntity = restTemplate.getForEntity(url, GraceJSONResult.class);
        List<AppUserVO> publisherList = null;
        if (forEntity.getBody().getStatus() == 200){
            String userJson = JsonUtils.objectToJson(forEntity.getBody().getData());
            publisherList = JsonUtils.jsonToList(userJson, AppUserVO.class);
        }

        // 3 拼接两个List
        List<IndexArticleVO> indexArticleList = new ArrayList<>();
//        for (Article a:list){
//            IndexArticleVO indexArticleVO = new IndexArticleVO();
//            BeanUtils.copyProperties(a,indexArticleVO);
//
//            // 3.1 从publisher中获得发布者的基本信息
//            AppUserVO userInfoPublisher = getUserInfoPublisher(a.getPublishUserId(), publisherList);
//            indexArticleVO.setPublisherVO(userInfoPublisher);
//
//            // 3.2 重新组装设置文章列表中的阅读量
//            Integer readCount = getCountsFromRedis(REDIS_ARTICLE_READ_COUNTS + ":" + a.getId());
//            indexArticleVO.setReadCounts(readCount);
//
//            indexArticleList.add(indexArticleVO);
//        }
        for (int i=0; i<list.size();i++){
            IndexArticleVO indexArticleVO = new IndexArticleVO();
            Article a = list.get(i);
            BeanUtils.copyProperties(a,indexArticleVO);

            // 3.1 从publisher中获得发布者的基本信息
            AppUserVO userInfoPublisher = getUserInfoPublisher(a.getPublishUserId(), publisherList);
            indexArticleVO.setPublisherVO(userInfoPublisher);

            // 3.2 重新组装设置文章列表中的阅读量
            String redisCountsStr = readCountsRedisList.get(i);
            int readCount = 0;
            if (StringUtils.isNotBlank(redisCountsStr)){
                readCount = Integer.parseInt(redisCountsStr);
            }

            indexArticleVO.setReadCounts(readCount);
            indexArticleList.add(indexArticleVO);
        }

        gridResult.setRows(indexArticleList);
        return gridResult;
    }


    private AppUserVO getUserInfoPublisher(String publishId, List<AppUserVO> publisherList){
        for (AppUserVO u:publisherList){
            if (u.getId().equalsIgnoreCase(publishId)){
                return u;
            }

        }
        return null;
    }
}
