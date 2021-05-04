package com.imooc.api.interceptors;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 拦截器
 */

public class AdminTokenInterceptor extends BaseInterceptor implements HandlerInterceptor {



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

        String userId = request.getHeader("adminUserId");
        String userToken = request.getHeader("adminUserToken");

        // 判断是否放行
        boolean run = verifyUserIdToken(userId, userToken, REDIS_ADMIN_TOKEN);
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
