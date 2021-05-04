package com.imooc.api.controller.files;

import com.imooc.grace.result.GraceJSONResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Api(value = "文件上传的api")
@RequestMapping("fs")
public interface FileUploadControllerApi {

    @PostMapping("/uploadFace")
    @ApiOperation(value = "上传用户头像",httpMethod = "POST")
    public GraceJSONResult uploadFace(@RequestParam String userId,
                                      MultipartFile file) throws IOException;
}
