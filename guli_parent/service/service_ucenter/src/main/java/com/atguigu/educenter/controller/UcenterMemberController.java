package com.atguigu.educenter.controller;


import com.atguigu.commonutils.R;
import com.atguigu.educenter.entity.UcenterMember;
import com.atguigu.educenter.entity.vo.RegisterVo;
import com.atguigu.educenter.entity.vo.UserLoginVo;
import com.atguigu.educenter.service.UcenterMemberService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 会员表 前端控制器
 * </p>
 *
 * @author LuoKai
 * @since 2022-06-20
 */
@Api("用户管理")
@RestController
@RequestMapping("/educenter/member")
@CrossOrigin
public class UcenterMemberController {

    @Autowired
    private UcenterMemberService memberService;

    //登录
    @PostMapping("login")
    @ApiOperation("用户登录接口")
    public R loginUser(
            @ApiParam(name = "member", value = "登录对象封装", required = true)
            @RequestBody UserLoginVo member) {

        //调用service实现登录，返回token值，使用Jwt生成
        String token = memberService.login(member);
        return R.ok().data("token", token);
    }

    //注册
    @ApiOperation(value = "会员注册")
    @PostMapping("register")
    public R register(@RequestBody RegisterVo registerVo){

        memberService.register(registerVo);
        return R.ok();
    }

    //根据token获取用户信息
    @ApiOperation(value = "解析token获取用户信息")
    @GetMapping("getMemberInfo")
    public R getMemberInfo(HttpServletRequest request) {

        UcenterMember member = memberService.getMemberInfo(request);
        return R.ok().data("userInfo",member);
    }
}

