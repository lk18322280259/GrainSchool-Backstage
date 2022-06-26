package com.atguigu.eduservice.client;

import com.atguigu.servicebase.exceptionhandler.GuliException;
import org.springframework.stereotype.Component;

/**
 * @Author luokai
 */
@Component
public class OrderClientImpl implements OrderClient{
    @Override
    public boolean isBuyCourse(String courseId, String memberId) {
        throw new GuliException(20001,"远程调用订单服务异常");
    }
}
