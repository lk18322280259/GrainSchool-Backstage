package com.atguigu.eduservice.service.impl;

import com.atguigu.eduservice.entity.EduVideo;
import com.atguigu.eduservice.mapper.EduVideoMapper;
import com.atguigu.eduservice.service.EduVideoService;
import com.atguigu.servicebase.exceptionhandler.GuliException;
import com.atguigu.eduservice.utils.ManageTencentVod;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 * 课程视频 服务实现类
 * </p>
 *
 * @author testjava
 * @since 2022-06-12
 */
@Service
public class EduVideoServiceImpl extends ServiceImpl<EduVideoMapper, EduVideo> implements EduVideoService {

    //添加小节
    @Override
    public void addVideo(EduVideo eduVideo) {

        boolean save = this.save(eduVideo);
        if (!save) {
            throw new GuliException(20001, "添加小节失败");
        }
    }

    //删除小节
    @Override
    public void deleteById(String videoId) {

        LambdaQueryWrapper<EduVideo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(null!=videoId, EduVideo::getId, videoId);
        EduVideo eduVideo = this.getOne(queryWrapper);
        String fileId = eduVideo.getVideoSourceId();
        //videoId不为""，说明有视频存在
        boolean remove = this.removeById(videoId);
        if (!remove) {
            throw new GuliException(20001, "删除小节失败");
        } else {
            //TODO删除视频
            boolean deleteVod = ManageTencentVod.deleteVod(fileId);
            if (!deleteVod) {
                throw new GuliException(20001, "删除视频异常");
            }
        }
    }

    //查询回显小节
    @Override
    public EduVideo getVideoById(String videoId) {

        EduVideo eduVideo = this.getById(videoId);
        if (eduVideo==null) {
            throw new GuliException(20001, "查询小节失败");
        }
        return eduVideo;
    }

    //修改小节
    @Override
    public void updateVideo(EduVideo eduVideo) {

        boolean update = this.updateById(eduVideo);
        if (!update) {
            throw new GuliException(20001, "修改小节失败");
        }
    }

    //根据课程id删除小节
    //TODO 删除小节对应的视频
    @Override
    @Transactional
    public void removeVideoByCourseId(String courseId) {

        LambdaQueryWrapper<EduVideo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(EduVideo::getCourseId, courseId);
        List<EduVideo> eduVideoList = this.list(queryWrapper);

        boolean remove = this.remove(queryWrapper);

        if (!remove) {
            throw new GuliException(20001, "删除小节失败");
        } else {

            //TODO 删除小节对应的视频
            boolean deleteVod = true;
            for (EduVideo eduVideo : eduVideoList) {
                String fileId = eduVideo.getVideoSourceId();
                deleteVod = ManageTencentVod.deleteVod(fileId);
            }

            if (!deleteVod) {
                throw new GuliException(20001, "腾讯云视频删除异常");
            }
        }


    }
}
