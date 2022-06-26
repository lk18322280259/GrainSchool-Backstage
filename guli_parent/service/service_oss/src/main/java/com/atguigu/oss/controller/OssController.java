package com.atguigu.oss.controller;

import com.atguigu.commonutils.R;
import com.atguigu.oss.service.OssService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * @Author luokai
 */
@Api("阿里云Oss管理")
@RestController
@RequestMapping("/eduoss/fileoss")
@SuppressWarnings("AlibabaCommentsMustBeJavadocFormat")
//@CrossOrigin
public class OssController {

    @Autowired
    private OssService ossService;

    //上传头像方法
    @PostMapping
    @ApiOperation("Oss文件上传接口")
    public R uploadOssFile(
            @ApiParam(name = "file", value = "前端文件", required = true)
            MultipartFile file) {

        //获取上传文件 MultipartFile
        //返回上传的图片oss路径
        String url = ossService.uploadFileAvatar(file);

        return R.ok().data("url", url);
    }
}
