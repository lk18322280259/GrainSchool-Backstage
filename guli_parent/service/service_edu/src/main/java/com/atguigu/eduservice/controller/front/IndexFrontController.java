package com.atguigu.eduservice.controller.front;

import com.atguigu.commonutils.R;
import com.atguigu.eduservice.entity.EduCourse;
import com.atguigu.eduservice.entity.EduTeacher;
import com.atguigu.eduservice.service.EduCourseService;
import com.atguigu.eduservice.service.EduTeacherService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/eduservice/indexfront")
@CrossOrigin
@Api("前台查看")
public class IndexFrontController {

    @Autowired
    private EduCourseService courseService;

    @Autowired
    private EduTeacherService teacherService;

    // 查询前8个门课程，查询前4名讲师
    @SuppressWarnings("unchecked")
    @GetMapping("index")
    public R index() {

        // 查询前8个门课程
        LambdaQueryWrapper<EduCourse> courseWrapper = new LambdaQueryWrapper<>();
        courseWrapper.orderByDesc(EduCourse::getViewCount);
        courseWrapper.last("limit 8");
        List<EduCourse> courseList = courseService.list(courseWrapper);

        // 查询前4名讲师
        LambdaQueryWrapper<EduTeacher> teacherWrapper = new LambdaQueryWrapper<>();
        teacherWrapper.orderByDesc(EduTeacher::getId);
        teacherWrapper.last("limit 4");
        List<EduTeacher> teacherList = teacherService.list(teacherWrapper);

        return R.ok().data("courseList",courseList).data("teacherList", teacherList);
    }
}
