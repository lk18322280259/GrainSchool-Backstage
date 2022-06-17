package com.atguigu.eduservice.service.impl;

import com.alibaba.excel.EasyExcel;
import com.atguigu.eduservice.entity.EduSubject;
import com.atguigu.eduservice.entity.excel.ExcelSubjectData;
import com.atguigu.eduservice.entity.subject.OneSubject;
import com.atguigu.eduservice.entity.subject.TwoSubject;
import com.atguigu.eduservice.listener.ExcelSubjectListener;
import com.atguigu.eduservice.mapper.EduSubjectMapper;
import com.atguigu.eduservice.service.EduSubjectService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 课程科目 服务实现类
 * </p>
 *
 * @author testjava
 * @since 2022-06-02
 */
@Service
public class EduSubjectServiceImpl extends ServiceImpl<EduSubjectMapper, EduSubject> implements EduSubjectService {

    //添加课程分类
    @Override
    public void saveSubject(MultipartFile file, EduSubjectService eduSubjectService) {

        try {
            //文件输入流
            InputStream inputStream = file.getInputStream();
            EasyExcel.read(inputStream, ExcelSubjectData.class, new ExcelSubjectListener(eduSubjectService)).sheet().doRead();

        } catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public List<OneSubject> getAllOneTwoSubject() {

        //1 查询所有的一级分类 parent_id==0
        QueryWrapper<EduSubject> wrapperOne = new QueryWrapper<>();
        wrapperOne.eq("parent_id", "0");
        List<EduSubject> oneSubjectList = this.list(wrapperOne);

        //2 查询所有的二级分类 parent_id!=0
        QueryWrapper<EduSubject> wrapperTwo = new QueryWrapper<>();
        wrapperTwo.ne("parent_id", "0");
        List<EduSubject> twoSubjectList = this.list(wrapperTwo);

        //创建list集合存储最终封装的数据
        List<OneSubject> finalSubjectList = new ArrayList<>();

        //3 封装一级分类
        //拷贝List<EduSubject> -> List<OneSubject>
        for (EduSubject eduSubject : oneSubjectList) {
            OneSubject oneSubject = new OneSubject();
//            oneSubject.setId(eduSubject.getId());
//            oneSubject.setTitle(eduSubject.getTitle());
            BeanUtils.copyProperties(eduSubject, oneSubject);
            finalSubjectList.add(oneSubject);

            //4 封装二级分类
            //在一级分类中遍历查询所有的二级分类
            List<TwoSubject> towFinalSubjectList = new ArrayList<>();
            for (EduSubject tSubject : twoSubjectList) {
                //获取每个二级分类
                //判断二级分类parentid和一级分类id是否一样
                if (tSubject.getParentId().equals(eduSubject.getId())) {
                    //把twoSubject值复制到TwoSubject里面，放到towFinalSubjectList里
                    TwoSubject twoSubject = new TwoSubject();
                    BeanUtils.copyProperties(tSubject, twoSubject);
                    towFinalSubjectList.add(twoSubject);
                }
            }

            //把一级下面所有分类放到一级分类里面
            oneSubject.setChildren(towFinalSubjectList);
        }


        return finalSubjectList;
    }
}
