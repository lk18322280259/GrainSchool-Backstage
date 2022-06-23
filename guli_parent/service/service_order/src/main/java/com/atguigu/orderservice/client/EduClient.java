package com.atguigu.orderservice.client;

import com.atguigu.commonutils.ordervo.CourseWebVoCommon;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @Author luokai
 */
@Component
@FeignClient("service-edu")
public interface EduClient {

    /**
     * 根据课程id得到课程相关信息
     * @param courseId 课程id
     * @return 课程信息
     */
    @GetMapping("/eduservice/coursefront/getCourseInfoOrder/{courseId}")
    public CourseWebVoCommon getCourseInfoOrder(@PathVariable("courseId") String courseId);
}
