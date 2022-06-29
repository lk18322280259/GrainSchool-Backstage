package com.atguigu.eduservice.service.impl;

import com.atguigu.commonutils.R;
import com.atguigu.eduservice.client.VodClient;
import com.atguigu.eduservice.entity.EduVideo;
import com.atguigu.eduservice.mapper.EduVideoMapper;
import com.atguigu.eduservice.service.EduVideoService;
import com.atguigu.servicebase.exceptionhandler.GuliException;
import com.atguigu.eduservice.utils.ManageTencentVod;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 课程视频 服务实现类
 * </p>
 *
 * @author luokai
 * @since 2022-06-12
 */
@Service
public class EduVideoServiceImpl extends ServiceImpl<EduVideoMapper, EduVideo> implements EduVideoService {


    //注入微服务的interface
    @Autowired
    private VodClient vodClient;

    //是否调用微服务删除视频
    @Value("${custom-parameters.enable-cloud-delete-vod}")
    private String enableCloudDeleteVod;


    //添加小节
    @Override
    public void addVideo(EduVideo eduVideo) {

        boolean save = this.save(eduVideo);
        if (!save) {
            throw new GuliException(20001, "添加小节失败");
        }
    }

    //TODO 删除小节
    @Override
    public void deleteById(String videoId) {

        LambdaQueryWrapper<EduVideo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(null!=videoId, EduVideo::getId, videoId);
        EduVideo eduVideo = this.getOne(queryWrapper);

        //先删除视频
        //fileId不为空，说明有视频存在
        if (!StringUtils.isEmpty(eduVideo.getVideoSourceId())) {

            //TODO 删除视频
            //获取视频ID字段
            String fileId = eduVideo.getVideoSourceId();

            //微服务删除视频
            if ("true".equals(enableCloudDeleteVod)){
                //微服务删除视频
                System.out.println("调用微服务删除腾讯云视频");
                R result = vodClient.removeTencentVideoByFileId(fileId);
                if (result.getCode()==20001) {
                    throw new GuliException(20001,"删除视频失败，启用熔断器");
                }
            } else {
                //调用本地函数删除视频
                System.out.println("调用调用本地函数删除腾讯云视频");
                ManageTencentVod.deleteVod(fileId);
            }
        }
        //再删除小节
        boolean remove = this.removeById(videoId);
        if (!remove) {
            throw new GuliException(20001, "删除小节失败");
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

    //TODO 根据课程id删除小节及小节对应的视频
    @Override
    public void removeVideoByCourseId(String courseId) {

        LambdaQueryWrapper<EduVideo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(EduVideo::getCourseId, courseId);
        List<EduVideo> eduVideoList = this.list(queryWrapper);

        //先删除视频
        List<String> videoList = new ArrayList<>();
        for (EduVideo eduVideo : eduVideoList) {

            //把不为空的videoId传进去
            if (!StringUtils.isEmpty(eduVideo.getVideoSourceId())) {
                videoList.add(eduVideo.getVideoSourceId());
            }

        }

        //videoList有值再删除视频
        if (videoList.size()>0) {

            //判断微服务是否开启
            if ("true".equals(enableCloudDeleteVod)){
                //微服务删除视频
                vodClient.removeTencentVideoByFileIds(videoList);

            } else {
                //本地函数删除视频
                boolean deleteVod = true;
                for (String video : videoList) {
                    deleteVod = ManageTencentVod.deleteVod(video);
                }

                if (!deleteVod) {
                    throw new GuliException(20001, "腾讯云视频删除异常");
                }
            }
        }

        //删除小节
        boolean remove = this.remove(queryWrapper);

        if (!remove) {
            throw new GuliException(20001, "删除小节失败");
        }
    }
}
