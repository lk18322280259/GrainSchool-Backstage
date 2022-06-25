package com.atguigu.edustatistics.controller;


import com.atguigu.commonutils.R;
import com.atguigu.edustatistics.service.StatisticsDailyService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * <p>
 * 网站统计日数据 前端控制器
 * </p>
 *
 * @author luokai
 * @since 2022-06-25
 */
@Api("统计分析管理")
@CrossOrigin
@RestController
@RequestMapping("/edustatistics/daily")
public class StatisticsDailyController {

    @Autowired
    private StatisticsDailyService statisticsService;

    /**
     * 统计某一天的注册人数
     * @param day 某一天
     * @return 注册总人数
     */
    @ApiOperation("统计指定日期注册人数接口")
    @GetMapping("registerCount/{day}")
    public R createStatisticsByDate(
            @ApiParam(name = "day", value = "具体日期(例:2022-06-21)", required = true)
            @PathVariable String day) {

        statisticsService.createStatisticsByDay(day);
        return R.ok();
    }

    /**
     * 图表显示，返回两部分数据，日期json数组，数量json数组
     * @param type 选择查看的类型
     * @param begin 开始日期
     * @param end 结束日期
     * @return 日期json数组+数量json数组
     */
    @ApiOperation("统计指定时间段的指定字段的人数接口")
    @GetMapping("showData/{type}/{begin}/{end}")
    public R showData(
            @ApiParam(name = "type", value = "选择查看的类型", required = true)
            @PathVariable String type,
            @ApiParam(name = "begin", value = "开始日期(例:2019-01-01)", required = true)
            @PathVariable String begin,
            @ApiParam(name = "end", value = "结束日期(例:2022-06-25)", required = true)
            @PathVariable String end) {

        Map<String,Object> map = statisticsService.getShowData(type,begin,end);

        return R.ok().data(map);
    }
}

