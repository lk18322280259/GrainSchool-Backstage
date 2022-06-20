package com.atguigu.msmservice.controller;


import com.atguigu.commonutils.R;
import com.atguigu.msmservice.service.MsmService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Api("短信管理")
@RestController
@RequestMapping("/edumsm/msm")
@CrossOrigin
public class MsmController {

    @Autowired
    private MsmService msmService;

    //发送短信的方法
    @GetMapping("send/{phone}")
    @ApiOperation("获取短信验证码接口")
    public R sendMsm(
            @ApiParam(name = "phone", value = "手机号", required = true)
            @PathVariable String phone) {

        String code = msmService.sendTencentMsg(phone);
        return R.ok().data("code", code);
    }
}
