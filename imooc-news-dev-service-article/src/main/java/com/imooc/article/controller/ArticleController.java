package com.imooc.article.controller;

import com.imooc.api.BaseController;
import com.imooc.api.controller.article.ArticleControllerApi;
import com.imooc.article.service.ArticleService;
import com.imooc.enums.ArticleCoverType;
import com.imooc.enums.ArticleReviewStatus;
import com.imooc.enums.YesOrNo;
import com.imooc.grace.result.GraceJSONResult;
import com.imooc.grace.result.ResponseStatusEnum;
import com.imooc.pojo.Category;
import com.imooc.pojo.bo.NewArticleBO;
import com.imooc.pojo.vo.ArticleDetailVO;
import com.imooc.utils.JsonUtils;
import com.imooc.utils.PagedGridResult;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.validation.Valid;
import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class ArticleController extends BaseController implements ArticleControllerApi {

    @Value("${freemarker.html.article}")
    private String articlePath;
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
    @Transactional
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

        if (pendingStatus == ArticleReviewStatus.SUCCESS.type){
            // 审核成功 生成文章详情页静态html
            try {
                createArticleIdHTML(articleId);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return GraceJSONResult.ok();
    }

    @Autowired
    private RestTemplate restTemplate;

    // 文章生成HTML
    private void createArticleIdHTML(String articleId) throws Exception {

        // 0. 配置freemarker基本环境
        Configuration cfg = new Configuration(Configuration.getVersion());
        // 声明freemarker模板所需要加载的目录的位置
        String classpath = this.getClass().getResource("/").getPath();
        cfg.setDirectoryForTemplateLoading(new File(classpath + "templates"));

        // 1. 获得现有的模板ftl文件
        Template template = cfg.getTemplate("detail.ftl", "utf-8");

        // 获得文章的详情数据
        ArticleDetailVO articleDetail = getArticleDetail(articleId);


        // 3. 融合动态数据和ftl，生成html
        File tempDic = new File(articlePath);
        if (!tempDic.exists()) {
            tempDic.mkdirs();
        }
        articlePath = articlePath + File.separator + articleDetail.getId() + ".html";
        Map<String, Object> map = new HashMap<>();
        map.put("articleDetail",articleDetail);
        Writer out = new FileWriter(articlePath);
        template.process(map, out);
        out.close();
    }

    // 发起远程调用rest 调用 文章detail
    public ArticleDetailVO getArticleDetail(String articleId) {
        String url
                = "http://www.imoocnews.com:8001/portal/article/detail?articleId={1}" ;
        ResponseEntity<GraceJSONResult> forEntity = restTemplate.getForEntity(url, GraceJSONResult.class,articleId);
        GraceJSONResult bodyResult = forEntity.getBody();
        ArticleDetailVO detailVO = null;
        if (bodyResult.getStatus() == 200) {
            String detailJson = JsonUtils.objectToJson(bodyResult.getData());
            detailVO = JsonUtils.jsonToPojo(detailJson, ArticleDetailVO.class);
        }
        return detailVO;
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
