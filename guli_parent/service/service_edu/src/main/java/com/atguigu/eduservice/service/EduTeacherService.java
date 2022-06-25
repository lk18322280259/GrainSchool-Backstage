package com.atguigu.eduservice.service;

import com.atguigu.eduservice.entity.EduTeacher;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * <p>
 * 讲师 服务类
 * </p>
 *
 * @author testjava
 * @since 2022-05-24
 */
public interface EduTeacherService extends IService<EduTeacher> {

    /**
     * 分页查询讲师
     * @param teacherPage 分页参数
     * @return 分页结果整合
     */
    Map<String, Object> getTeacherFrontList(Page<EduTeacher> teacherPage);

    /**
     * 根据讲师id查询讲师基本信息和所讲课程信息
     * @param teacherId 讲师id
     * @param request 请求对象
     * @return 讲师基本信息和所讲课程信息
     */
    Map<Object, Object> getTeacherFrontInfo(String teacherId, HttpServletRequest request);
}
