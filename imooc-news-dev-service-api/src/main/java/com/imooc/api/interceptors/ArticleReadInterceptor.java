package com.imooc.api.interceptors;

import com.imooc.utils.IPUtil;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ArticleReadInterceptor extends BaseInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {



        String userIp = IPUtil.getRequestIp(request);
        // 设置对当前ip的永久存在的key 存入到redis
        // 表示该 ip用户 已经阅读过了
        boolean b = redisOperator.keyIsExist(REDIS_ALREADY_READ+":"+request.getParameter("articleId")+":"+userIp);
        if (b){
            return false;
        }
        return true;
    }
}
