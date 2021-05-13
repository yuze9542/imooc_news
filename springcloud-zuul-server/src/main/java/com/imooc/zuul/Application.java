package com.imooc.zuul;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication  (exclude = {DataSourceAutoConfiguration.class,
                    MongoAutoConfiguration.class})
@ComponentScan(basePackages = {"com.imooc","org.n3r.idworker"})
@EnableZuulProxy // @EnableZuulServer的升级版 当使用ribbon eureka zuul 时使用

public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
