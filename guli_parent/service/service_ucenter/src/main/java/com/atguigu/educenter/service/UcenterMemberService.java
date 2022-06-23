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

    /**
     * 实现单点登录
     * @param pwdLoginVo 密码登陆对象
     * @return token
     */
    String loginByPwd(PwdLoginVo pwdLoginVo);

    /**
     * 验证码登录
     * @param verCodeLoginVo 验证码登录对象
     * @return token
     */
    String loginByCode(VerCodeLoginVo verCodeLoginVo);


    /**
     * 用户注册
     * @param registerVo 注册对象
     */
    void register(RegisterVo registerVo);

    /**
     * 解析token获取用户信息
     * @param request 请求对象
     * @return UcenterMember实体类
     */
    UcenterMember getMemberInfo(HttpServletRequest request);

    /**
     * 展示个人信息
     * @param userId 用户ID
     * @return UcenterMember实体类
     */
    UcenterMember showUserInfo(String userId);

    /**
     * 根据微信扫码查询数据库是否存在用户
     * @param openid 微信用户openid
     * @return 用户类
     */
    UcenterMember getOpenIdMember(String openid);

    /**
     * 生成二维码的链接重定向显示二维码
     *
     * @return 重定向到二维码页面链接
     */
    String getWxCode();

    /**
     * 从code中获取用户信息并生成token
     * @param code 二维码code
     * @param state 二维码state
     * @return token
     */
    String getCallBackToken(String code, String state);

    /**
     * 根据token字符串获取用户信息
     * @param id 用户id
     * @return 用户信息
     */
    UcenterMember getUserInfoById(String id);
}
