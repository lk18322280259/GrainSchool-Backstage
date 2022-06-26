package com.atguigu.educenter.controller;

import com.atguigu.educenter.service.UcenterMemberService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 重定向必须使用Controller，RestController只能返回数据
 *
 * @Author luokai
 */
@Api("微信API")
@SuppressWarnings("AlibabaCommentsMustBeJavadocFormat")
//@CrossOrigin
@Controller
@RequestMapping("/api/ucenter/wx")
public class WxApiController {

    @Autowired
    private UcenterMemberService memberService;

    /**
     * 生成微信扫码的二维码
     *
     * @return 重定向到首页
     */
    @ApiOperation("微信扫码登录接口")
    @GetMapping("login")
    public String getWxCode() {

        //生成二维码的链接重定向显示二维码
        String qrCodeUrl = memberService.getWxCode();
        //重定向显示微信二维码
        return "redirect:" + qrCodeUrl;
    }

    /**
     * 获取扫描人信息，添加数据
     */
    @GetMapping("callback")
    public String callback(String code, String state) {

        //从code中获取用户信息并生成token
        String token = memberService.getCallBackToken(code, state);
        //返回首页面
        return "redirect:http://localhost:3000?token="+token;
    }
}
