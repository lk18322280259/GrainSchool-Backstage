package com.atguigu.msmservice.service;

import org.springframework.web.multipart.MultipartFile;

public interface CosService {

    /**
     * 上传头像
     * @param file 前端文件
     * @return 存储对象URL
     */
    String uploadFileAvatar(MultipartFile file);
}
