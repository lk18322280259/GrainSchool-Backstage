package com.atguigu.eduservice.controller;


import com.atguigu.commonutils.R;
import com.atguigu.eduservice.entity.EduVideo;
import com.atguigu.eduservice.service.EduVideoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 课程视频 前端控制器
 * </p>
 *
 * @author luokai
 * @since 2022-06-12
 */
@Api("小节管理")
@RestController
@RequestMapping("/eduservice/video")
@SuppressWarnings("AlibabaCommentsMustBeJavadocFormat")
//@CrossOrigin
public class EduVideoController {

    @Autowired
    private EduVideoService videoService;

    //添加小节
    @PostMapping("addVideo")
    @ApiOperation("添加小节")
    public R addVideo(
            @ApiParam(name = "eduVideo", value = "小节实体", required = true)
            @RequestBody EduVideo eduVideo){

        videoService.addVideo(eduVideo);
        return R.ok();
    }

    //删除小节
    @DeleteMapping("{videoId}")
    @ApiOperation("删除小节")
    public R addVideo(
            @ApiParam(name = "videoId", value = "小节ID", required = true)
            @PathVariable String videoId){

        videoService.deleteById(videoId);
        return R.ok();
    }

    //根据id查询回显小节
    @GetMapping("getVideoInfo/{videoId}")
    @ApiOperation("查询小节")
    public R getVideo(
            @ApiParam(name = "videoId", value = "小节ID", required = true)
            @PathVariable String videoId){

        EduVideo eduVideo = videoService.getVideoById(videoId);
        return R.ok().data("eduVideo",eduVideo);
    }

    //修改小节
    @PostMapping("updateVideo")
    @ApiOperation("修改小节")
    public R updateVideo(
            @ApiParam(name = "eduVideo", value = "小节实体", required = true)
            @RequestBody EduVideo eduVideo){

        videoService.updateVideo(eduVideo);
        return R.ok();
    }

}

