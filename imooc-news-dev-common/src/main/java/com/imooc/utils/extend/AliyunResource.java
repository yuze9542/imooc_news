package com.imooc.utils.extend;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * 引入 aliyun.properties 文件
 */
@Component
@PropertySource("classpath:aliyun.properties")   // 指明路径
@ConfigurationProperties(prefix = "aliyun") // 查找资源文件时携带 aliyun 的字样
@Data
public class AliyunResource {

    private String accessKeyID;
    private String accessKeySecret;
}
