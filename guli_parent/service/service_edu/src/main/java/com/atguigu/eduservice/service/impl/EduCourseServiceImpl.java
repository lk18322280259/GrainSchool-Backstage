package com.atguigu.eduservice.service.impl;

import com.atguigu.eduservice.client.VodClient;
import com.atguigu.eduservice.entity.*;
import com.atguigu.eduservice.entity.frontvo.CourseFrontVo;
import com.atguigu.eduservice.entity.frontvo.CourseWebVo;
import com.atguigu.eduservice.entity.vo.CourseInfoVo;
import com.atguigu.eduservice.entity.vo.CoursePublishVo;
import com.atguigu.eduservice.entity.vo.CourseQueryVo;
import com.atguigu.eduservice.mapper.EduCourseMapper;
import com.atguigu.eduservice.service.EduChapterService;
import com.atguigu.eduservice.service.EduCourseDescriptionService;
import com.atguigu.eduservice.service.EduCourseService;
import com.atguigu.eduservice.service.EduVideoService;
import com.atguigu.servicebase.exceptionhandler.GuliException;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 课程 服务实现类
 * </p>
 *
 * @author testjava
 * @since 2022-06-12
 */
@Service
@SuppressWarnings({"unchecked", "SpringJavaAutowiredFieldsWarningInspection", "DuplicatedCode"})
public class EduCourseServiceImpl extends ServiceImpl<EduCourseMapper, EduCourse> implements EduCourseService {

    @Autowired
    private EduCourseDescriptionService courseDescriptionService;

    @Autowired
    private EduChapterService chapterService;

    @Autowired
    private EduVideoService eduVideoService;

    @Autowired
    private EduCourseDescriptionService descriptionService;

    @Autowired
    private EduCourseMapper eduCourseMapper;

    /**
     * 微服务videoVod
     */
    @Autowired
    private VodClient vodClient;


    /**
     * 查询课程列表
     * @return 课程列表
     */
    @Override
    public List<EduCourse> getCourseList() {
        LambdaQueryWrapper<EduCourse> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(EduCourse::getIsDeleted, 0);
        wrapper.orderByDesc(EduCourse::getGmtModified).orderByAsc(EduCourse::getGmtCreate);

        List<EduCourse> courseList = this.list(wrapper);
        if (courseList==null) {
            throw new GuliException(20001, "查询失败");
        }
        return courseList;
    }

    /**
     * 分页查询课程
     * @param current 当前页
     * @param limit 每页记录数
     * @return 分页结果
     */
    @Override
    public IPage<EduCourse> getPageCourse(long current, long limit) {

        //创建page对象
        Page<EduCourse> pageCourse = new Page<>(current, limit);

        LambdaQueryWrapper<EduCourse> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(EduCourse::getIsDeleted, 0);

        //调用方法实现分页
        IPage<EduCourse> iPage = this.page(pageCourse, wrapper);
        if (iPage==null) {
            throw new GuliException(20001,"分页查询失败");
        }
        return iPage;
    }

    /**
     * 带条件分页查询
     * @param current 当前页
     * @param limit 每页记录数
     * @param courseQueryVo 条件查询
     * @return 分页带条件结果
     */
    @Override
    public IPage<EduCourse> getPageCourseCondition(long current, long limit, CourseQueryVo courseQueryVo) {

        //创建page对象
        Page<EduCourse> pageCourse = new Page<>(current, limit);
        //构建条件
        LambdaQueryWrapper<EduCourse> wrapper = new LambdaQueryWrapper<>();

        String title = courseQueryVo.getTitle();
        String status = courseQueryVo.getStatus();
        String beginCreate = courseQueryVo.getBeginCreate();
        String endCreate = courseQueryVo.getEndCreate();
        String beginModified = courseQueryVo.getBeginModified();
        String endModified = courseQueryVo.getEndModified();

        //封装wrapper
        wrapper.like(StringUtils.isNotEmpty(title),EduCourse::getTitle, title);
        wrapper.eq(StringUtils.isNotEmpty(status),EduCourse::getStatus, status);

        wrapper.ge(StringUtils.isNotEmpty(beginCreate),EduCourse::getGmtCreate, beginCreate);
        wrapper.le(StringUtils.isNotEmpty(endCreate),EduCourse::getGmtCreate, endCreate);

        wrapper.ge(StringUtils.isNotEmpty(beginModified),EduCourse::getGmtModified, beginModified);
        wrapper.le(StringUtils.isNotEmpty(endModified),EduCourse::getGmtModified, endModified);

        wrapper.eq(EduCourse::getIsDeleted, 0);

        //按照修改时间降序排列
        wrapper.orderByDesc(EduCourse::getGmtModified).orderByAsc(EduCourse::getGmtCreate);

        //调用方法实现分页
        IPage<EduCourse> iPage = this.page(pageCourse, wrapper);
        if (iPage==null) {
            throw new GuliException(20001,"分页查询失败");
        }
        return iPage;
    }

    /**
     * 添加课程基本信息的方法
     * @param courseInfoVo 课程信息封装
     * @return 课程id
     */
    @Override
    @Transactional
    public String saveCourseInfo(CourseInfoVo courseInfoVo) {

        //1 向课程表添加课程基本信息
        //CourseInfoVo对象转换eduCourse对象
        EduCourse eduCourse = new EduCourse();
        BeanUtils.copyProperties(courseInfoVo, eduCourse);
        boolean saveEduCourse = this.save(eduCourse);

        if (!saveEduCourse) {
            //添加失败
            throw new GuliException(20001, "添加课程信息失败");
        }

        //获取添加之后的课程id
        String courseId = eduCourse.getId();

        //2 向课程简介表添加课程简介
        EduCourseDescription courseDescription = new EduCourseDescription();
        courseDescription.setDescription(courseInfoVo.getDescription());
        courseDescription.setId(courseId);
        boolean saveEduCourseDescription = courseDescriptionService.save(courseDescription);

        if (!saveEduCourseDescription) {
            //添加失败
            throw new GuliException(20001, "添加课程描述信息失败");
        }
        return courseId;
    }

    /**
     * 根据课程id查询课程的基本信息
     * @param courseId 课程id
     * @return 课程信息封装
     */
    @Override
    public CourseInfoVo getCourseInfo(String courseId) {

        //创建返回数据对象
        CourseInfoVo courseInfoVo = new CourseInfoVo();

        //1 查询课程表
        EduCourse eduCourse = this.getById(courseId);
        BeanUtils.copyProperties(eduCourse, courseInfoVo);

        //2 查询描述表
        EduCourseDescription courseDescription = courseDescriptionService.getById(courseId);
        BeanUtils.copyProperties(courseDescription, courseInfoVo);

        return courseInfoVo;
    }

    /**
     * 修改课程信息
     * @param courseInfoVo 课程信息封装
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateCourseInfo(CourseInfoVo courseInfoVo) {

        //1 修改课程表
        //创建EduCourse，并赋值
        EduCourse eduCourse = new EduCourse();
        BeanUtils.copyProperties(courseInfoVo, eduCourse);
        boolean saveEduCourse = this.updateById(eduCourse);
        if (!saveEduCourse) {
            throw new GuliException(20001,"课程表修改失败");
        }

        //2 修改描述表
        EduCourseDescription courseDescription = new EduCourseDescription();
        BeanUtils.copyProperties(courseInfoVo, courseDescription);
        boolean saveEduCourseDescription = courseDescriptionService.updateById(courseDescription);
        if (!saveEduCourseDescription) {
            throw new GuliException(20001,"描述表修改失败");
        }

    }

    /**
     * 根据课程id查询课程确认信息
     * @param courseId 课程id
     * @return 最终发布页面显示数据
     */
    @Override
    public CoursePublishVo publishCourseInfo(String courseId) {

        CoursePublishVo publishCourseInfo = eduCourseMapper.getPublishCourseInfo(courseId);
        if (publishCourseInfo==null) {
            throw new GuliException(20001, "查询课程最终发布信息失败");
        }
        return publishCourseInfo;
    }

    /**
     * 课程最终发布(修改课程状态)
     * @param courseId 课程id
     */
    @Override
    public void updateCourseStatus(String courseId) {

        EduCourse eduCourse = new EduCourse();
        eduCourse.setId(courseId);
        eduCourse.setStatus("Normal");
        boolean update = this.updateById(eduCourse);

        if (!update) {
            throw new GuliException(20001, "课程发布失败");
        }
    }

    /**
     * 删除课程
     * @param courseId 课程id
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeCourse(String courseId) {

        //先查询磕碜是否发布，发布的课程不允许删除
        EduCourse eduCourse = this.getById(courseId);
        String normal = "Normal";
        if (normal.equals(eduCourse.getStatus())) {
            throw new GuliException(20001, "课程已发布，无法删除");
        }

        //1 根据课程id删除视频及课程小节
        eduVideoService.removeVideoByCourseId(courseId);

        //2 根据课程id删除章节
        chapterService.removeChapterByCourseId(courseId);

        //3 根据课程id删除描述
        descriptionService.removeDescriptionByCourseId(courseId);

        //4 根据课程id删除课程
        boolean remove = this.removeById(courseId);
        if (!remove) {
            throw new GuliException(20001, "删除课程失败");
        }
    }

    /**
     * 修改课程状态
     * @param courseId 课程id
     */
    @Override
    public void changeStatus(String courseId) {

        EduCourse eduCourse = this.getById(courseId);

        if ("Draft".equals(eduCourse.getStatus())) {
            eduCourse.setStatus("Normal");
        } else if ("Normal".equals(eduCourse.getStatus())) {
            eduCourse.setStatus("Draft");
        } else {
            throw new GuliException(20001, "未知异常");
        }

        boolean save = this.updateById(eduCourse);
        if (!save) {
            throw new GuliException(20001, "状态修改异常");
        }
    }

    /**
     * 修改浏览数
     * @param id 课程id
     * @param viewCount 修改后的课程浏览数
     */
    @Override
    public void changeViewCount(String id, Long viewCount) {

        EduCourse eduCourse = new EduCourse();
        eduCourse.setId(id);
        eduCourse.setViewCount(viewCount);

        boolean update = this.updateById(eduCourse);
        if (!update) {
            throw new GuliException(20001, "浏览数修改失败");
        }
    }

    /**
     * 修改购买数
     * @param id 课程id
     * @param buyCount 修改后的购买数量
     */
    @Override
    public void changeBuyCount(String id, Long buyCount) {

        EduCourse eduCourse = new EduCourse();
        eduCourse.setId(id);
        eduCourse.setBuyCount(buyCount);

        boolean update = this.updateById(eduCourse);
        if (!update) {
            throw new GuliException(20001, "购买数修改失败");
        }
    }

    /**
     * 分页带条件查询课程
     * @param coursePage    分页
     * @param courseFrontVo 条件查询
     * @return 分页带条件的课程整合
     */
    @Override
    public Map<String, Object> getCourseFrontList(Page<EduCourse> coursePage, CourseFrontVo courseFrontVo) {

        LambdaQueryWrapper<EduCourse> wrapper = new LambdaQueryWrapper<>();
        String publishStatus = "Normal";
        wrapper.eq(EduCourse::getStatus, publishStatus);
        wrapper.eq(StringUtils.isNotEmpty(courseFrontVo.getSubjectParentId()), EduCourse::getSubjectParentId, courseFrontVo.getSubjectParentId());
        wrapper.eq(StringUtils.isNotEmpty(courseFrontVo.getSubjectId()), EduCourse::getSubjectId, courseFrontVo.getSubjectId());


        if (StringUtils.isNotEmpty(courseFrontVo.getBuyCountSort())){
            wrapper.orderByDesc(EduCourse::getBuyCount);
        }
        if (StringUtils.isNotEmpty(courseFrontVo.getGmtCreateSort())){
            wrapper.orderByDesc(EduCourse::getGmtCreate);
        }
        if (StringUtils.isNotEmpty(courseFrontVo.getPriceSort())){
            wrapper.orderByDesc(EduCourse::getPrice);
        }

        this.page(coursePage, wrapper);

        List<EduCourse> records = coursePage.getRecords();
        long current = coursePage.getCurrent();
        long pages = coursePage.getPages();
        long size = coursePage.getSize();
        long total = coursePage.getTotal();
        boolean hasNext = coursePage.hasNext();
        boolean hasPrevious = coursePage.hasPrevious();

        Map<String, Object> map = new HashMap<>();
        map.put("items", records);
        map.put("current", current);
        map.put("pages", pages);
        map.put("size", size);
        map.put("total", total);
        map.put("hasNext", hasNext);
        map.put("hasPrevious", hasPrevious);

        return map;
    }

    /**
     * 获取课程信息
     * @param id 课程id
     * @return 课程信息
     */
    @Override
    public CourseWebVo selectInfoWebById(String id) {

        this.updatePageViewCount(id);
        return eduCourseMapper.selectInfoWebById(id);
    }

    /**
     * 自增课程浏览数
     * @param id 课程id
     */
    @Override
    public void updatePageViewCount(String id) {

        EduCourse eduCourse = this.getById(id);
        Long viewCount = eduCourse.getViewCount();
        this.changeViewCount(id,++viewCount);
    }

}
