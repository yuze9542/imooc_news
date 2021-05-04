package com.imooc.admin.service;

import com.imooc.pojo.AdminUser;
import com.imooc.pojo.bo.NewAdminBO;
import com.imooc.utils.PagedGridResult;

public interface AdminUserService {

    //查询管理员
    public AdminUser queryAdminByUsername(String username);

    // 新增管理员
    public void createAdminUser(NewAdminBO bo);

    //分页查询 admin 列表
    public PagedGridResult queryAdminList(Integer page, Integer pageSize);
}
