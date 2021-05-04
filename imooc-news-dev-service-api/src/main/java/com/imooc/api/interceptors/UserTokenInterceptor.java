package com.imooc.api.interceptors;

import com.imooc.utils.IPUtil;
import com.imooc.utils.RedisOperator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 拦截器
 */

public class UserTokenInterceptor extends BaseInterceptor implements HandlerInterceptor {


    public static final String MOBILE_SMSCODE = "mobile:smscode";

    /**
     * 拦截请求 访问Controller之前
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // false: 请求拦截  ture 请求通过验证 放行

        String userId = request.getHeader("headerUserId");
        String userToken = request.getHeader("headerUserToken");

        // 判断是否放行
        boolean run = verifyUserIdToken(userId, userToken, REDIS_USER_TOKEN);
        return run;    // 放行
    }

    /**
     *  请求访问到Controller之后 渲染视图之前
     * @param request
     * @param response
     * @param handler
     * @param modelAndView
     * @throws Exception
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    /**
     * 访问到Controller之后 渲染之后
     * @param request
     * @param response
     * @param handler
     * @param ex
     * @throws Exception
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
