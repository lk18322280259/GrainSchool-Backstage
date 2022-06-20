package com.atguigu.educenter.service.impl;

import com.atguigu.commonutils.JwtUtils;
import com.atguigu.commonutils.MD5;
import com.atguigu.educenter.entity.UcenterMember;
import com.atguigu.educenter.entity.vo.RegisterVo;
import com.atguigu.educenter.entity.vo.UserLoginVo;
import com.atguigu.educenter.mapper.UcenterMemberMapper;
import com.atguigu.educenter.service.UcenterMemberService;
import com.atguigu.servicebase.exceptionhandler.GuliException;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 会员表 服务实现类
 * </p>
 *
 * @author LuoKai
 * @since 2022-06-20
 */
@Service
public class UcenterMemberServiceImpl extends ServiceImpl<UcenterMemberMapper, UcenterMember> implements UcenterMemberService {


    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    //实现单点登录
    @Override
    public String login(UserLoginVo member) {

        String mobile = member.getMobile();
        String password = member.getPassword();

        if(StringUtils.isEmpty(mobile) || StringUtils.isEmpty(password)) {
            throw new GuliException(20001, "手机号和密码不能同时为空");
        }

        //判断手机号是否正确
        LambdaQueryWrapper<UcenterMember> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UcenterMember::getMobile, mobile);
        UcenterMember mobileMember = this.getOne(wrapper);

        //判断手机号在数据库是否存在
        if (mobileMember == null) {
            throw new GuliException(20001, "登陆失败");
        }

        //MD5加密密码
        String md5Pwd = MD5.encrypt(password);

        //判断密码
        if (!md5Pwd.equals(mobileMember.getPassword())) {
            throw new GuliException(20001, "密码错误, 请重新输入");
        }

        //判断状态
        if (mobileMember.getIsDisabled()) {
            throw new GuliException(20001, "账号已被禁用");
        }

        //状态正常，允许登录，生成token
        String token = JwtUtils.getJwtToken(mobileMember.getId(), mobileMember.getPassword());

        return token;
    }

    //用户注册
    @Override
    public void register(RegisterVo registerVo) {

        String code = registerVo.getCode();
        String mobile = registerVo.getMobile();
        String nickname = registerVo.getNickname();
        String password = registerVo.getPassword();

        //非空判断
        if(StringUtils.isEmpty(mobile) || StringUtils.isEmpty(password)
            || StringUtils.isEmpty(code) || StringUtils.isEmpty(nickname)) {

            throw new GuliException(20001, "注册失败，注册数据都不能为空");
        }

        //判断验证码
        String redisCode = redisTemplate.opsForValue().get(mobile);
        if(!code.equals(redisCode)) {
            throw new GuliException(20001, "验证码错误");
        }

        //判断手机号是否已注册过
        LambdaQueryWrapper<UcenterMember> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UcenterMember::getMobile, mobile);
        int count = this.count(wrapper);
        if (count==1) {
            throw new GuliException(20001, "手机号"+mobile+"已注册过");
        }

        //数据添加进数据库
        UcenterMember ucenterMember = new UcenterMember();
        ucenterMember.setMobile(mobile);
        ucenterMember.setPassword(MD5.encrypt(password));
        ucenterMember.setNickname(nickname);
        ucenterMember.setIsDisabled(false); //用户未禁用
        ucenterMember.setAvatar("https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif");
        this.save(ucenterMember);

        //添加验证码进redis
        redisTemplate.opsForValue().set(mobile, code);

    }

    //解析token获取用户信息
    @Override
    public UcenterMember getMemberInfo(HttpServletRequest request) {

        //调用jwt工具类，根据request获取头信息并解析id
        String memberId = JwtUtils.getMemberIdByJwtToken(request);
        //查询数据库获取用户信息
        UcenterMember member = this.getById(memberId);

        return member;
    }
}
