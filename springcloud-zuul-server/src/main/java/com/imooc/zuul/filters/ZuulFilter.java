package com.imooc.zuul.filters;

import com.netflix.zuul.exception.ZuulException;
import org.springframework.stereotype.Component;

/**
 * 构建zuul的自定义过滤器
 */
@Component
public class ZuulFilter extends com.netflix.zuul.ZuulFilter {

    /**
     *  定义过滤器类型:
     *      pre: 在请求被路由之前
     *      route:  在请求的时候发生
     *      post:: 请求之后
     *      error:  处理请求时发生错误的时候执行
     */
    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 1;
    }

    /**
     * 是否开启过滤器 true为开启
     * @return
     */
    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() throws ZuulException {
        System.out.println("看看这个是啥？");
        return null;    // 无意义 不用管
    }
}
