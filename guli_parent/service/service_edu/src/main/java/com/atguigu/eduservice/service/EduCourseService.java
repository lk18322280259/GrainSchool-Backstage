package com.atguigu.eduservice.service;

import com.atguigu.eduservice.entity.EduCourse;
import com.atguigu.eduservice.entity.frontvo.CourseFrontVo;
import com.atguigu.eduservice.entity.frontvo.CourseWebVo;
import com.atguigu.eduservice.entity.vo.CourseInfoVo;
import com.atguigu.eduservice.entity.vo.CoursePublishVo;
import com.atguigu.eduservice.entity.vo.CourseQueryVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 课程 服务类
 * </p>
 *
 * @author testjava
 * @since 2022-06-12
 */
public interface EduCourseService extends IService<EduCourse> {


    /**
     * 查询课程列表
     * @return 课程列表
     */
    List<EduCourse> getCourseList();

    /**
     * 分页查询课程
     * @param current 当前页
     * @param limit 每页记录数
     * @return 分页结果
     */
    IPage<EduCourse> getPageCourse(long current, long limit);

    /**
     * 带条件分页查询
     * @param current 当前页
     * @param limit 每页记录数
     * @param courseQueryVo 条件查询
     * @return 分页带条件结果
     */
    IPage<EduCourse> getPageCourseCondition(long current, long limit, CourseQueryVo courseQueryVo);

    /**
     * 添加课程基本信息的方法
     * @param courseInfoVo 课程信息封装
     * @return 课程id
     */
    String saveCourseInfo(CourseInfoVo courseInfoVo);

    /**
     * 根据课程id查询课程的基本信息
     * @param courseId 课程id
     * @return 课程信息封装
     */
    CourseInfoVo getCourseInfo(String courseId);

    /**
     * 修改课程信息
     * @param courseInfoVo 课程信息封装
     */
    void updateCourseInfo(CourseInfoVo courseInfoVo);

    /**
     * 根据课程id查询课程确认信息
     * @param courseId 课程id
     * @return 最终发布页面显示数据
     */
    CoursePublishVo publishCourseInfo(String courseId);

    /**
     * 课程最终发布(修改课程状态)
     * @param courseId 课程id
     */
    void updateCourseStatus(String courseId);

    /**
     * 删除课程
     * @param courseId 课程id
     */
    void removeCourse(String courseId);

    /**
     * 修改课程状态
     * @param courseId 课程id
     */
    void changeStatus(String courseId);

    /**
     * 修改浏览数
     * @param id 课程id
     * @param viewCount 修改后的课程浏览数
     */
    void changeViewCount(String id, Long viewCount);

    /**
     * 修改购买数
     * @param id 课程id
     * @param buyCount 修改后的购买数量
     */
    void changeBuyCount(String id, Long buyCount);


    /**
     * 分页带条件查询课程
     * @param coursePage    分页
     * @param courseFrontVo 条件查询
     * @return 分页带条件的课程整合
     */
    Map<String, Object> getCourseFrontList(Page<EduCourse> coursePage, CourseFrontVo courseFrontVo);

    /**
     * 获取课程信息
     * @param id 课程id
     * @return 课程信息
     */
    CourseWebVo selectInfoWebById(String id);

    /**
     * 更新课程浏览数
     * @param id 课程id
     */
    void updatePageViewCount(String id);
}
