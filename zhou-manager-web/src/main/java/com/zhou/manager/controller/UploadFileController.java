package com.zhou.manager.controller;

import com.zhou.utils.FastDFSClient;
import entity.Result;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * @description: 文件上传 controller
 * @author: zhoulei
 * @createTime: 2020-03-02 21:01
 **/
@RequestMapping("/file")
@RestController
public class UploadFileController {

    @Value("${FILE_SERVER_URL}")
    private String FILE_SEVER_URL;

    @RequestMapping("/uploadFile")
    public Result uploadFile(MultipartFile file){
        String fileName = file.getOriginalFilename();
        String fileType = fileName.substring(fileName.indexOf(".") + 1);
        try {
            FastDFSClient fastDFSClient = new FastDFSClient("classpath:config/fdfs_client.conf");
            String fileId = fastDFSClient.uploadFile(file.getBytes(),fileType);
            String url = FILE_SEVER_URL + fileId;
            return new Result(true,url);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"上传错误！");
        }
    }
}
