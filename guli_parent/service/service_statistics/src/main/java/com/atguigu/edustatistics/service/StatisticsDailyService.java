package com.atguigu.edustatistics.service;

import com.atguigu.edustatistics.entity.StatisticsDaily;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * <p>
 * 网站统计日数据 服务类
 * </p>
 *
 * @author luokai
 * @since 2022-06-25
 */
public interface StatisticsDailyService extends IService<StatisticsDaily> {

    /**
     * 统计某一天的注册人数
     * @param day 某一天
     */
    void createStatisticsByDay(String day);

    /**
     * 图表显示，返回两部分数据，日期json数组，数量json数组
     * @param type 选择查看的类型
     * @param begin 开始日期
     * @param end 结束日期
     * @return 日期json数组+数量json数组
     */
    Map<String, Object> getShowData(String type, String begin, String end);
}
