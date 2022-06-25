package com.atguigu.edustatistics.utils;

import com.atguigu.edustatistics.service.StatisticsDailyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.util.Date;

/**
 * @Author luokai
 */
@Component
public class ScheduledTask {

    @Autowired
    private StatisticsDailyService dailyService;

    /**
     * 每天晚上0点0分执行定时 ，统计前一天的数据
     * （SpringBoot只允许只写前6位）
     */
    @Scheduled(cron = "0 0 0 * * ?")
    public void task01() {
        System.out.println("00:00执行了定时统计任务");
        //获取上一天的日期
        String day = DateUtil.formatDate(DateUtil.addDays(new Date(), -1));
        dailyService.createStatisticsByDay(day);

    }
}
