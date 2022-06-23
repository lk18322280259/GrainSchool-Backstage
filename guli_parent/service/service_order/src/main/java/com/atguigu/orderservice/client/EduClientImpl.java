package com.atguigu.orderservice.client;

import com.atguigu.commonutils.ordervo.CourseWebVoCommon;
import com.atguigu.servicebase.exceptionhandler.GuliException;

/**
 * @Author luokai
 */
public class EduClientImpl implements EduClient{

    @Override
    public CourseWebVoCommon getCourseInfoOrder(String courseId) {
        throw new GuliException(20001,"远程调用查询课程信息异常");
    }
}
