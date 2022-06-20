package com.atguigu.educenter.entity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel(value="登录对象", description="手机号和验证码登录")
public class VerCodeLoginVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "手机号")
    private String mobile;

    @ApiModelProperty(value = "验证码")
    private String code;

}

