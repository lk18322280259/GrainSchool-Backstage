package com.atguigu.eduservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @Author luokai
 */
@Component
@FeignClient(name="service-order",fallback = OrderClientImpl.class)
public interface OrderClient {

    /**
     * 根据课程id和用户id查询订单表中订单状态
     * @param courseId 课程id
     * @param memberId 用户id
     * @return 是否购买
     */
    @GetMapping("/orderservice/order/isBuyCourse/{courseId}/{memberId}")
    public boolean isBuyCourse(@PathVariable("courseId") String courseId,
                               @PathVariable("memberId") String memberId);
}
