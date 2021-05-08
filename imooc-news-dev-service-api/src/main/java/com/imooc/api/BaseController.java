package com.imooc.api;


import com.imooc.grace.result.GraceJSONResult;
import com.imooc.pojo.vo.AppUserVO;
import com.imooc.utils.JsonUtils;
import com.imooc.utils.RedisOperator;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class BaseController {
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    public RedisOperator redis;
    @Value("${website.domain-name}")
    public String DOMAIN_NAME;

    public static final String MOBILE_SMSCODE = "mobile:smscode";
    public static final String REDIS_USER_TOKEN = "redis_user_token";
    public static final String REDIS_USER_INFO = "redis_user_info";
    public static final String REDIS_ADMIN_INFO = "redis_admin_info";
    public static final String REDIS_WRITER_FANS_COUNTS = "redis_writer_fans_counts";
    public static final String REDIS_MY_FOLLOW_COUNTS = "redis_my_follow_counts";
    public static final String REDIS_ALL_CATEGORY = "redis_all_category";
    public static final String REDIS_ARTICLE_READ_COUNTS = "redis_article_read_counts";
    public static final String REDIS_ALREADY_READ = "redis_already_read";
    public static final String REDIS_ARTICLE_COMMENT_COUNTS = "redis_article_comment_counts";

    public static final Integer COOKIE_MONTH = 30 * 24 * 60 * 60;
    public static final Integer COOKIE_DELETE = 0;

    // 设置cookies
    public void setCookie(HttpServletRequest request,
                          HttpServletResponse response,
                          String cookieName,
                          String cookieValue,
                          Integer maxAge
    ){
        try{
            cookieValue = URLEncoder.encode(cookieValue,"utf-8");
            setCookieValue(request,response,cookieName,cookieValue,maxAge);
        }catch (UnsupportedEncodingException e){
            e.printStackTrace();
        }
    }

    public void delCookie(HttpServletRequest request, HttpServletResponse response,String cookieName){
        try{
            String deleteValue = URLEncoder.encode("","utf-8");
            setCookieValue(request,response,cookieName,deleteValue,COOKIE_DELETE);
        }catch (UnsupportedEncodingException e){
            e.printStackTrace();
        }
    }

    public void setCookieValue(HttpServletRequest request,
                               HttpServletResponse response,
                               String cookieName,
                               String cookieValue,
                               Integer maxAge){
        Cookie cookie = new Cookie(cookieName,cookieValue);
        cookie.setMaxAge(maxAge);
        cookie.setDomain(DOMAIN_NAME);
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    // 获取BO中错误信息
    public Map<String, String> getErrors(BindingResult result){
        Map<String,String> map = new HashMap<>();
        List<FieldError> fieldError = result.getFieldErrors();
        for(FieldError error:fieldError){
            // 发送验证错误的时候所对应的某个属性
            String field = error.getField();
            // 7验证的错误消息
            String msg = error.getDefaultMessage();
            map.put(field,msg);
        }
        return map;
    }

    public Integer getCountsFromRedis(String key){
        String s = redis.get(key);
        if (StringUtils.isBlank(s) || Integer.parseInt(s)< 0){
            s = "0";
        }
        return Integer.parseInt(s);
    }

    public List<AppUserVO> getPublisherList (Set idSet){
        String url = "http://user.imoocnews.com:8003/user/getUserByIds?userIds="+ JsonUtils.objectToJson(idSet);
        ResponseEntity<GraceJSONResult> forEntity = restTemplate.getForEntity(url, GraceJSONResult.class);
        List<AppUserVO> publisherList = null;
        if (forEntity.getBody().getStatus() == 200){
            String userJson = JsonUtils.objectToJson(forEntity.getBody().getData());
            publisherList = JsonUtils.jsonToList(userJson, AppUserVO.class);
        }
        return publisherList;
    }
}
