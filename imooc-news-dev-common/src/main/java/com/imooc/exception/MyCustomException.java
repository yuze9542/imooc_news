package com.imooc.exception;

import com.imooc.grace.result.ResponseStatusEnum;
import lombok.Data;

/**
 * 自定义异常
 * 目的：统一处理异常信息
 *      便于解耦，service与controller错误的解耦，不会被service返回的类型而限制
 */
@Data
public class MyCustomException extends RuntimeException {

    private ResponseStatusEnum responseStatusEnum;

    public MyCustomException(ResponseStatusEnum responseStatusEnum) {
        super("异常状态码为：" + responseStatusEnum.status()
                + "；具体异常信息为：" + responseStatusEnum.msg());
        this.responseStatusEnum = responseStatusEnum;
    }
}


