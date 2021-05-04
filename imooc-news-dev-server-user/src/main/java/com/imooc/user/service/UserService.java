package com.imooc.user.service;

import com.imooc.pojo.AppUser;
import com.imooc.pojo.bo.UpdateUserInfoBO;

public interface UserService {
    // 判断用户是否存在
    public AppUser queryMobileIsExist(String mobile);

    //  创建用户
    public AppUser createUser(String mobile);

    // 根据用户主键 查询用户id
    public AppUser getUser(String userId);

    //用户修改信息 完善资料 并且激活
    public void updateUserInfo(UpdateUserInfoBO updateUserInfoBO);
}
