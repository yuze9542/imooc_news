package com.imooc.files;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSBuckets;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import javax.management.MXBean;

@Component
public class GridFSConfig {

    @Value("${spring.data.mongodb.database}")
    private String mongodb;

    /**
     * 文件上传
     */
    @Bean
    public GridFSBucket gridFSBucket(MongoClient mongoClient){
        MongoDatabase database = mongoClient.getDatabase(mongodb);  // 连接数据库 ??
        GridFSBucket gridFSBucket = GridFSBuckets.create(database);
        return gridFSBucket;
    }
}
