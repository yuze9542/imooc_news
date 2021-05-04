package com.imooc.user.controller;

import com.imooc.api.controller.user.HelloControllerApi;
import com.imooc.grace.result.GraceJSONResult;
import com.imooc.grace.result.IMOOCJSONResult;
import com.imooc.utils.RedisOperator;
import org.apache.ibatis.annotations.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController implements HelloControllerApi {

    final static Logger logger = LoggerFactory.getLogger(HelloController.class);

    @Autowired
    private RedisOperator redisOperator;

    public Object Hello() {
        logger.debug("debug: hello~");
        logger.error("error: hello~");
        logger.warn("warn: hello~");
        return IMOOCJSONResult.ok("你好 朴树");
    }

        @GetMapping("redis")
       public Object redis(){
            redisOperator.set("姓名","余泽");
            redisOperator.set("age","12");
            return GraceJSONResult.ok(redisOperator.get("age"));
       }


}
