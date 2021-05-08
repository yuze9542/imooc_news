package com.imooc.article.task;

import com.imooc.article.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalDateTime;


//@Configuration  // 1 开始配置类 使得springboot容器扫描到
//@EnableScheduling   //  2   开启定时任务
public class TaskPublishArticle {

    @Autowired
    private ArticleService articleService;

    // 添加定时任务
    // 后续通过MQ优化
    @Scheduled(cron = "0/3 * * * * ? ")
    private void publishArticles(){
//        System.out.println("执行定时任务" + LocalDateTime.now());
        articleService.updateAppointToPublish();
    }
}
