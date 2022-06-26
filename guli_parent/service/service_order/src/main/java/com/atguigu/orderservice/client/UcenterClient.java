package com.atguigu.orderservice.client;

import com.atguigu.commonutils.ordervo.CourseWebVoCommon;
import com.atguigu.commonutils.ordervo.UcenterMemberCommon;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @Author luokai
 */
@Component
@FeignClient(name="service-ucenter",fallback = UcenterClientImpl.class)
public interface UcenterClient {

    /**
     * 根据用户id获取用户信息
     *
     * @param id 用户id
     * @return 用户信息
     */
    @GetMapping("/educenter/member/getUserInfo/{id}")
    public UcenterMemberCommon getUserInfo(@PathVariable("id") String id);

}
