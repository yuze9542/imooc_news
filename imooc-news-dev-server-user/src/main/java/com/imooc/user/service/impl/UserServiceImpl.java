package com.imooc.user.service.impl;

import com.imooc.enums.Sex;
import com.imooc.enums.UserStatus;
import com.imooc.exception.GraceException;
import com.imooc.grace.result.ResponseStatusEnum;
import com.imooc.pojo.AppUser;
import com.imooc.pojo.bo.UpdateUserInfoBO;
import com.imooc.user.mapper.AppUserMapper;
import com.imooc.user.service.UserService;
import com.imooc.utils.DateUtil;
import com.imooc.utils.JsonUtils;
import com.imooc.utils.RedisOperator;
import org.n3r.idworker.Sid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;

@Service
public class UserServiceImpl implements UserService {

    public static final String REDIS_USER_INFO = "redis_user_info";

    @Autowired
    public AppUserMapper  appUserMapper;

    @Autowired
    public RedisOperator redis;

    private static String USER_FACE0 = "https://pics7.baidu.com/feed/a8773912b31bb051fb37de05c78e64b24bede083.jpeg?token=f02d22e51399a01c6c239e6247cec44f";
    private static String USER_FACE1 = "https://pics7.baidu.com/feed/a8773912b31bb051fb37de05c78e64b24bede083.jpeg?token=f02d22e51399a01c6c239e6247cec44f";
    private static String USER_FACE2 = "https://pics7.baidu.com/feed/a8773912b31bb051fb37de05c78e64b24bede083.jpeg?token=f02d22e51399a01c6c239e6247cec44f";

    @Autowired
    public Sid sid;

    @Override
    public AppUser queryMobileIsExist(String mobile) {
        Example userExample = new Example(AppUser.class);
        Example.Criteria userCriteria = userExample.createCriteria();
        userCriteria.andEqualTo("mobile",mobile); // 左边属性 和AppUser匹配  右边是值
        AppUser appUser = appUserMapper.selectOneByExample(userExample);
        return appUser;
    }

    @Override
    @Transactional // 事务
    public AppUser createUser(String mobile) {
        /**
         * 互联网项目都要考虑可扩展性
         * 未来业务扩展 需要分库分表
         * 那么数据库的表逐渐id必须保证全局(全库)唯一
         */
        String userId = sid.nextShort();
        AppUser user = new AppUser();

        user.setId(userId);
        user.setMobile(mobile);
        user.setNickname("用户: "+mobile);
        user.setFace(USER_FACE0);
        user.setBirthday(DateUtil.stringToDate("1980-01-01"));
        user.setSex(Sex.secret.type);
        user.setActiveStatus(UserStatus.INACTIVE.type);
        user.setTotalIncome(0);
        user.setCreatedTime(new Date());
        user.setUpdatedTime(new Date());

        appUserMapper.insert(user);
        return user;
    }

    @Override
    public AppUser getUser(String userId) {
        return appUserMapper.selectByPrimaryKey(userId);
    }

    @Override
    public void updateUserInfo(UpdateUserInfoBO updateUserInfoBO) {

        String userId = updateUserInfoBO.getId();

        // 为保证数据一致性 先删除redis数据 后更新数据库
        redis.del(REDIS_USER_INFO+":"+userId);

        AppUser userInfo = new AppUser();
        BeanUtils.copyProperties(updateUserInfoBO,userInfo);

        userInfo.setUpdatedTime(new Date());
        userInfo.setActiveStatus(UserStatus.ACTIVE.type);

        int result =appUserMapper.updateByPrimaryKeySelective(userInfo);    // 查询
        if (result != 1){
            GraceException.display(ResponseStatusEnum.USER_UPDATE_ERROR);
        }
        // 再次查询用户的最新信息 放入redis中
        AppUser user = getUser(userId);
        redis.set(REDIS_USER_INFO+":"+userId, JsonUtils.objectToJson(user));

        // 缓存双删
        try{
            Thread.sleep(100);
            redis.del(REDIS_USER_INFO+":"+userId);
        }catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
