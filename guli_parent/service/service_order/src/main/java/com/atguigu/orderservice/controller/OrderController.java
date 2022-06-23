package com.atguigu.orderservice.controller;


import com.atguigu.commonutils.JwtUtils;
import com.atguigu.commonutils.R;
import com.atguigu.orderservice.entity.Order;
import com.atguigu.orderservice.service.OrderService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 订单 前端控制器
 * </p>
 *
 * @author luokai
 * @since 2022-06-24
 */
@CrossOrigin
@RestController
@RequestMapping("/orderservice/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    /**
     * 通过课程id生成订单号
     * @param courseId 课程id
     * @return 订单号
     */
    @GetMapping("createOrder/{courseId}")
    public R createOrder(@PathVariable String courseId, HttpServletRequest request) {

        String orderNo = orderService.createOrder(courseId, request);
        return R.ok().data("orderNo", orderNo);
    }

    /**
     * 通过订单号来查询订单（非订单id）
     * @param orderNo 订单号
     * @return 订单详情
     */
    @GetMapping("getOrderInfo/{orderNo}")
    public R getOrderInfo(@PathVariable String orderNo) {

        //orderNo是订单id 字段为order_no
        LambdaQueryWrapper<Order> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Order::getOrderNo, orderNo);
        Order order = orderService.getOne(queryWrapper);

        return R.ok().data("order",order);
    }

}

