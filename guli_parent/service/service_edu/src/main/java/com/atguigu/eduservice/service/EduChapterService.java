package com.atguigu.eduservice.service;

import com.atguigu.eduservice.entity.EduChapter;
import com.atguigu.eduservice.entity.chapter.ChapterVo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 课程 服务类
 * </p>
 *
 * @author luokai
 * @since 2022-06-12
 */
public interface EduChapterService extends IService<EduChapter> {

    //课程大纲列表，根据课程id进行查询
    List<ChapterVo> getChapterVideoByCourseId(String courseId);

    //添加章节
    void addChapter(EduChapter eduChapter);

    //根据id查询章节
    EduChapter getChapterById(String chapterId);

    //修改章节
    void updateChapter(EduChapter eduChapter);

    //删除章节
    void deleteChapter(String chapterId);

    //根据课程id删除章节
    void removeChapterByCourseId(String courseId);
}
