package com.atguigu.vod.service;

import com.qcloud.vod.model.VodUploadResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface VodService {

    //上传视频
    VodUploadResponse uploadVideo(MultipartFile file);

    //生成签名
    String getUploadSign();

    //删除腾讯云视频
    void removeTencentVideoByFileId(String fileId);

    //删除多个视频
    void removeTencentVideoByFileIds(List<String> videoList);

}
