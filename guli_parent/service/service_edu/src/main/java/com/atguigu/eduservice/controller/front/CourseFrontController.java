package com.atguigu.eduservice.controller.front;

import com.atguigu.commonutils.R;
import com.atguigu.eduservice.entity.EduCourse;
import com.atguigu.eduservice.entity.EduTeacher;
import com.atguigu.eduservice.entity.frontvo.CourseFrontVo;
import com.atguigu.eduservice.service.EduCourseService;
import com.atguigu.eduservice.service.EduTeacherService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @Author luokai
 */
@SuppressWarnings({"SpringJavaAutowiredFieldsWarningInspection"})
@RestController
@RequestMapping("/eduservice/coursefront")
@CrossOrigin
@Api("前台查看课程信息")
public class CourseFrontController {

    @Autowired
    private EduCourseService courseService;


    /**
     * 分页带条件查询课程
     * @return 分页结果整合
     */
    @ApiOperation("分页查询课程接口")
    @PostMapping("getCourseFrontList/{page}/{limit}")
    public R getTeacherFrontList(
            @ApiParam(name = "page", value = "页数", required = true)
            @PathVariable long page,
            @ApiParam(name = "limit", value = "每页记录数", required = true)
            @PathVariable long limit,
            @ApiParam(name = "courseFrontVo", value = "条件查询对象", required = false)
            @RequestBody(required = false) CourseFrontVo courseFrontVo) {

        Page<EduCourse> coursePage = new Page<>(page, limit);
        Map<String,Object> map = courseService.getCourseFrontList(coursePage, courseFrontVo);

        return R.ok().data(map);
    }
}
