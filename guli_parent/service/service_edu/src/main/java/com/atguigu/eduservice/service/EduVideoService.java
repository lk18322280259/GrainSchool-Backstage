package com.atguigu.eduservice.service;

import com.atguigu.eduservice.entity.EduVideo;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 课程视频 服务类
 * </p>
 *
 * @author luokai
 * @since 2022-06-12
 */
public interface EduVideoService extends IService<EduVideo> {

    //添加小节
    void addVideo(EduVideo eduVideo);

    //删除小节
    void deleteById(String videoId);

    //查询回显小节
    EduVideo getVideoById(String videoId);

    //修改小节
    void updateVideo(EduVideo eduVideo);

    //根据课程id删除小节
    void removeVideoByCourseId(String courseId);
}
