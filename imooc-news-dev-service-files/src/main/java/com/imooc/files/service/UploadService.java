package com.imooc.files.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface UploadService {

    public String uploadFdfs(MultipartFile file, String fileExtName) throws IOException;
}
