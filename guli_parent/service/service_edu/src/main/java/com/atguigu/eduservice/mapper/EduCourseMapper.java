package com.atguigu.eduservice.mapper;

import com.atguigu.eduservice.entity.EduCourse;
import com.atguigu.eduservice.entity.frontvo.CourseWebVo;
import com.atguigu.eduservice.entity.vo.CoursePublishVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.mapstruct.Mapper;

/**
 * <p>
 * 课程 Mapper 接口
 * </p>
 *
 * @author testjava
 * @since 2022-06-12
 */
@Mapper
public interface EduCourseMapper extends BaseMapper<EduCourse> {

    /**
     * 根据课程id查询课程信息
     * @param courseId 课程id
     * @return 发布信息
     */
    public CoursePublishVo getPublishCourseInfo(String courseId);

    /**
     * 课程学习详情界面
     * @param courseId 课程id
     * @return 课程学习详情界面
     */
    CourseWebVo selectInfoWebById(String courseId);
}
