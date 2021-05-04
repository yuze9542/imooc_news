package com.imooc.admin.mapper;

import com.imooc.my.mapper.MyMapper;
import com.imooc.pojo.AdminUser;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AdminUserMapper extends MyMapper<AdminUser> {
}