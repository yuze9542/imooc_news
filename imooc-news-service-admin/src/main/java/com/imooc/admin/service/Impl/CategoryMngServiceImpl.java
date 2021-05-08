package com.imooc.admin.service.Impl;

import com.imooc.admin.mapper.AdminCategoryMapper;
import com.imooc.admin.service.CategoryMngService;
import com.imooc.api.BaseService;
import com.imooc.exception.GraceException;
import com.imooc.grace.result.ResponseStatusEnum;
import com.imooc.pojo.Category;
import com.imooc.pojo.bo.SaveCategoryBO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@Service
public class CategoryMngServiceImpl extends BaseService implements CategoryMngService {

    @Autowired
    private AdminCategoryMapper adminCategoryMapper;

    @Override
    @Transactional
    public void saveCategory(Category category) {


        int insert = adminCategoryMapper.insert(category);
        if (insert!=1){
            GraceException.display(ResponseStatusEnum.CATEGORY_INSERT_ERROR);
        }

        // 直接使用redis删除缓存即可，用户端在查询的时候会直接查库，再把最新的数据放入到缓存中
        redis.del(REDIS_ALL_CATEGORY);
    }

    @Override
    public boolean queryCatIsExist(String newName, String oldName) {

        Example example = new Example(Category.class);  // 先定义查询条件
        Example.Criteria catCriteria = example.createCriteria();
        catCriteria.andEqualTo("name", newName);    // 1 难道是包含新名字不包含就名字
        if (StringUtils.isNotBlank(oldName)) {  //改名
            catCriteria.andNotEqualTo("name", oldName);
        }

        // 2 然后查询到了 就返回真
        List<Category> catList = adminCategoryMapper.selectByExample(example);

        boolean isExist = false;
        if (catList != null && !catList.isEmpty() && catList.size() > 0) {
            isExist = true;
        }

        return isExist;
    }

    @Override
    @Transactional
    public void modifyCategory(Category category) {
        int result = adminCategoryMapper.updateByPrimaryKey(category);
        if (result != 1) {
            GraceException.display(ResponseStatusEnum.SYSTEM_OPERATION_ERROR);
        }

        /**
         * 不建议如下做法：
         * 1. 查询redis中的categoryList
         * 2. 循环categoryList中拿到原来的老的数据
         * 3. 替换老的category为新的
         * 4. 再次转换categoryList为json，并存入redis中
         */

        // 直接使用redis删除缓存即可，用户端在查询的时候会直接查库，再把最新的数据放入到缓存中
        redis.del(REDIS_ALL_CATEGORY);
    }

    @Override
    public List<Category> queryCategoryList() {

        return adminCategoryMapper.selectAll();
    }

    @Override
    @Transactional
    public void deleteCat(Category category) {
        adminCategoryMapper.delete(category);
    }
}
