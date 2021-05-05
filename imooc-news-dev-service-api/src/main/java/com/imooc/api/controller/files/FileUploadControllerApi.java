package com.imooc.api.controller.files;

import com.imooc.grace.result.GraceJSONResult;
import com.imooc.pojo.bo.NewAdminBO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Api(value = "文件上传的api")
@RequestMapping("fs")
public interface FileUploadControllerApi {

    @PostMapping("/uploadFace")
    @ApiOperation(value = "上传用户头像",httpMethod = "POST")
    public GraceJSONResult uploadFace(@RequestParam String userId,
                                      MultipartFile file) throws IOException;

    /**
     * 文件上传到mongodb的gridFS中
     * @param bo
     * @return
     * @throws IOException
     */
    @PostMapping("/uploadToGridFS")
    public GraceJSONResult uploadToGridFS(@RequestBody NewAdminBO bo) throws IOException;

    @GetMapping("/readInGridFS")
    public void readInGridFS(@RequestParam String faceId,
                             HttpServletRequest request,
                             HttpServletResponse response) throws IOException;

    /**
     * 后端调用 怎么后端调用呢?
     * ==> restTemplate class:AdminMngController.adminFaceLogin
     * 从grdifs中读取图片数据 返回base64
     * @param faceId
     * @param request
     * @param response
     * @throws IOException
     */
    @GetMapping("/readFace64InGridFS")
    public GraceJSONResult readFace64InGridFS(@RequestParam String faceId,
                             HttpServletRequest request,
                             HttpServletResponse response) throws IOException;
}
