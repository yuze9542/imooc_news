package com.imooc.admin.mapper;

import com.imooc.my.mapper.MyMapper;
import com.imooc.pojo.AdminUser;
import com.imooc.pojo.Category;
import com.imooc.pojo.bo.SaveCategoryBO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminCategoryMapper extends MyMapper<Category> {
}