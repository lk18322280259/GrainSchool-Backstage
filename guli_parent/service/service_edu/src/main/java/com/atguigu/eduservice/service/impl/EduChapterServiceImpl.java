package com.atguigu.eduservice.service.impl;

import com.atguigu.eduservice.entity.EduChapter;
import com.atguigu.eduservice.entity.EduVideo;
import com.atguigu.eduservice.entity.chapter.ChapterVo;
import com.atguigu.eduservice.entity.chapter.VideoVo;
import com.atguigu.eduservice.mapper.EduChapterMapper;
import com.atguigu.eduservice.service.EduChapterService;
import com.atguigu.eduservice.service.EduVideoService;
import com.atguigu.servicebase.exceptionhandler.GuliException;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 课程 服务实现类
 * </p>
 *
 * @author testjava
 * @since 2022-06-12
 */
@Service
public class EduChapterServiceImpl extends ServiceImpl<EduChapterMapper, EduChapter> implements EduChapterService {

    @Autowired
    private EduVideoService videoService;

    //课程大纲列表，根据课程id进行查询
    @Override
    public List<ChapterVo> getChapterVideoByCourseId(String courseId) {

        //1 根据课程id查询所有章节
        QueryWrapper<EduChapter> wrapperChapter = new QueryWrapper<>();
        wrapperChapter.eq("course_id", courseId);
        List<EduChapter> eduChapterList = this.list(wrapperChapter);

        //2 根据课程id查询所有小节
        LambdaQueryWrapper<EduVideo> wrapperVideo = new LambdaQueryWrapper<>();
        wrapperVideo.eq(EduVideo::getCourseId,courseId);
        List<EduVideo> eduVideoList = videoService.list(wrapperVideo);

        //创建list集合，用于最终封装数据
        List<ChapterVo> finalList = new ArrayList<>();

        //3 遍历查询章节list集合进行封装
        //遍历章节
        for (EduChapter eduChapter : eduChapterList) {
            ChapterVo chapterVo = new ChapterVo();
            BeanUtils.copyProperties(eduChapter, chapterVo);

            //创建集合封装章节中小节
            ArrayList<VideoVo> videoList = new ArrayList<>();

            //4 遍历查询小节list集合，进行封装
            for (EduVideo eduVideo : eduVideoList) {
                String chapterId = eduVideo.getChapterId();
                if (chapterId.equals(eduChapter.getId())) {
                    VideoVo videoVo = new VideoVo();
                    BeanUtils.copyProperties(eduVideo, videoVo);
                    videoList.add(videoVo);
                }
            }
            //将封装好的小节注入到章节vo中
            chapterVo.setChildren(videoList);

            //将章节放到最终返回的数据里
            finalList.add(chapterVo);

        }
        return finalList;
    }

    //添加章节
    @Override
    public void addChapter(EduChapter eduChapter) {

        boolean save = this.save(eduChapter);
        if (!save) {
            throw new GuliException(20001, "添加章节失败");
        }
    }

    //根据id查询章节
    @Override
    public EduChapter getChapterById(String chapterId) {

        EduChapter eduChapter = this.getById(chapterId);
        if (eduChapter==null) {
            throw new GuliException(20001, "查询章节失败");
        }
        return eduChapter;
    }

    //修改章节
    @Override
    public void updateChapter(EduChapter eduChapter) {

        boolean update = this.updateById(eduChapter);
        if (!update) {
            throw new GuliException(20001, "修改章节失败");
        }
    }

    //删除章节
    @Override
    @Transactional
    public void deleteChapter(String chapterId) {

        //删除章节如果章节内存在小节，将小节也一并删除
        LambdaQueryWrapper<EduVideo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(null!=chapterId, EduVideo::getChapterId, chapterId);
        List<EduVideo> videoList = videoService.list(queryWrapper);

        //章节下存在小节
        if (videoList != null) {
            /*
            for (EduVideo eduVideo : videoList) {
                //删除小节
                removeChapter = videoService.removeById(eduVideo);
            }
            */
            //存在小节，不允许删除
            throw new GuliException(20001, "该章节下存在小节，无法删除");
        }

        //删除章节
        boolean removeChapter = this.removeById(chapterId);
        if (!removeChapter) {
            throw new GuliException(20001, "删除章节(小节)失败");
        }
    }

    //根据课程id删除章节
    @Override
    public void removeChapterByCourseId(String courseId) {

        LambdaQueryWrapper<EduChapter> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(EduChapter::getCourseId, courseId);

        boolean remove = this.remove(queryWrapper);

        if (!remove) {
            throw new GuliException(20001, "删除章节失败");
        }
    }
}
