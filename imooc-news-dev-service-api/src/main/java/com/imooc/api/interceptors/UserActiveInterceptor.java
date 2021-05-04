package com.imooc.api.interceptors;

import com.imooc.enums.UserStatus;
import com.imooc.exception.GraceException;
import com.imooc.grace.result.ResponseStatusEnum;
import com.imooc.pojo.AppUser;
import com.imooc.utils.JsonUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 用户激活状态检查拦截器
 * 发文章 修改文章 删除文章 发表评论 查看评论等等
 * 这些接口都是需要在用户激活以后 才能放行
 */

public class UserActiveInterceptor extends BaseInterceptor implements HandlerInterceptor {


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
        // false: 请求拦截  true 请求通过验证 放行
        String userId = request.getHeader("headerUserId");
       String useJson = redisOperator.get(REDIS_USER_INFO+":"+userId);
        AppUser user = null;
        if (StringUtils.isNotBlank(useJson)){
            user = JsonUtils.jsonToPojo(useJson,AppUser.class);
        }else {
            GraceException.display(ResponseStatusEnum.UN_LOGIN);
            return false;
        }
        if (user.getActiveStatus() == null || user.getActiveStatus() != UserStatus.ACTIVE.type){
            GraceException.display(ResponseStatusEnum.USER_INACTIVE_ERROR);
            return false;
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
