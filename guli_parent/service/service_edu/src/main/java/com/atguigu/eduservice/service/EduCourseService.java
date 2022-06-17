package com.atguigu.eduservice.service;

import com.atguigu.eduservice.entity.EduCourse;
import com.atguigu.eduservice.entity.vo.CourseInfoVo;
import com.atguigu.eduservice.entity.vo.CoursePublishVo;
import com.atguigu.eduservice.entity.vo.CourseQueryVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 课程 服务类
 * </p>
 *
 * @author testjava
 * @since 2022-06-12
 */
public interface EduCourseService extends IService<EduCourse> {

    //查询课程列表
    List<EduCourse> getCourseList();

    //分页查询课程
    IPage<EduCourse> getPageCourse(long current, long limit);

    //带条件分页查询
    IPage<EduCourse> getPageCourseCondition(long current, long limit, CourseQueryVo courseQueryVo);

    //添加课程基本信息的方法
    String saveCourseInfo(CourseInfoVo courseInfoVo);

    //根据课程id查询课程的基本信息
    CourseInfoVo getCourseInfo(String courseId);

    //修改课程信息
    void updateCourseInfo(CourseInfoVo courseInfoVo);

    //根据课程id查询课程确认信息
    CoursePublishVo publishCourseInfo(String courseId);

    //课程最终发布(修改课程状态)
    void updateCourseStatus(String courseId);

    //删除课程
    void removeCourse(String courseId);

    //修改课程状态
    void changeStatus(String courseId);

    //修改浏览数
    void changeViewCount(String id, Long viewCount);

    //修改购买数
    void changeBuyCount(String id, Long buyCount);
}
