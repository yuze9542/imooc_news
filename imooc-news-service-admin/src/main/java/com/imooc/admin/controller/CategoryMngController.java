package com.imooc.admin.controller;

import com.imooc.admin.service.CategoryMngService;
import com.imooc.api.BaseController;
import com.imooc.api.controller.admin.CategoryMngControllerApi;
import com.imooc.grace.result.GraceJSONResult;
import com.imooc.grace.result.ResponseStatusEnum;
import com.imooc.pojo.Category;
import com.imooc.pojo.bo.SaveCategoryBO;
import com.imooc.utils.JsonUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
public class CategoryMngController extends BaseController implements CategoryMngControllerApi {

    @Autowired
    private CategoryMngService  categoryMngService;

    @Override
    public GraceJSONResult saveOrUpdateCategory(@Valid SaveCategoryBO newCategoryBO, BindingResult result) {

        // 1 查询错误
        if (result.hasErrors()){
            Map<String, String> errors = getErrors(result);
            GraceJSONResult.errorMap(errors);
        }
        //  2 把 Category 送到service中  id为空新增，不为空修改
        Category category = new Category();
        BeanUtils.copyProperties(newCategoryBO,category);
        // 新增
        if (newCategoryBO.getId() == null) {
            // 查询新增的分类名称不能重复存在
            boolean isExist = categoryMngService.queryCatIsExist(category.getName(), null);
            if (!isExist) {
                // 新增到数据库
                categoryMngService.saveCategory(category);
            } else {
                return GraceJSONResult.errorCustom(ResponseStatusEnum.CATEGORY_EXIST_ERROR);
            }
        } else { // 修改
            // 查询修改的分类名称不能重复存在
            boolean isExist = categoryMngService.queryCatIsExist(category.getName(), newCategoryBO.getOldName());
            if (!isExist) {
                // 修改到数据库
                categoryMngService.modifyCategory(category);
            } else {
                return GraceJSONResult.errorCustom(ResponseStatusEnum.CATEGORY_EXIST_ERROR);
            }
        }

        return GraceJSONResult.ok();
    }

    @Override
    public GraceJSONResult getCatList() {

        List<Category> categoryList = categoryMngService.queryCategoryList();
        return GraceJSONResult.ok(categoryList);
    }

    @Override
    public GraceJSONResult getCats() {
        // 先从redis中查询，如果有，则返回，如果没有，则查询数据库库后先放缓存，放返回
        String allCatJson = redis.get(REDIS_ALL_CATEGORY);

        List<Category> categoryList = null;
        // 如果从redis中找不到 就从数据库中拿 找的到就从redis中拿
        if (StringUtils.isBlank(allCatJson)) {
            categoryList = categoryMngService.queryCategoryList();
            redis.set(REDIS_ALL_CATEGORY, JsonUtils.objectToJson(categoryList));
        } else {
            categoryList = JsonUtils.jsonToList(allCatJson, Category.class);
        }

        return GraceJSONResult.ok(categoryList);
    }
}
