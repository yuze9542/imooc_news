package com.imooc.files.controller;

import com.imooc.api.controller.files.FileUploadControllerApi;
import com.imooc.exception.GraceException;
import com.imooc.files.FileResource;
import com.imooc.files.GridFSConfig;
import com.imooc.files.service.UploadService;
import com.imooc.grace.result.GraceJSONResult;
import com.imooc.grace.result.ResponseStatusEnum;
import com.imooc.pojo.bo.NewAdminBO;
import com.imooc.utils.FileUtils;
import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSFindIterable;
import com.mongodb.client.gridfs.model.GridFSFile;
import com.mongodb.client.model.Filters;
import org.apache.commons.lang3.StringUtils;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

@RestController
public class FileUploadController implements FileUploadControllerApi {

    final static Logger logger = LoggerFactory.getLogger(FileUploadController.class);

    @Autowired
    private UploadService uploadService;

    @Autowired
    private FileResource fileResource;

    @Autowired
    private GridFSBucket gridFSBucket;

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

    @Override
    public GraceJSONResult uploadToGridFS(NewAdminBO bo) throws IOException {
        // 获得图片的base64
        String img64 = bo.getImg64();

        // 将 base64 字符串转换为byte数组
        byte[] bytes = new BASE64Decoder().decodeBuffer(img64.trim());

        //转换为输入流
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);

        // 上传到gridfs中
        ObjectId fileId = gridFSBucket.uploadFromStream(bo.getUsername() + ".png", byteArrayInputStream);

        // 获得文件在gridfs的主键id
        String fileIdStr = fileId.toString();

        return GraceJSONResult.ok(fileIdStr);
    }

    @Override
    public void readInGridFS(String faceId,
                                        HttpServletRequest request,
                                        HttpServletResponse response) throws IOException {

        // 1  判断是否为空
        if (StringUtils.isBlank(faceId) || faceId.equalsIgnoreCase("null")){
             GraceException.display(ResponseStatusEnum.FILE_NOT_EXIST_ERROR);
        }

        // 2 从gridFS读取
        File adminFace = readGridFSByFaceId(faceId);

        // 3 把人脸图片输出到浏览器
        FileUtils.downloadFileByStream(response,adminFace);
    }

    @Override
    public GraceJSONResult readFace64InGridFS(String faceId, HttpServletRequest request, HttpServletResponse response) throws IOException {

        // 0 获得gridfs中人脸文件
        File myfile = readGridFSByFaceId(faceId);

        // 1 转换文件为base64
        String s = FileUtils.fileToBase64(myfile);
        return GraceJSONResult.ok(s);
    }

    /**
     * 下载人脸
     * @param faceId
     * @return
     * @throws FileNotFoundException
     */
    private File readGridFSByFaceId(String faceId) throws FileNotFoundException {

        GridFSFindIterable id = gridFSBucket.find(Filters.eq("_id", new ObjectId(faceId)));// 条件和faceId做匹配
        GridFSFile GridFS = id.first();//列表拿第一个
        if (GridFS == null){
            GraceException.display(ResponseStatusEnum.FILE_NOT_EXIST_ERROR);
        }
        String fileName = GridFS.getFilename();
        File file = new File("/home/yuze/IdeaProjects/imooc_news/temp");
        if (!file.exists()){
            file.mkdirs();
        }
        File myFile = new File("/home/yuze/IdeaProjects/imooc_news/temp/"+fileName);
        // 创建输出流
        FileOutputStream outputStream = new FileOutputStream(myFile);
        // 下载到服务器或 本地
        gridFSBucket.downloadToStream(new ObjectId(faceId),outputStream);
        return myFile;

    }
}
