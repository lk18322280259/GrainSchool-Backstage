package com.atguigu.vod.service;

import com.qcloud.vod.model.VodUploadResponse;
import org.springframework.web.multipart.MultipartFile;

public interface VodService {

    //上传视频
    VodUploadResponse uploadVideo(MultipartFile file);

    //生成签名
    String getUploadSign();

}
