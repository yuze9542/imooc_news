package com.imooc.exception;

import com.imooc.grace.result.GraceJSONResult;
import com.imooc.grace.result.ResponseStatusEnum;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

/**
 *  统一异常拦截处理
 *  也是一种AOP
 *  可以针对异常的类型进行捕获 然后返回json信息到前端
 */
@ControllerAdvice
public class GraceExceptionHandler {

    @ResponseBody
    @ExceptionHandler(MyCustomException.class)
    public GraceJSONResult returnMyException(MyCustomException e){
        e.printStackTrace();
        return GraceJSONResult.exception(e.getResponseStatusEnum());
    }

    @ResponseBody
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public GraceJSONResult returnMaxUploadSizeExceededException(MaxUploadSizeExceededException e){
        return GraceJSONResult.errorCustom(ResponseStatusEnum.FILE_MAX_SIZE_ERROR);
    }
}
