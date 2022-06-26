package com.atguigu.eduservice.controller.front;

import com.atguigu.commonutils.R;
import com.atguigu.eduservice.entity.EduCourse;
import com.atguigu.eduservice.entity.EduTeacher;
import com.atguigu.eduservice.service.EduTeacherService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * @Author luokai
 */
@RestController
@RequestMapping("/eduservice/teacherfront")
@SuppressWarnings({"AlibabaCommentsMustBeJavadocFormat", "unchecked"})
//@CrossOrigin
@Api("前台查看讲师信息")
public class TeacherFrontController {

    @Autowired
    private EduTeacherService teacherService;


    /**
     * 分页查询讲师
     * @return 分页结果整合
     */
    @ApiOperation("分页查询讲师接口")
    @GetMapping("getTeacherFrontList/{page}/{limit}")
    public R getTeacherFrontList(
            @ApiParam(name = "page", value = "页数", required = true)
            @PathVariable long page,
            @ApiParam(name = "limit", value = "每页记录数", required = true)
            @PathVariable long limit) {

        Page<EduTeacher> teacherPage = new Page<>(page, limit);
        Map<String,Object> map = teacherService.getTeacherFrontList(teacherPage);

        return R.ok().data(map);
    }

    /**
     * 根据讲师id查询讲师基本信息和所讲课程信息
     * @param teacherId 讲师id
     * @return 讲师基本信息和所讲课程信息
     */
    @ApiOperation("讲师详情接口")
    @GetMapping("getTeacherFrontInfo/{teacherId}")
    public R getTeacherFrontInfo(
            @ApiParam(name = "teacherId", value = "讲师id", required = true)
            @PathVariable String teacherId,
            @ApiParam(name = "request", value = "请求对象", required = true)
            HttpServletRequest request) {

        Map<Object, Object> teacherInfo = teacherService.getTeacherFrontInfo(teacherId, request);
        EduTeacher teacher = (EduTeacher) teacherInfo.get("teacher");
        List<EduCourse> courseList = (List<EduCourse>) teacherInfo.get("courseList");

        boolean isLogin = (boolean) teacherInfo.get("isLogin");

        return R.ok().data("teacher",teacher).data("courseList",courseList).data("isLogin",isLogin);
    }
}
