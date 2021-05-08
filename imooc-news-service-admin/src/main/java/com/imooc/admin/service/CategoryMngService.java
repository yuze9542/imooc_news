package com.imooc.admin.service;

import com.imooc.pojo.Category;
import com.imooc.pojo.bo.SaveCategoryBO;

import java.util.List;

public interface CategoryMngService {

    // 保存某个分类
    public void saveCategory(Category category);

    // 查询某个名称是否存在 包含newName 不包含旧名字
    public boolean queryCatIsExist(String newName,String oldName);

    // 修改某个分类
    public void modifyCategory(Category category);
    public List<Category> queryCategoryList();
    public void deleteCat(Category category);
}
