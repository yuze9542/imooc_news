package com.imooc.api.controller.user.fallback;

import com.imooc.api.controller.user.UserControllerApi;
import com.imooc.grace.result.GraceJSONResult;
import com.imooc.pojo.bo.UpdateUserInfoBO;
import com.imooc.pojo.vo.AppUserVO;
import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

import javax.validation.Valid;
import java.util.ArrayList;

@Component
public class UserControllerFactoryFallback implements FallbackFactory<UserControllerApi> {

    @Override
    public UserControllerApi create(Throwable throwable) {
        // 重写过程就是降级过程
        return new UserControllerApi() {
            @Override
            public GraceJSONResult getUserInfo(String userId) {
                return null;
            }

            @Override
            public GraceJSONResult getAccountInfo(String userId) {
                return null;
            }

            @Override
            public GraceJSONResult updateUserInfo(@Valid UpdateUserInfoBO updateUserInfoBO) {
                return null;
            }

            @Override
            public GraceJSONResult getUserByIds(String userIds) {
                System.out.println("返回客户端降级方法");
                ArrayList<AppUserVO> publisherList = new ArrayList<>();
                return GraceJSONResult.ok(publisherList);
            }
        };
    }
}
