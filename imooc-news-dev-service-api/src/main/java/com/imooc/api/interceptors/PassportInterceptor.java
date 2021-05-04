package com.imooc.api.interceptors;

import com.imooc.api.BaseController;
import com.imooc.utils.IPUtil;
import com.imooc.utils.RedisOperator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 拦截器
 */

public class PassportInterceptor implements HandlerInterceptor {

    @Autowired
    public RedisOperator redis;

    @Resource
    private RedisTemplate redisTemplate;

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
        String userIp = IPUtil.getRequestIp(request);
        if (redisTemplate == null)
            return true;

        boolean keyIsExist = redisTemplate.hasKey(MOBILE_SMSCODE + ":" + userIp);
        if (keyIsExist){
            System.out.println("短信发送频率太大");
            return false;   //拦截
        }

        return true;    // 放行
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
