package com.imooc.pojo.bo;

import lombok.Data;

import javax.validation.constraints.NotBlank;
@Data
public class SaveCategoryBO {

    private Integer id;

    @NotBlank(message = "分类名不能为空")
    private String name;

    private String oldName;

    @NotBlank(message = "分类颜色不能为空")
    private String tagColor;

}