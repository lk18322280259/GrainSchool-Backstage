package com.atguigu.vod.service.impl;

import com.atguigu.servicebase.exceptionhandler.GuliException;
import com.atguigu.vod.common.Constant;
import com.atguigu.vod.entity.Signature;
import com.atguigu.vod.service.VodService;
import com.qcloud.vod.VodUploadClient;
import com.qcloud.vod.model.VodUploadRequest;
import com.qcloud.vod.model.VodUploadResponse;
import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;
import com.tencentcloudapi.vod.v20180717.VodClient;
import com.tencentcloudapi.vod.v20180717.models.DeleteMediaRequest;
import com.tencentcloudapi.vod.v20180717.models.DeleteMediaResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Random;

@Service
public class VodServiceImpl implements VodService {

    public final static String secretId = Constant.secretId;
    public final static String secretKey = Constant.secretKey;
    public final static String region = Constant.region;

    //上传视频
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

    //生成签名返回前端上传视频
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

    //删除腾讯云视频
    @Override
    public void removeTencentVideoByFileId(String fileId) {

        try {
            // 实例化一个认证对象，入参需要传入腾讯云账户secretId，secretKey,此处还需注意密钥对的保密
            // 密钥可前往https://console.cloud.tencent.com/cam/capi网站进行获取
            Credential cred = new Credential(secretId, secretKey);
            // 实例化一个http选项，可选的，没有特殊需求可以跳过
            HttpProfile httpProfile = new HttpProfile();
            httpProfile.setEndpoint("vod.tencentcloudapi.com");
            // 实例化一个client选项，可选的，没有特殊需求可以跳过
            ClientProfile clientProfile = new ClientProfile();
            clientProfile.setHttpProfile(httpProfile);
            // 实例化要请求产品的client对象,clientProfile是可选的
            VodClient client = new VodClient(cred, region, clientProfile);
            // 实例化一个请求对象,每个接口都会对应一个request对象
            DeleteMediaRequest req = new DeleteMediaRequest();
            req.setFileId(fileId);
            // 返回的resp是一个DeleteMediaResponse的实例，与请求对象对应
            DeleteMediaResponse resp = client.DeleteMedia(req);
            // 输出json格式的字符串回包
            String result = DeleteMediaResponse.toJsonString(resp);
            System.out.println(result);
        } catch ( TencentCloudSDKException e) {
            throw new GuliException(20001, "删除腾讯云视频异常");
        }
    }

    @Override
    public void removeTencentVideoByFileIds(List<String> videoList) {

            // 实例化一个认证对象，入参需要传入腾讯云账户secretId，secretKey,此处还需注意密钥对的保密
            // 密钥可前往https://console.cloud.tencent.com/cam/capi网站进行获取
            Credential cred = new Credential(secretId, secretKey);
            // 实例化一个http选项，可选的，没有特殊需求可以跳过
            HttpProfile httpProfile = new HttpProfile();
            httpProfile.setEndpoint("vod.tencentcloudapi.com");
            // 实例化一个client选项，可选的，没有特殊需求可以跳过
            ClientProfile clientProfile = new ClientProfile();
            clientProfile.setHttpProfile(httpProfile);
            // 实例化要请求产品的client对象,clientProfile是可选的
            VodClient client = new VodClient(cred, region, clientProfile);
            // 实例化一个请求对象,每个接口都会对应一个request对象
            DeleteMediaRequest req = new DeleteMediaRequest();

            for (String video : videoList) {
                req.setFileId(video);
                try {
                    // 返回的resp是一个DeleteMediaResponse的实例，与请求对象对应
                    client.DeleteMedia(req);
                } catch ( TencentCloudSDKException e) {
                    throw new GuliException(20001, "删除腾讯云视频异常");
                }
            }

    }

}
