package com.imooc.files.service.Impl;

import com.github.tobato.fastdfs.domain.fdfs.StorePath;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import com.imooc.files.service.UploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
@Service
public class UploadServiceImpl implements UploadService {


    @Autowired
    public FastFileStorageClient fastFileStorageClient; // 上传 相关

    @Override
    public String uploadFdfs(MultipartFile file,String fileExtName) throws IOException {
        StorePath storePath = fastFileStorageClient.uploadFile(file.getInputStream(),
                file.getSize(),
                fileExtName,
                null);
        String fullPath = storePath.getFullPath();
        return fullPath;
    }
}
