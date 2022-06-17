package com.atguigu.eduservice.controller;


import com.atguigu.commonutils.R;
import com.atguigu.eduservice.entity.subject.OneSubject;
import com.atguigu.eduservice.service.EduSubjectService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * <p>
 * 课程科目 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2022-06-02
 */
@Api("科目管理")
@RestController
@RequestMapping("/eduservice/subject")
@CrossOrigin
public class EduSubjectController {

    @Autowired
    private EduSubjectService eduSubjectService;

    //添加课程分类
    //获取上传过来文件，把文件内容读取过来
    @PostMapping("addSubject")
    @ApiOperation("科目添加接口")
    public R addSubject(
            @ApiParam(name = "file", value = "前端文件", required = true)
            MultipartFile file) {

        //上传过来的文件
        eduSubjectService.saveSubject(file, eduSubjectService);
        return R.ok();
    }

    //课程分类列表（树形）
    @GetMapping("getAllSubject")
    @ApiOperation("科目查询接口")
    public R getAllSubject() {

        //返回一级分类的列表，每个一级分类含有一个列表来存放多个二级分类
        List<OneSubject> list = eduSubjectService.getAllOneTwoSubject();
        return R.ok().data("list", list);
    }
}

