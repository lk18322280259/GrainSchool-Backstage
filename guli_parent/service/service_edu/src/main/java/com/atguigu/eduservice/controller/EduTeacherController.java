package com.atguigu.eduservice.controller;


import com.atguigu.commonutils.R;
import com.atguigu.eduservice.entity.EduTeacher;
import com.atguigu.eduservice.entity.vo.TeacherQueryVo;
import com.atguigu.eduservice.service.EduTeacherService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 讲师 前端控制器
 * </p>
 *
 * @author luokai
 * @since 2022-05-24
 */
@Api("讲师管理")
@RestController
@RequestMapping("/eduservice/teacher")
@SuppressWarnings("AlibabaCommentsMustBeJavadocFormat")
//@CrossOrigin
public class EduTeacherController {

    //注入service
    @Autowired
    private EduTeacherService teacherService;


    /**
     * 1 查询讲师表所有数据
     * rest风格
     *
     * @return 讲师列表
     */
    @ApiOperation("查询所有讲师接口")
    @GetMapping("findAll")
    public R findAllTeacher() {

        //调用service方法实现查询所有操作
        List<EduTeacher> list = teacherService.list(null);
        return R.ok().data("list", list);
    }

    /**
     * 2 逻辑删除讲师的方法
     *
     * @param id 讲师id
     * @return 成功
     */
    @ApiOperation("逻辑删除讲师接口")
    @DeleteMapping("{id}")
    public R removeTeacher(
            @ApiParam(name = "id", value = "讲师ID", required = true)
            @PathVariable String id) {

        boolean remove = teacherService.removeById(id);

        return remove ? R.ok() : R.error();
    }

    /**
     * 3 讲师分页功能
     *
     * @param current 当前页
     * @param limit 每页条数
     * @return 分页列表
     */
    @ApiOperation("讲师分页查询接口")
    @GetMapping("pageTeacher/{current}/{limit}")
    public R pageListTeacher(
            @ApiParam(name = "current", value = "当前页", required = true)
            @PathVariable long current,
            @ApiParam(name = "limit", value = "每页条数", required = true)
            @PathVariable long limit) {

        //创建page对象
        Page<EduTeacher> pageTeacher = new Page<>(current, limit);
        //调用方法实现分页
        teacherService.page(pageTeacher, null);

        //总记录数
        long total = pageTeacher.getTotal();
        List<EduTeacher> records = pageTeacher.getRecords();

        return R.ok().data("total", total).data("rows", records);
    }

    /**
     * 4 条件查询带分页方法
     *
     * @param current 当前页
     * @param limit 每页条数
     * @param teacherQueryVo 条件查询
     * @return 分页列表
     * @RequestBody 使用json传递数据，把json数据封装到对应对象中，必须使用post请求，required = false表示当前参数可以没有
     * @ResponseBody 返回数据：json数据
     */
    @ApiOperation("讲师查询带分页接口")
    @PostMapping("pageTeacherCondition/{current}/{limit}")
    public R pageTeacherCondition(
            @ApiParam(name = "current", value = "当前页", required = true)
            @PathVariable long current,
            @ApiParam(name = "limit", value = "每页条数", required = true)
            @PathVariable long limit,
            @ApiParam(name = "teacherQueryVo", value = "条件查询封装", required = false)
            @RequestBody(required = false) TeacherQueryVo teacherQueryVo) {

        //创建page对象
        Page<EduTeacher> pageTeacher = new Page<>(current, limit);
        //构建条件
        QueryWrapper<EduTeacher> wrapper = new QueryWrapper<>();

        String name = teacherQueryVo.getName();
        Integer level = teacherQueryVo.getLevel();
        String begin = teacherQueryVo.getBegin();
        String end = teacherQueryVo.getEnd();

        //封装wrapper
        if (!StringUtils.isEmpty(name)) {
            //第一个参数为字段名
            wrapper.like("name", name);
        }
        if (!StringUtils.isEmpty(level)) {
            //第一个参数为字段名
            wrapper.eq("level", level);
        }
        if (!StringUtils.isEmpty(begin)) {
            //第一个参数为字段名
            wrapper.ge("gmt_create", begin);
        }
        if (!StringUtils.isEmpty(end)) {
            //第一个参数为字段名
            wrapper.le("gmt_create", end);
        }

        //按照创建时间倒叙排序
        wrapper.orderByDesc("gmt_create");

        //调用方法实现分页
        teacherService.page(pageTeacher, wrapper);
        //总记录数
        long total = pageTeacher.getTotal();
        List<EduTeacher> records = pageTeacher.getRecords();

        return R.ok().data("total", total).data("rows", records);
    }

    /**
     * 添加讲师
     * @param eduTeacher 讲师
     * @return 成功
     */
    @ApiOperation("添加讲师接口")
    @PostMapping("addTeacher")
    public R addTeacher(
            @ApiParam(name = "eduTeacher", value = "讲师实体", required = true)
            @RequestBody EduTeacher eduTeacher) {

        boolean save = teacherService.save(eduTeacher);
        return save ? R.ok() : R.error();
    }

    /**
     * 根据id查询讲师
     * @param id 讲师id
     * @return 讲师
     */
    @ApiOperation("根据id查询讲师接口")
    @GetMapping("getTeacher/{id}")
    public R getTeacher(
            @ApiParam(name = "id", value = "讲师id", required = true)
            @PathVariable String id) {

        EduTeacher eduTeacher = teacherService.getById(id);
        return R.ok().data("eduTeacher", eduTeacher);
    }

    /**
     * 讲师修改
     * @param eduTeacher 讲师
     * @return 成功
     */
    @ApiOperation("讲师修改接口")
    @PostMapping("updateTeacher")
    public R updateTeacher(
            @ApiParam(name = "eduTeacher", value = "讲师实体", required = true)
            @RequestBody EduTeacher eduTeacher) {

        boolean update = teacherService.updateById(eduTeacher);
        return update?R.ok():R.error();

    }
}

