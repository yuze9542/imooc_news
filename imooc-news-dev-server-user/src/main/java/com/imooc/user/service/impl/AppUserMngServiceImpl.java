package com.imooc.user.service.impl;

import com.github.pagehelper.PageHelper;
import com.imooc.api.BaseService;
import com.imooc.enums.UserStatus;
import com.imooc.exception.GraceException;
import com.imooc.grace.result.ResponseStatusEnum;
import com.imooc.pojo.AppUser;
import com.imooc.user.mapper.AppUserMapper;
import com.imooc.user.service.AppUserMngService;
import com.imooc.utils.PagedGridResult;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@Service
public class AppUserMngServiceImpl extends BaseService implements AppUserMngService {

    @Autowired
    private AppUserMapper appUserMapper;

    @Override
    public PagedGridResult queryAllUserList(String nickname, Integer status, String startDate, String endDate, Integer page, Integer pageSize) {

        Example example = new Example(AppUser.class);
        example.orderBy("createdTime").desc();
        Example.Criteria criteria = example.createCriteria();

        if (StringUtils.isNotBlank(nickname)){
            criteria.andLike("nickname","%"+nickname+"%");
        }
        if (UserStatus.isUserStatusValid(status)){
            criteria.andEqualTo("activeStatus",status);
        }
        if (startDate!=null && startDate!=""){
            criteria.andGreaterThanOrEqualTo("createdTime",startDate);
        }
        if (endDate!=null && endDate!=""){
            criteria.andLessThanOrEqualTo("createdTime",endDate);
        }

        // 分页
        PageHelper.startPage(page,pageSize);

        List<AppUser> appUsers = appUserMapper.selectByExample(example);


        return setterPagedGrid(appUsers,page);
    }

    @Override
    @Transactional
    public void freezeOrNot(String userId, Integer status) {

        AppUser user = new AppUser();
        user.setId(userId);
        user.setActiveStatus(status);

        int i = appUserMapper.updateByPrimaryKeySelective(user);
        if (i!=1){
            GraceException.display(ResponseStatusEnum.FAILED);
        }

    }
}
