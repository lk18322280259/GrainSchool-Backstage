package com.atguigu.orderservice.service;

import com.atguigu.orderservice.entity.Order;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 订单 服务类
 * </p>
 *
 * @author luokai
 * @since 2022-06-24
 */
public interface OrderService extends IService<Order> {

    /**
     * 通过课程id和用户id生成订单号
     * @param courseId 课程id
     * @param request 通过request得到用户id
     * @return 订单号
     */
    String createOrder(String courseId, HttpServletRequest request);
}
