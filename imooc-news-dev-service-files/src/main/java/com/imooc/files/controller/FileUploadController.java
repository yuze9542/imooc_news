package com.imooc.files.controller;

import com.imooc.api.controller.files.FileUploadControllerApi;
import com.imooc.files.FileResource;
import com.imooc.files.service.UploadService;
import com.imooc.grace.result.GraceJSONResult;
import com.imooc.grace.result.ResponseStatusEnum;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
public class FileUploadController implements FileUploadControllerApi {

    final static Logger logger = LoggerFactory.getLogger(FileUploadController.class);

    @Autowired
    private UploadService uploadService;

    @Autowired
    private FileResource fileResource;

    @Override
    public GraceJSONResult uploadFace(String userId, MultipartFile file) throws IOException {
        String path ="";
        if (file != null){
            // 获得文件名
            String  fileName = file.getOriginalFilename();
            // 判断文件名不能为空
            if (StringUtils.isBlank(fileName)){
                return GraceJSONResult.errorCustom(ResponseStatusEnum.FILE_UPLOAD_FAILD);
            }else {
                String fileNameArr[] = fileName.split("\\.");
                //获得后缀
                String suffix = fileNameArr[fileNameArr.length-1];
                if (!suffix.equalsIgnoreCase("png") &&
                        !suffix.equalsIgnoreCase("jpg") &&
                        !suffix.equalsIgnoreCase("jpeg")){
                    return GraceJSONResult.errorCustom(ResponseStatusEnum.FILE_FORMATTER_FAILD);
                }
                path = uploadService.uploadFdfs(file, suffix);
            }

        }else {
            return GraceJSONResult.errorCustom(ResponseStatusEnum.FILE_UPLOAD_NULL_ERROR);
        }
        if (StringUtils.isNotBlank(path)){
            path = "http://1.15.44.134:8888/" + path;
        }else {
            return GraceJSONResult.errorCustom(ResponseStatusEnum.FILE_UPLOAD_FAILD);
        }
        logger.info("path = {}",path);
        return GraceJSONResult.ok(path);
    }
}
