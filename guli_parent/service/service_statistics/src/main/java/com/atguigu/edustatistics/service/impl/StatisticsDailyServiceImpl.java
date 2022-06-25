package com.atguigu.edustatistics.service.impl;

import com.atguigu.edustatistics.client.UcenterClient;
import com.atguigu.edustatistics.entity.StatisticsDaily;
import com.atguigu.edustatistics.mapper.StatisticsDailyMapper;
import com.atguigu.edustatistics.service.StatisticsDailyService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 网站统计日数据 服务实现类
 * </p>
 *
 * @author luokai
 * @since 2022-06-25
 */
@SuppressWarnings("AlibabaCollectionInitShouldAssignCapacity")
@Service
public class StatisticsDailyServiceImpl extends ServiceImpl<StatisticsDailyMapper, StatisticsDaily> implements StatisticsDailyService {


    @Autowired
    private UcenterClient ucenterClient;

    /**
     * 统计某一天的注册人数
     * @param day 某一天
     */
    @Override
    public void createStatisticsByDay(String day) {

        //删除相同日期的对象，保证同一天不会出现多条数据
        LambdaQueryWrapper<StatisticsDaily> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(StatisticsDaily::getDateCalculated, day);
        this.remove(queryWrapper);

        //获取统计信息
        Integer registerNum = (Integer) ucenterClient.registerCount(day).getData().get("countRegister");

        //TODO
        Integer loginNum = RandomUtils.nextInt(100, 200);
        Integer videoViewNum = RandomUtils.nextInt(100, 200);
        Integer courseNum = RandomUtils.nextInt(100, 200);

        //创建统计对象
        StatisticsDaily daily = new StatisticsDaily();
        daily.setRegisterNum(registerNum);
        daily.setLoginNum(loginNum);
        daily.setVideoViewNum(videoViewNum);
        daily.setCourseNum(courseNum);
        daily.setDateCalculated(day);

        this.save(daily);
    }

    /**
     * 图表显示，返回两部分数据，日期json数组，数量json数组
     * @param type 选择查看的类型
     * @param begin 开始日期
     * @param end 结束日期
     * @return 日期json数组+数量json数组
     */
    @Override
    public Map<String, Object> getShowData(String type, String begin, String end) {

        QueryWrapper<StatisticsDaily> wrapper = new QueryWrapper<>();
        wrapper.between("date_calculated", begin, end);
        wrapper.select("date_calculated",type);

        List<StatisticsDaily> staList = this.list(wrapper);
        //返回两部分数据，日期json数组，数量json数组
        //后端list <==> 前端数组
        //创建两个list，日期list，数量list
        List<String> dateCalculatedList = new ArrayList<>();
        List<Integer> numDataList = new ArrayList<>();
        for (StatisticsDaily sta : staList) {
            //封装日期list
            dateCalculatedList.add(sta.getDateCalculated());
            switch (type) {
                case "login_num":
                    numDataList.add(sta.getLoginNum());
                    break;
                case "register_num":
                    numDataList.add(sta.getRegisterNum());
                    break;
                case "video_view_num":
                    numDataList.add(sta.getVideoViewNum());
                    break;
                case "course_num":
                    numDataList.add(sta.getCourseNum());
                    break;
                default:
                    break;
            }
        }

        HashMap<String, Object> map = new HashMap<>();
        map.put("date_calculated_list",dateCalculatedList);
        map.put("num_data_list",numDataList);

        return map;
    }
}
