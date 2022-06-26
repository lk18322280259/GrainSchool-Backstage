package com.atguigu.eduservice.controller.front;

import com.atguigu.commonutils.JwtUtils;
import com.atguigu.commonutils.R;
import com.atguigu.commonutils.ordervo.CourseWebVoCommon;
import com.atguigu.eduservice.client.OrderClient;
import com.atguigu.eduservice.entity.EduCourse;
import com.atguigu.eduservice.entity.chapter.ChapterVo;
import com.atguigu.eduservice.entity.frontvo.CourseFrontVo;
import com.atguigu.eduservice.entity.frontvo.CourseWebVo;
import com.atguigu.eduservice.service.EduChapterService;
import com.atguigu.eduservice.service.EduCourseService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * @Author luokai
 */
@RestController
@RequestMapping("/eduservice/coursefront")
@SuppressWarnings("AlibabaCommentsMustBeJavadocFormat")
//@CrossOrigin
@Api("前台查看课程信息")
public class CourseFrontController {

    @Autowired
    private EduCourseService courseService;

    @Autowired
    private EduChapterService chapterService;

    @Autowired
    private OrderClient orderClient;


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
            @ApiParam(name = "courseFrontVo", value = "条件查询对象")
            @RequestBody(required = false) CourseFrontVo courseFrontVo,
            @ApiParam(name = "request", value = "请求对象", required = true)
            HttpServletRequest request) {

        Page<EduCourse> coursePage = new Page<>(page, limit);
        Map<String,Object> map = courseService.getCourseFrontList(coursePage, courseFrontVo, request);

        return R.ok().data(map);
    }

    /**
     * 根据ID查询课程详情
     * @param courseId 课程id
     * @return 课程信息
     */
    @PostMapping(value = "getCourseDetailInfoById/{courseId}")
    @ApiOperation(value = "根据课程id查询课程详细信息(章节|视频|讲师等等)")
    public R getCourseDetailInfoById(
            @ApiParam(name = "courseId", value = "课程ID", required = true)
            @PathVariable String courseId,
            @ApiParam(name = "request", value = "请求对象", required = true)
            HttpServletRequest request){

        //查询课程信息和讲师信息
        CourseWebVo courseWebVo = courseService.selectInfoWebById(courseId);

        //查询当前课程的章节信息
        List<ChapterVo> chapterVoList = chapterService.getChapterVideoByCourseId(courseId);

        //根据课程id和用户id查询当前课程是否已经支付过
        String memberId = JwtUtils.getMemberIdByJwtToken(request);
        boolean buyCourse = orderClient.isBuyCourse(courseId, memberId);

        return R.ok().data("course", courseWebVo).data("chapterVoList", chapterVoList).data("isBuy",buyCourse);
    }

    /**
     * 根据课程id得到课程相关信息
     * @param courseId 课程id
     * @return 课程信息
     */
    @GetMapping("getCourseInfoOrder/{courseId}")
    public CourseWebVoCommon getCourseInfoOrder(@PathVariable String courseId) {

        CourseWebVo courseWebVo = courseService.selectInfoWebById(courseId);
        CourseWebVoCommon courseWebVoCommon = new CourseWebVoCommon();
        BeanUtils.copyProperties(courseWebVo, courseWebVoCommon);

        return courseWebVoCommon;
    }
}
