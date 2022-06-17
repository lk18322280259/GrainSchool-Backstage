package com.atguigu.tencent;

import com.atguigu.vod.common.Constant;
import com.qcloud.vod.VodUploadClient;
import com.qcloud.vod.model.VodUploadRequest;
import com.qcloud.vod.model.VodUploadResponse;
import java.util.Random;

public class TestTencent {

    public static void main(String[] args) {

        getSignature();

        //uploadVideo();
    }

    private static void getSignature() {
        Signature sign = new Signature();

        long currentTime = System.currentTimeMillis() / 1000;
        int random = new Random().nextInt(Integer.MAX_VALUE);
        // 设置 App 的云 API 密钥
        sign.setSecretId("AKIDX0V3SjmbGwdM1c8whMxRTXaVHqFYikeL");
        sign.setSecretKey("c5OfwFZoHZlssjZJxhIgu7Nf2SsLuMn6");
        sign.setCurrentTime(currentTime);
        sign.setRandom(random);
        sign.setSignValidDuration(3600 * 24 * 2); // 签名有效期：2天

        try {
            String signature = sign.getUploadSignature();

            System.out.println("当前时间 : " + currentTime);
            System.out.println("random : " + random);
            System.out.println("生成签名 : " + signature);
        } catch (Exception e) {
            System.out.print("获取签名失败");
            e.printStackTrace();
        }
    }

    private static void uploadVideo() {
        String secretId = Constant.secretId;
        String secretKey = Constant.secretKey;

        VodUploadClient client = new VodUploadClient(secretId, secretKey);

        VodUploadRequest request = new VodUploadRequest();
        request.setMediaFilePath("D:\\Java\\项目\\谷粒学苑\\项目资料\\1-阿里云上传测试视频\\6 - What If I Want to Move Faster.mp4");
        request.setCoverFilePath("D:\\File\\图片\\图标\\图图.jpg");
        try {
            VodUploadResponse response = client.upload("ap-guangzhou", request);
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
    }
}
