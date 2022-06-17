package com.atguigu.eduservice.controller;


import com.atguigu.commonutils.R;
import com.atguigu.eduservice.entity.EduChapter;
import com.atguigu.eduservice.entity.chapter.ChapterVo;
import com.atguigu.eduservice.service.EduChapterService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 课程 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2022-06-12
 */
@Api("章节管理")
@RestController
@RequestMapping("/eduservice/chapter")
@CrossOrigin
public class EduChapterController {

    @Autowired
    private EduChapterService chapterService;

    //课程大纲列表，根据课程id进行查询
    @GetMapping("getChapterVideo/{courseId}")
    @ApiOperation("根据课程id查询章节和小节接口")
    public R getChapterVideo(
            @ApiParam(name = "courseId", value = "课程ID", required = true)
            @PathVariable String courseId) {

        List<ChapterVo> list = chapterService.getChapterVideoByCourseId(courseId);
        return R.ok().data("allChapterVideo", list);
    }

    //添加章节
    @PostMapping("addChapter")
    @ApiOperation("添加章节接口")
    public R addChapter(
            @ApiParam(name = "eduChapter", value = "章节实体", required = true)
            @RequestBody EduChapter eduChapter){

        chapterService.addChapter(eduChapter);
        return R.ok();
    }

    //根据id查询章节
    @GetMapping("getChapterInfo/{chapterId}")
    @ApiOperation("根据id查询章节接口")
    public R getChapterInfo(
            @ApiParam(name = "chapterId", value = "章节ID", required = true)
            @PathVariable String chapterId){

        EduChapter eduChapter = chapterService.getChapterById(chapterId);
        return R.ok().data("chapter",eduChapter);
    }

    //修改章节
    @PostMapping("updateChapter")
    @ApiOperation("修改章节接口")
    public R updateChapter(
            @ApiParam(name = "eduChapter", value = "章节实体", required = true)
            @RequestBody EduChapter eduChapter){

        chapterService.updateChapter(eduChapter);
        return R.ok();
    }

    //删除章节
    @DeleteMapping("{chapterId}")
    @ApiOperation("删除章节接口")
    public R deleteChapter(
            @ApiParam(name = "chapterId", value = "章节ID", required = true)
            @PathVariable String chapterId) {

        chapterService.deleteChapter(chapterId);
        return R.ok();
    }
}

