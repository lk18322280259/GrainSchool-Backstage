package com.atguigu.eduservice.controller;


import com.atguigu.commonutils.R;
import com.atguigu.eduservice.entity.EduCourse;
import com.atguigu.eduservice.entity.chapter.ChapterVo;
import com.atguigu.eduservice.entity.frontvo.CourseWebVo;
import com.atguigu.eduservice.entity.vo.CourseInfoVo;
import com.atguigu.eduservice.entity.vo.CoursePublishVo;
import com.atguigu.eduservice.entity.vo.CourseQueryVo;
import com.atguigu.eduservice.entity.vo.TeacherQueryVo;
import com.atguigu.eduservice.service.EduChapterService;
import com.atguigu.eduservice.service.EduCourseService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
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
@SuppressWarnings("AlibabaAvoidCommentBehindStatement")
@Api("课程管理")
@RestController
@RequestMapping("/eduservice/course")
@CrossOrigin
public class EduCourseController {

    @Autowired
    private EduCourseService courseService;

    @Autowired
    private EduChapterService chapterService;


    /**
     * 查询课程列表
     * @return 课程列表
     */
    @GetMapping("getAllCourseList")
    @ApiOperation("查询所有课程列表接口")
    public R getAllCourseList() {

        List<EduCourse> list = courseService.getCourseList();
        return R.ok().data("list", list);
    }

    /**
     * 分页查询课程
     * @param current 当前页
     * @param limit 每页记录数
     * @return 分页结果
     */
    @ApiOperation("讲师分页查询接口")
    @GetMapping("pageCourse/{current}/{limit}")
    public R pageCourse(
            @ApiParam(name = "current", value = "当前页", required = true)
            @PathVariable long current,
            @ApiParam(name = "limit", value = "每页条数", required = true)
            @PathVariable long limit) {

        IPage<EduCourse> iPage = courseService.getPageCourse(current, limit);
        //总记录数
        long total = iPage.getTotal();
        //数据
        List<EduCourse> records = iPage.getRecords();

        return R.ok().data("total", total).data("rows", records);
    }

    /**
     * 课程带条件分页
     * @param current 当前页
     * @param limit 每页记录数
     * @param courseQueryVo 条件对象
     * @return 课程带条件分页结果
     */
    @ApiOperation("课程查询带分页接口")
    @PostMapping("pageCourseCondition/{current}/{limit}")
    public R pageCourseCondition(
            @ApiParam(name = "current", value = "当前页", required = true)
            @PathVariable long current,
            @ApiParam(name = "limit", value = "每页条数", required = true)
            @PathVariable long limit,
            @ApiParam(name = "teacherQueryVO", value = "条件查询封装", required = false)
            @RequestBody(required = false) CourseQueryVo courseQueryVo) {

        IPage<EduCourse> iPage = courseService.getPageCourseCondition(current, limit, courseQueryVo);
        //总记录数
        long total = iPage.getTotal();
        //数据
        List<EduCourse> records = iPage.getRecords();

        return R.ok().data("total", total).data("rows", records);
    }

    /**
     * 添加课程基本信息的方法
     * @param courseInfoVo 课程对象
     * @return 课程id
     */
    @PostMapping("addCourseInfo")
    @ApiOperation("课程添加接口")
    public R addCourseInfo(
            @ApiParam(name = "courseInfoVo", value = "课程信息实体", required = true)
            @RequestBody CourseInfoVo courseInfoVo) {

        String id = courseService.saveCourseInfo(courseInfoVo);
        return R.ok().data("courseId",id);
    }

    /**
     * 根据课程id查询课程的基本信息
     * @param courseId 课程id
     * @return 课程信息
     */
    @GetMapping("getCourseInfo/{courseId}")
    @ApiOperation("根据课程id查询课程基本信息接口")
    public R getCourseInfo(
            @ApiParam(name = "courseId", value = "课程ID", required = true)
            @PathVariable String courseId) {

        CourseInfoVo courseInfoVo = courseService.getCourseInfo(courseId);
        return R.ok().data("courseInfoVo", courseInfoVo);
    }

    /**
     * 修改课程信息
     * @param courseInfoVo 课程信息
     * @return 成功
     */
    @PostMapping("updateCourseInfo")
    @ApiOperation("修改课程基本信息接口")
    public R updateCourseInfo(@RequestBody CourseInfoVo courseInfoVo) {
        courseService.updateCourseInfo(courseInfoVo);
        return R.ok();
    }

    /**
     * 根据课程id查询课程确认信息
     * @param courseId 课程id
     * @return 课程发布页面显示信息
     */
    @GetMapping("getPublishCourseInfo/{courseId}")
    @ApiOperation("根据课程id查询课程确认信息接口")
    public R getPublishCourseInfo(
            @ApiParam(name = "courseId", value = "课程ID", required = true)
            @PathVariable String courseId) {

        CoursePublishVo publishVo = courseService.publishCourseInfo(courseId);
        return R.ok().data("publishVo", publishVo);
    }

    /**
     * 课程最终发布(修改课程状态)
     * @param courseId 课程id
     * @return 成功
     */
    @PostMapping("officialPublishCourse/{courseId}")
    @ApiOperation("根据课程id查询课程确认信息接口")
    public R officialPublishCourse(
            @ApiParam(name = "courseId", value = "课程ID", required = true)
            @PathVariable String courseId) {

        courseService.updateCourseStatus(courseId);
        return R.ok();
    }

    /**
     * 删除课程
     * @param courseId 课程id
     * @return 成功
     */
    @DeleteMapping("{courseId}")
    @ApiOperation("删除课程接口")
    public R deleteCourse(
            @ApiParam(name = "courseId", value = "课程ID", required = true)
            @PathVariable String courseId) {

        courseService.removeCourse(courseId);
        return R.ok();
    }

    /**
     * 更改课程状态
     * @param courseId 课程id
     * @return 成功
     */
    @PostMapping("changeStauts/{courseId}")
    @ApiOperation("更改课程状态接口")
    public R changeCourseStauts(
            @ApiParam(name = "courseId", value = "课程ID", required = true)
            @PathVariable String courseId) {

        courseService.changeStatus(courseId);
        return R.ok();
    }

    /**
     * 修改浏览数
     * @param id 课程id
     * @param viewCount 浏览数
     * @return 成功
     */
    @PostMapping("changeViewCount/{id}/{viewCount}")
    @ApiOperation("更改课程状态接口")
    public R changeViewCount(
            @ApiParam(name = "id", value = "课程ID", required = true)
            @PathVariable String id,
            @ApiParam(name = "viewCount", value = "浏览数", required = true)
            @PathVariable Long viewCount) {

        courseService.changeViewCount(id, viewCount);
        return R.ok();
    }

    /**
     * 修改购买数
     * @param id 课程id
     * @param buyCount 购买数
     * @return 成功
     */
    @PostMapping("changeBuyCount/{id}/{buyCount}")
    @ApiOperation("更改课程状态接口")
    public R changeBuyCount(
            @ApiParam(name = "id", value = "课程ID", required = true)
            @PathVariable String id,
            @ApiParam(name = "buyCount", value = "购买数", required = true)
            @PathVariable Long buyCount) {

        courseService.changeBuyCount(id, buyCount);
        return R.ok();
    }

    /**
     * 根据ID查询课程
     * @param courseId 课程id
     * @return 课程信息
     */
    @GetMapping(value = "getCourseDetailInfoById/{courseId}")
    @ApiOperation(value = "根据课程id查询课程详细信息(章节|视频|讲师等等)")
    public R getCourseDetailInfoById(
            @ApiParam(name = "courseId", value = "课程ID", required = true)
            @PathVariable String courseId){

        //查询课程信息和讲师信息
        CourseWebVo courseWebVo = courseService.selectInfoWebById(courseId);

        //查询当前课程的章节信息
        List<ChapterVo> chapterVoList = chapterService.getChapterVideoByCourseId(courseId);

        return R.ok().data("course", courseWebVo).data("chapterVoList", chapterVoList);
    }

}
