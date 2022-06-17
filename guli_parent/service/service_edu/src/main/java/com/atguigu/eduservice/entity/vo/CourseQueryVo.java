package com.atguigu.eduservice.entity.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class CourseQueryVo implements Serializable {


    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "课程名称,模糊查询")
    private String title;

    @ApiModelProperty(value = "状态 Normal已发布 Draft未发布")
    private String status;

    @ApiModelProperty(value = "查询开始创建时间", example = "2019-01-01 10:10:10")
    private String beginCreate;//注意，这里使用的是String类型，前端传过来的数据无需进行类型转换

    @ApiModelProperty(value = "查询结束创建时间", example = "2019-12-01 10:10:10")
    private String endCreate;

    @ApiModelProperty(value = "查询开始修改时间", example = "2019-01-01 10:10:10")
    private String beginModified;//注意，这里使用的是String类型，前端传过来的数据无需进行类型转换

    @ApiModelProperty(value = "查询结束修改时间", example = "2019-12-01 10:10:10")
    private String endModified;
}
