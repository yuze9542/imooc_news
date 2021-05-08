package com.imooc.admin.service.Impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.imooc.admin.mapper.AdminUserMapper;
import com.imooc.admin.service.AdminUserService;
import com.imooc.api.BaseService;
import com.imooc.exception.GraceException;
import com.imooc.grace.result.ResponseStatusEnum;
import com.imooc.pojo.AdminUser;
import com.imooc.pojo.bo.NewAdminBO;
import com.imooc.utils.PagedGridResult;
import org.apache.commons.lang3.StringUtils;
import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.List;

@Service
public class AdminUserServiceImpl extends BaseService implements AdminUserService {

    @Autowired
    public AdminUserMapper adminUserMapper;

    @Autowired
    public Sid sid;

    @Override
    public AdminUser queryAdminByUsername(String username) {

        Example adminExample = new Example(AdminUser.class);
        Example.Criteria criteria = adminExample.createCriteria(); // 构建
        criteria.andEqualTo("username", username);

        AdminUser adminUser = adminUserMapper.selectOneByExample(adminExample);

        return adminUser;
    }

    @Transactional
    @Override
    public void createAdminUser(NewAdminBO bo) {

        String adminId = sid.next();
        AdminUser adminUser = new AdminUser();
        adminUser.setId(adminId);
        adminUser.setUsername(bo.getUsername());
        adminUser.setAdminName(bo.getAdminName());
        // password不为空 则加密密码
        if (StringUtils.isNotBlank(bo.getPassword())){
            String pwd = BCrypt.hashpw(bo.getPassword(),BCrypt.gensalt());
            adminUser.setPassword(pwd);
        }
        //faceId 为空则设置一个假的
        if (StringUtils.isNotBlank(bo.getFaceId())){
            adminUser.setFaceId(bo.getFaceId());
        }
        adminUser.setCreatedTime(new Date());
        adminUser.setUpdatedTime(new Date());

        int insert = adminUserMapper.insert(adminUser);
        if (insert!=1){
            GraceException.display(ResponseStatusEnum.ADMIN_CREATE_ERROR);
        }
    }

    @Override
    public PagedGridResult queryAdminList(Integer page, Integer pageSize) {

        Example adminExample = new Example(AdminUser.class);
        adminExample.orderBy("createdTime").desc();

        PageHelper.startPage(page,pageSize);
        List<AdminUser> adminUsers = adminUserMapper.selectByExample(adminExample);
        return setterPagedGrid(adminUsers,page);
    }



}
