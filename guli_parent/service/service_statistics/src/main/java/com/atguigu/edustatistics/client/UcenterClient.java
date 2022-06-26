package com.atguigu.edustatistics.client;

import com.atguigu.commonutils.R;
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
     * 统计某一天的注册人数
     * @param day 某一天
     * @return 注册总人数
     */
    @GetMapping(value = "/educenter/member/countregister/{day}")
    public R registerCount(@PathVariable("day") String day);
}
