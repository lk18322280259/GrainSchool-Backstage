package com.atguigu.orderservice.service.impl;

import com.atguigu.commonutils.JwtUtils;
import com.atguigu.commonutils.ordervo.CourseWebVoCommon;
import com.atguigu.commonutils.ordervo.UcenterMemberCommon;
import com.atguigu.orderservice.client.EduClient;
import com.atguigu.orderservice.client.UcenterClient;
import com.atguigu.orderservice.entity.Order;
import com.atguigu.orderservice.mapper.OrderMapper;
import com.atguigu.orderservice.service.OrderService;
import com.atguigu.orderservice.utils.OrderNoUtil;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 订单 服务实现类
 * </p>
 *
 * @author luokai
 * @since 2022-06-24
 */
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {

    /**
     * 注入远程调用的接口
     */
    @Autowired
    private UcenterClient ucenterClient;

    @Autowired
    private EduClient eduClient;


    /**
     * 通过课程id和用户id生成订单号
     * @param courseId 课程id
     * @param request 通过request得到用户id
     * @return 订单号
     */
    @Override
    public String createOrder(String courseId, HttpServletRequest request) {

        //1、通过request得到用户id
        String memberId = JwtUtils.getMemberIdByJwtToken(request);

        //2、通过远程调用根据用户id获取用户信息
        UcenterMemberCommon userInfo = ucenterClient.getUserInfo(memberId);
        //3、通过远程调用根据课程id获取课程信息
        CourseWebVoCommon courseInfo = eduClient.getCourseInfoOrder(courseId);

        Order order = new Order();
        order.setOrderNo(OrderNoUtil.getOrderNo());
        order.setCourseId(courseId);
        order.setCourseTitle(courseInfo.getTitle());
        order.setCourseCover(courseInfo.getCover());
        order.setTeacherName(courseInfo.getTeacherName());
        order.setTotalFee(courseInfo.getPrice());
        order.setMemberId(memberId);
        order.setMobile(userInfo.getMobile());
        order.setNickname(userInfo.getNickname());
        // 支付状态 0-未支付 1-已支付
        order.setStatus(0);
        // 支付类型 1-微信
        order.setPayType(1);

        this.save(order);

        //返回订单号
        return order.getOrderNo();
    }
}
