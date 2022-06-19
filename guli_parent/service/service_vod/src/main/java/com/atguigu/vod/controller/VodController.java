package com.atguigu.vod.controller;


import com.atguigu.commonutils.R;
import com.atguigu.vod.service.VodService;
import com.qcloud.vod.model.VodUploadResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/eduvod/video")
@CrossOrigin
public class VodController {

    @Autowired
    private VodService vodService;

    //上传视频到腾讯云
    @PostMapping("uploadTencentVideo")
    public R uploadTencentVideo(MultipartFile file) {

        VodUploadResponse response = vodService.uploadVideo(file);
        return R.ok();
    }

    //生成签名
    @GetMapping("/getSign")
    public R getUploadSign(){

        String signature = vodService.getUploadSign();
        return R.ok().data("signature", signature);
    }

    //删除视频
    @DeleteMapping("removeTencentVideo/{fileId}")
    public R removeTencentVideoByFileId(@PathVariable String fileId) {

        vodService.removeTencentVideoByFileId(fileId);
        return R.ok();
    }

    //删除章节或者课程下面的多个视频
    @DeleteMapping("delete-batch")
    public R removeTencentVideoByFileIds(@RequestParam("videoList") List<String> videoList) {

        vodService.removeTencentVideoByFileIds(videoList);
        return R.ok();
    }
}
