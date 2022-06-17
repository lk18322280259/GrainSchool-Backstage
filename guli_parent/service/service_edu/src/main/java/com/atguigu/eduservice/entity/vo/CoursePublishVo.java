package com.atguigu.eduservice.entity.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class CoursePublishVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "课程ID")
    private String id;

    @ApiModelProperty(value = "课程名称")
    private String title;

    @ApiModelProperty(value = "课程封面")
    private String cover;

    @ApiModelProperty(value = "课程数")
    private Integer lessonNum;

    @ApiModelProperty(value = "一级分类")
    private String subjectLevelOne;

    @ApiModelProperty(value = "二级分类")
    private String subjectLevelTwo;

    @ApiModelProperty(value = "讲师姓名")
    private String teacherName;

    @ApiModelProperty(value = "课程价格")
    private String price;//只用于显示
}
