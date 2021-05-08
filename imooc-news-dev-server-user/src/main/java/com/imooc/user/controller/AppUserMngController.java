package com.imooc.user.controller;

import com.imooc.api.BaseController;
import com.imooc.api.controller.user.AppUserMngControllerApi;
import com.imooc.api.controller.user.PassportControllerApi;
import com.imooc.enums.UserStatus;
import com.imooc.grace.result.GraceJSONResult;
import com.imooc.grace.result.ResponseStatusEnum;
import com.imooc.pojo.AppUser;
import com.imooc.pojo.bo.RegisterLoginBO;
import com.imooc.user.service.AppUserMngService;
import com.imooc.user.service.UserService;
import com.imooc.utils.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

@RestController
public class AppUserMngController extends BaseController implements AppUserMngControllerApi {

    final static Logger logger = LoggerFactory.getLogger(AppUserMngController.class);

    @Autowired
    private AppUserMngService appUserMngService;
    @Autowired
    private UserService  userService;

    @Override
    public GraceJSONResult queryAll(String nickname, Integer status, String startDate, String endDate, Integer page, Integer pageSize) {

        if (page == null ){
            page = 1;
        }
        if (pageSize == null){
            pageSize = 10;
        }

        PagedGridResult gridResult = appUserMngService.queryAllUserList(nickname, status, startDate, endDate, page, pageSize);

        return GraceJSONResult.ok(gridResult);
    }

    @Override
    public GraceJSONResult userDetail(String userId) {
        AppUser user = userService.getUser(userId);
        return GraceJSONResult.ok(user);
    }

    @Override
    public GraceJSONResult freezeUserOrNot(String userId, Integer doStatus) {
        if (!UserStatus.isUserStatusValid(doStatus)){
            return GraceJSONResult.errorCustom(ResponseStatusEnum.USER_STATUS_ERROR);
        }


        appUserMngService.freezeOrNot(userId,doStatus);

        // 刷新用户状态
        // 1 删除用户会话(session ?? 还是 cookie??) 从而保障用户需要重新登陆以后再来刷新他的会话状态
        redis.del(REDIS_USER_INFO+":"+userId);
        // 2 查询最新用户啊的信息 重新放入redis中 做一次更新

        return GraceJSONResult.ok();
    }
}
