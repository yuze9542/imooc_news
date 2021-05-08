package com.imooc.user.controller;

import com.imooc.api.BaseController;
import com.imooc.api.controller.user.UserControllerApi;
import com.imooc.grace.result.GraceJSONResult;
import com.imooc.grace.result.ResponseStatusEnum;
import com.imooc.pojo.AppUser;
import com.imooc.pojo.bo.UpdateUserInfoBO;
import com.imooc.pojo.vo.AppUserVO;
import com.imooc.pojo.vo.UserAccountInfoVo;
import com.imooc.user.service.UserService;
import com.imooc.utils.JsonUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class UserController extends BaseController implements UserControllerApi {

    @Autowired
    public UserService userService;

    @Override
    public GraceJSONResult getUserInfo(String userId) {

        // 0 判断参数不能为空
        if (StringUtils.isBlank(userId)){
            return GraceJSONResult.errorCustom(ResponseStatusEnum.UN_LOGIN);
        }

        // 1 根据userId查询用户信息
        AppUser user = getUser(userId);

        // 2 返回用户信息
        AppUserVO appUserVO = new AppUserVO();
        BeanUtils.copyProperties(user,appUserVO); // 拷贝属性 原 -拷贝-> 新

        // 3 查询用户的关注数和粉丝数
        Integer FansCounts = getCountsFromRedis(REDIS_WRITER_FANS_COUNTS + ":" + userId);
        Integer followCounts = getCountsFromRedis(REDIS_MY_FOLLOW_COUNTS + ":" + userId);

        appUserVO.setMyFansCounts(FansCounts);
        appUserVO.setMyFollowCounts(followCounts);

        return GraceJSONResult.ok(appUserVO);
    }

    @Override
    public GraceJSONResult getAccountInfo(String userId) {

        // 0 判断参数不能为空
        if (StringUtils.isBlank(userId)){
            return GraceJSONResult.errorCustom(ResponseStatusEnum.UN_LOGIN);
        }

        // 1 根据userId查询用户信息
        AppUser user = getUser(userId);

        // 2 返回用户信息
        UserAccountInfoVo accountInfoVo = new UserAccountInfoVo();
        BeanUtils.copyProperties(user,accountInfoVo); // 拷贝属性 原 -> 新

        return GraceJSONResult.ok(accountInfoVo);
    }

    @Override
    public GraceJSONResult updateUserInfo(@Valid UpdateUserInfoBO updateUserInfoBO,
                                          BindingResult result) {
        // 0 校验BO
        if (result.hasErrors()){
            Map<String, String> errors = getErrors(result);
            return GraceJSONResult.errorMap(errors);
        }

        // 1 执行更新操作
        userService.updateUserInfo(updateUserInfoBO);

        return GraceJSONResult.ok();
    }

    @Override
    public GraceJSONResult getUserByIds(String userIds) {

        if (StringUtils.isBlank(userIds)){
            return GraceJSONResult.errorCustom(ResponseStatusEnum.USER_NOT_EXIST_ERROR);
        }

        List<AppUserVO> publisherList = new ArrayList<>();
        List<String> userIdList = JsonUtils.jsonToList(userIds, String.class);
        for (String userId:userIdList){
            // 1 根据userId查询用户信息
            AppUser user = getUser(userId);

            // 2 返回用户信息
            AppUserVO UserVO = new AppUserVO();
            BeanUtils.copyProperties(user,UserVO); // 拷贝属性 原 -拷贝-> 新

            // 3 添加到publisherList
            publisherList.add(UserVO);
        }

        return GraceJSONResult.ok(publisherList);
    }



    private AppUser getUser(String userId){

        // 查询判断redis中是否包含用户信息
        // 如果包含 则查询后直接返回 就不去查数据库了
        String userJson = redis.get(REDIS_USER_INFO+":"+userId);
        AppUser user = null;
        if (StringUtils.isNotBlank(userJson)){
            user = JsonUtils.jsonToPojo(userJson, AppUser.class);
        }else {
            user = userService.getUser(userId);

            // 由于用户不经常会变动 对于千万级别的网站来说
            // 这类信息不会直接问去查询数据
            // 那么可以依靠redis 直接把查询后的数据存入redis中

            redis.set(REDIS_USER_INFO+":"+userId, JsonUtils.objectToJson(user));
        }



        return user;
    }

}
