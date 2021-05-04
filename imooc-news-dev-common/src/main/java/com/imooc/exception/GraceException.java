package com.imooc.exception;

import com.imooc.grace.result.ResponseStatusEnum;

/**
 * 异常的统一封装
 */
public class GraceException {
    public static void display(ResponseStatusEnum responseStatusEnum){
        throw new MyCustomException(responseStatusEnum);
    }
}
