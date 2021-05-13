package com.imooc.exception;

import com.imooc.grace.result.GraceJSONResult;
import com.imooc.grace.result.ResponseStatusEnum;
import org.springframework.context.annotation.Bean;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @ResponseBody
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public GraceJSONResult returnException(MethodArgumentNotValidException e){

        BindingResult bindingResult = e.getBindingResult();
        Map<String, String> errors = getErrors(bindingResult);
        return GraceJSONResult.errorMap(errors);
    }

    public Map<String, String> getErrors(BindingResult result){
        Map<String,String> map = new HashMap<>();
        List<FieldError> fieldError = result.getFieldErrors();
        for(FieldError error:fieldError){
            // 发送验证错误的时候所对应的某个属性
            String field = error.getField();
            // 7验证的错误消息
            String msg = error.getDefaultMessage();
            map.put(field,msg);
        }
        return map;
    }

}
