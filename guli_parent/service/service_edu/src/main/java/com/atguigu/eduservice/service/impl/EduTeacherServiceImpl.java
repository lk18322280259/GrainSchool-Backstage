package com.atguigu.eduservice.service.impl;

import com.atguigu.commonutils.JwtUtils;
import com.atguigu.eduservice.entity.EduCourse;
import com.atguigu.eduservice.entity.EduTeacher;
import com.atguigu.eduservice.mapper.EduTeacherMapper;
import com.atguigu.eduservice.service.EduCourseService;
import com.atguigu.eduservice.service.EduTeacherService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 讲师 服务实现类
 * </p>
 *
 * @author testjava
 * @since 2022-05-24
 */
@SuppressWarnings({"unchecked", "SpringJavaAutowiredFieldsWarningInspection", "AlibabaCollectionInitShouldAssignCapacity", "DuplicatedCode"})
@Service
public class EduTeacherServiceImpl extends ServiceImpl<EduTeacherMapper, EduTeacher> implements EduTeacherService {

    @Autowired
    private EduCourseService courseService;

    /**
     * 分页查询讲师
     * @param teacherPage 分页参数
     * @return 分页结果整合
     */
    @Override
    public Map<String, Object> getTeacherFrontList(Page<EduTeacher> teacherPage) {

        LambdaQueryWrapper<EduTeacher> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByDesc(EduTeacher::getId);

        this.page(teacherPage, wrapper);

        List<EduTeacher> records = teacherPage.getRecords();
        long current = teacherPage.getCurrent();
        long pages = teacherPage.getPages();
        long size = teacherPage.getSize();
        long total = teacherPage.getTotal();
        boolean hasNext = teacherPage.hasNext();
        boolean hasPrevious = teacherPage.hasPrevious();

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
     * 根据讲师id查询讲师基本信息和所讲课程信息
     * @param teacherId 讲师id
     * @param request 请求对象
     * @return 讲师基本信息和所讲课程信息
     */
    @Override
    public Map<Object, Object> getTeacherFrontInfo(String teacherId,HttpServletRequest request) {

        //查询是否登录
        boolean isLogin = true;
        String memberId = JwtUtils.getMemberIdByJwtToken(request);

        //1、根据讲师id查询讲师基本信息
        EduTeacher teacher = this.getById(teacherId);

        //2、根据讲师id查询讲师课程信息
        LambdaQueryWrapper<EduCourse> wrapper = new LambdaQueryWrapper<>();
        String publishStatus = "Normal";
        wrapper.eq(EduCourse::getStatus, publishStatus);
        wrapper.eq(EduCourse::getTeacherId, teacherId);
        //未登录显示课程
        if (StringUtils.isEmpty(memberId)) {
            isLogin = false;
            wrapper.eq(EduCourse::getPrice, 0);
        }
        List<EduCourse> courseList = courseService.list(wrapper);

        Map<Object, Object> map = new HashMap<>();
        map.put("teacher", teacher);
        map.put("courseList", courseList);
        map.put("isLogin", isLogin);

        return map;
    }
}
