package com.imooc.pojo.bo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.imooc.valid.CheckUrl;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * 友情链接
 */
@Data
public class SaveFriendLinkBO {

    private String id;

    @NotBlank(message = "友情链接名称不能为空")
    private String linkName;

    @CheckUrl
    @NotBlank(message = "友情链接地址不能为空")
    private String linkUrl;

    @NotNull(message = "请选择保留或删除")
    private Integer isDelete;

}