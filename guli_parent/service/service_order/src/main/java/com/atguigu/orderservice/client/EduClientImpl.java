package com.atguigu.orderservice.client;

import com.atguigu.commonutils.ordervo.CourseWebVoCommon;
import com.atguigu.servicebase.exceptionhandler.GuliException;
import org.springframework.stereotype.Component;

/**
 * @Author luokai
 */
@Component
public class EduClientImpl implements EduClient{

    @Override
    public CourseWebVoCommon getCourseInfoOrder(String courseId) {
        throw new GuliException(20001,"远程调用查询课程信息异常");
    }
}
