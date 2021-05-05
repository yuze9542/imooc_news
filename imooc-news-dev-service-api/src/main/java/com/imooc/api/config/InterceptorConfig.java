package com.imooc.api.config;

import com.imooc.api.interceptors.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class InterceptorConfig implements WebMvcConfigurer {

    @Bean       // 之前因为没加bean总是出错
    public PassportInterceptor passportInterceptor(){
        return new PassportInterceptor();
    }

    @Bean
    public UserTokenInterceptor userTokenInterceptor(){
        return new UserTokenInterceptor();
    }

//    @Bean
//    public AdminCookieTokenInterceptor adminCookieTokenInterceptor(){
//        return new AdminCookieTokenInterceptor();
//    }

    @Bean
    public AdminTokenInterceptor adminTokenInterceptor(){
        return new AdminTokenInterceptor();
    }

    @Bean
    public UserActiveInterceptor userActiveInterceptor(){
        return new UserActiveInterceptor();
    }


    /**
     * 拦截器注册
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(passportInterceptor())
        .addPathPatterns("/passport/getSMSCode");

        registry.addInterceptor(userTokenInterceptor())
                .addPathPatterns("/user/getAccountInfo")
                .addPathPatterns("/user/updateUserInfo");

//        registry.addInterceptor(userActiveInterceptor())
//                .addPathPatterns("/user/getAccountInfo");
//        registry.addInterceptor(adminCookieTokenInterceptor())
//                .addPathPatterns("/fs/uploadToGridFS")
//                .addPathPatterns("/fs/readInGridFS");
        registry.addInterceptor(adminTokenInterceptor())
                .addPathPatterns("/adminMng/adminIsExist")
                .addPathPatterns("/adminMng/addNewAdmin")
                .addPathPatterns("/adminMng/getAdminList")
                .addPathPatterns("/fs/uploadToGridFS")
                .addPathPatterns("/fs/readInGridFS");
//                .addPathPatterns("/friendLinkMng/saveOrUpdateFriendLink")
//                .addPathPatterns("/friendLinkMng/getFriendLinkList")
//                .addPathPatterns("/friendLinkMng/delete")
//                .addPathPatterns("/categoryMng/saveOrUpdateCategory")
//                .addPathPatterns("/categoryMng/getCatList");

    }
}
