package com.atguigu.vod.service.impl;

import com.atguigu.servicebase.exceptionhandler.GuliException;
import com.atguigu.vod.common.Constant;
import com.atguigu.vod.entity.Signature;
import com.atguigu.vod.service.VodService;
import com.qcloud.vod.VodUploadClient;
import com.qcloud.vod.model.VodUploadRequest;
import com.qcloud.vod.model.VodUploadResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Random;

@Service
public class VodServiceImpl implements VodService {

    public final String secretId = Constant.secretId;
    public final String secretKey = Constant.secretKey;
    public final String region = Constant.region;

    @Override
    public VodUploadResponse uploadVideo(MultipartFile file) {

        VodUploadClient client = new VodUploadClient(secretId, secretKey);

        VodUploadRequest request = new VodUploadRequest();

        request.setMediaFilePath("D:\\Java\\项目\\谷粒学苑\\项目资料\\1-阿里云上传测试视频\\6 - What If I Want to Move Faster.mp4");

        request.setCoverFilePath("D:\\File\\图片\\图标\\图图.jpg");

        VodUploadResponse response = null;
        try {
            response = client.upload(region, request);
            String fileId = response.getFileId();
            String mediaUrl = response.getMediaUrl();
            String requestId = response.getRequestId();
            String coverUrl = response.getCoverUrl();
            System.out.println("fileId: "+ fileId);
            System.out.println("mediaUrl: "+ mediaUrl);
            System.out.println("requestId: "+ requestId);
            System.out.println("coverUrl: "+ coverUrl);
        } catch (Exception e) {
            // 业务方进行异常处理
            e.printStackTrace();
        }
        return response;
    }

    //生成签名
    @Override
    public String getUploadSign() {

        Signature sign = new Signature();

        long currentTime = System.currentTimeMillis() / 1000;
        int random = new Random().nextInt(java.lang.Integer.MAX_VALUE);
        int signValidDuration = 3600 * 24 * 2; // 签名有效期：2天
        // 设置 App 的云 API 密钥
        sign.setSecretId(secretId);
        sign.setSecretKey(secretKey);
        sign.setCurrentTime(currentTime);
        sign.setRandom(random);
        sign.setSignValidDuration(signValidDuration); // 签名有效期：2天

        String signature = null;
        try {
            signature = sign.getUploadSignature();

            System.out.println("当前时间 : " + currentTime);
            System.out.println("random : " + random);
            System.out.println("生成签名 : " + signature);
        } catch (Exception e) {
            System.out.print("获取签名失败");
            e.printStackTrace();
        }

        if (signature==null) {
            throw new GuliException(20001, "签名生成异常");
        }
        return signature;
    }

}
