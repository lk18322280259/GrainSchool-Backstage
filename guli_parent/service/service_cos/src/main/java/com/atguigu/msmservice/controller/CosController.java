package com.atguigu.msmservice.controller;

import com.atguigu.commonutils.R;
import com.atguigu.msmservice.service.CosService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Api("腾讯云Cos管理")
@RestController
@RequestMapping("/educos/filecos")
@CrossOrigin
public class CosController {

    @Autowired
    private CosService cosService;

    //上传头像方法
    @PostMapping
    @ApiOperation("Cos文件上传接口")
    public R uploadOssFile(
            @ApiParam(name = "file", value = "前端文件", required = true)
            MultipartFile file) {

        //获取上传文件 MultipartFile
        //返回上传的图片oss路径
        String url = cosService.uploadFileAvatar(file);

        return R.ok().data("url", url);
    }
}
