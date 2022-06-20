package com.atguigu.educenter.service;

import com.atguigu.educenter.entity.UcenterMember;
import com.atguigu.educenter.entity.vo.RegisterVo;
import com.atguigu.educenter.entity.vo.PwdLoginVo;
import com.atguigu.educenter.entity.vo.VerCodeLoginVo;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 会员表 服务类
 * </p>
 *
 * @author LuoKai
 * @since 2022-06-20
 */
public interface UcenterMemberService extends IService<UcenterMember> {

    //实现单点登录
    String loginByPwd(PwdLoginVo pwdLoginVo);

    //验证码登录
    String loginByCode(VerCodeLoginVo verCodeLoginVo);

    //用户注册
    void register(RegisterVo registerVo);

    //解析token获取用户信息
    UcenterMember getMemberInfo(HttpServletRequest request);

    //展示个人信息
    UcenterMember showUserInfo(String userId);
}
