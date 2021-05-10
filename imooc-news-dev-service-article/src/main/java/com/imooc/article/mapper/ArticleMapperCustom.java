package com.imooc.article.mapper;

import com.imooc.my.mapper.MyMapper;
import com.imooc.pojo.Article;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Repository
public interface ArticleMapperCustom {

    // 更新 定时任务 到 及时发布
    public void updateAppointToPublish();

}