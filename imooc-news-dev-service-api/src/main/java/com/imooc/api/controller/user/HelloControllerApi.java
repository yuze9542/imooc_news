package com.imooc.api.controller.user;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * api 的作用 就相当于 企业领导
 * 其他的服务层是实现 相当于企业员工 只做事情
 * 老板(开发人员) 来看一下每个人(服务)的进度,做什么事
 * 微服务亦如此
 *
 * 运作
 * 现在所有的接口都在此暴露 实现都是在各自的微服务中
 * 本项目只写接口 不写实现 实现都是在各自的微服务工程中
 * controller 也会分散在各个微服务工程中 一旦多了就很难统一管理和查看
 *
 * 其次 微服务之间的调用都是基于接口
 *
 * 此外 本工程的借口其实就是一套规范 实现都是各自的工程去做的处理
 * 目前我们使用springboot作为接口的实现的 如果未来新的java框架
 * 只需要修改对应的实现就可以了
 *
 * Swagger2 基于接口的自动文档生成
 * 所有的配置文件只需要一份
 */
@Api(value = "controller的标题",tags = "带有xx功能的api")
public interface HelloControllerApi {

    @GetMapping("hello")
    @ApiOperation(value = "hello方法的接口",httpMethod = "GET")
    public Object Hello();
}
