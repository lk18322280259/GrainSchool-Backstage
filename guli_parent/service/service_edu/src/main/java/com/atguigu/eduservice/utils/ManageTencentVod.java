package com.atguigu.eduservice.utils;

import com.atguigu.servicebase.exceptionhandler.GuliException;
import com.atguigu.eduservice.common.Constant;
import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.vod.v20180717.VodClient;
import com.tencentcloudapi.vod.v20180717.models.*;

/**
 * @Author luokai
 */
public class ManageTencentVod
{

    public final static String SECRET_ID = Constant.secretId;
    public final static String SECRET_KEY = Constant.secretKey;
    public final static String REGION = Constant.region;

    public static Boolean deleteVod(String fileId) {

        try{
            // 实例化一个认证对象，入参需要传入腾讯云账户secretId，secretKey,此处还需注意密钥对的保密
            // 密钥可前往https://console.cloud.tencent.com/cam/capi网站进行获取
            Credential cred = new Credential(SECRET_ID, SECRET_KEY);
            // 实例化一个http选项，可选的，没有特殊需求可以跳过
            HttpProfile httpProfile = new HttpProfile();
            httpProfile.setEndpoint("vod.tencentcloudapi.com");
            // 实例化一个client选项，可选的，没有特殊需求可以跳过
            ClientProfile clientProfile = new ClientProfile();
            clientProfile.setHttpProfile(httpProfile);
            // 实例化要请求产品的client对象,clientProfile是可选的
            VodClient client = new VodClient(cred, REGION, clientProfile);
            // 实例化一个请求对象,每个接口都会对应一个request对象
            DeleteMediaRequest req = new DeleteMediaRequest();
            req.setFileId(fileId);
            // 返回的resp是一个DeleteMediaResponse的实例，与请求对象对应
            DeleteMediaResponse resp = client.DeleteMedia(req);
            // 输出json格式的字符串回包
            String result = DeleteMediaResponse.toJsonString(resp);
            System.out.println(result);

            return true;
        } catch (TencentCloudSDKException e) {
            throw new GuliException(20001, "视频删除失败");
        }

    }
}
