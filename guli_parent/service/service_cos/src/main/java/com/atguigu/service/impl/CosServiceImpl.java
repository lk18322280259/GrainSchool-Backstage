package com.atguigu.service.impl;

import com.atguigu.common.Constant;
import com.atguigu.service.CosService;
import com.atguigu.servicebase.exceptionhandler.GuliException;
import com.qcloud.cos.model.ObjectMetadata;
import com.qcloud.cos.model.PutObjectRequest;
import com.qcloud.cos.model.UploadResult;
import com.qcloud.cos.transfer.TransferManager;
import com.qcloud.cos.transfer.Upload;
import org.joda.time.DateTime;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

import static com.atguigu.utils.CosUtils.createTransferManager;
import static com.atguigu.utils.CosUtils.shutdownTransferManager;

@Service
public class CosServiceImpl implements CosService {

    public final static String tencentRegion = Constant.region;
    public final static String tencentBucketName = Constant.bucketName;
    public final static String tencentFilePath = Constant.filePath;

    //上传头像
    @Override
    public String uploadFileAvatar(MultipartFile file) {

        // 使用高级接口必须先保证本进程存在一个 TransferManager 实例，如果没有则创建
        // 详细代码参见本页：高级接口 -> 创建 TransferManager
        TransferManager transferManager = createTransferManager();

        // 存储桶的命名格式为 BucketName-APPID，此处填写的存储桶名称必须为此格式
        String bucketName = tencentBucketName;

        //构建文件名
        //构建日期路径：guli-edu/avatar/2022-06-01
        String filePathPrefix = tencentFilePath + new DateTime().toString("yyyy/MM/dd");

        //文件名：uuid.扩展名
        String original = file.getOriginalFilename();
        String fileName = UUID.randomUUID().toString().replaceAll("-", "");
        assert original != null;
        String fileType = original.substring(original.lastIndexOf("."));
        String filePathSuffix = fileName + fileType;
        //完成路径 guli-edu/avatar/2022-06-01/8c1374e52ea748b09779075e851657b0.bmp
        // 对象键(Key)是对象在存储桶中的唯一标识。
        String key = filePathPrefix + "/" + filePathSuffix;

        try {
            //获取上传文件流
            InputStream inputStream = file.getInputStream();

            long inputStreamLength = 0;
            while (inputStreamLength == 0) {
                inputStreamLength = inputStream.available();
            }

            ObjectMetadata objectMetadata = new ObjectMetadata();
            // 上传的流如果能够获取准确的流长度，则推荐一定填写 content-length
            // 如果确实没办法获取到，则下面这行可以省略，但同时高级接口也没办法使用分块上传了
            objectMetadata.setContentLength(inputStreamLength);

            PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, key, inputStream, objectMetadata);
            // 高级接口会返回一个异步结果Upload
            // 可同步地调用 waitForUploadResult 方法等待上传完成，成功返回UploadResult, 失败抛出异常
            Upload upload = transferManager.upload(putObjectRequest);
            //返回数据
            UploadResult uploadResult = upload.waitForUploadResult();

            //上传图片成功标志 ETag="ad2d870ffebeb029e55027e24a931f68"
            String eTag = uploadResult.getETag();
            //返回路径
            String url = null;
            if (eTag==null) {
                throw new GuliException(20001,"头像上传失败");
            }


        } catch (IOException |InterruptedException e) {
            throw new GuliException(20001,"头像上传失败");
        }

        // 确定本进程不再使用 transferManager 实例之后，关闭之
        // 详细代码参见本页：高级接口 -> 关闭 TransferManager
        shutdownTransferManager(transferManager);

        //构建图片路径
        //对象地址 https://lk-1303842271.cos.ap-beijing.myqcloud.com/guli-edu/avatar/2022/06/17/97b2e244271a4e3fafc2582c5701ec1c.png
        return "https://" + bucketName + ".cos."+ tencentRegion +".myqcloud.com/" + key;
    }
}
