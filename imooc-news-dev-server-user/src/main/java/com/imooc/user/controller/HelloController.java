package com.imooc.user.controller;

import com.imooc.api.controller.user.HelloControllerApi;
import com.imooc.grace.result.IMOOCJSONResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController implements HelloControllerApi {

    final static Logger logger = LoggerFactory.getLogger(HelloController.class);

    public Object Hello() {
        logger.debug("debug: hello~");
        logger.error("error: hello~");
        logger.warn("warn: hello~");
        return IMOOCJSONResult.ok("你好 朴树");
    }
}
