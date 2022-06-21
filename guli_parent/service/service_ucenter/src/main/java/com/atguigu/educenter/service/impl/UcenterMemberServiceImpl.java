package com.atguigu.educenter.service.impl;

import com.atguigu.commonutils.JwtUtils;
import com.atguigu.commonutils.MD5;
import com.atguigu.educenter.entity.UcenterMember;
import com.atguigu.educenter.entity.vo.RegisterVo;
import com.atguigu.educenter.entity.vo.PwdLoginVo;
import com.atguigu.educenter.entity.vo.VerCodeLoginVo;
import com.atguigu.educenter.mapper.UcenterMemberMapper;
import com.atguigu.educenter.service.UcenterMemberService;
import com.atguigu.servicebase.exceptionhandler.GuliException;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
@SuppressWarnings({"DuplicatedCode", "SpringJavaAutowiredFieldsWarningInspection"})
@Service
public class UcenterMemberServiceImpl extends ServiceImpl<UcenterMemberMapper, UcenterMember> implements UcenterMemberService {


    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    @Value("${custom-parameters.default-avatar}")
    private String defaultAvatar;

    /**
     * 实现单点登录
     */
    @Override
    public String loginByPwd(PwdLoginVo pwdLoginVo) {

        String mobile = pwdLoginVo.getMobile();
        String password = pwdLoginVo.getPassword();

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

    /**
     * 验证码登录
     */
    @Override
    public String loginByCode(VerCodeLoginVo verCodeLoginVo) {

        String mobile = verCodeLoginVo.getMobile();
        String code = verCodeLoginVo.getCode();

        if(StringUtils.isEmpty(mobile) || StringUtils.isEmpty(code)) {
            throw new GuliException(20001, "手机号和验证码不能同时为空");
        }

        //从redis中取出密码
        String redisCode = redisTemplate.opsForValue().get(mobile);
        if(!code.equals(redisCode)) {
            throw new GuliException(20001, "验证码错误");
        }

        //验证码正确
        //判断手机号是否注册过
        LambdaQueryWrapper<UcenterMember> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UcenterMember::getMobile, mobile);
        UcenterMember mobileMember = this.getOne(wrapper);

        String memberId = "";
        String memberPwd = "";

        //用户未注册过
        if (mobileMember==null) {
            //将用户账号信息写入数据库
            UcenterMember member = new UcenterMember();

            member.setMobile(mobile);
            //为用户设置默认密码
            member.setPassword(MD5.encrypt("123456"));
            //为用户设置默认姓名
            member.setNickname("用户"+mobile);
            //用户未禁用
            member.setIsDisabled(false);
            member.setAvatar(defaultAvatar);
            this.save(member);

            memberId = member.getId();
            memberPwd = member.getPassword();
        } else {
            //用户注册过
            //判断状态
            if (mobileMember.getIsDisabled()) {
                throw new GuliException(20001, "账号已被禁用");
            }

            memberId = mobileMember.getId();
            memberPwd = mobileMember.getPassword();
        }


        //状态正常，允许登录，生成token
        String token = JwtUtils.getJwtToken(memberId, memberPwd);

        return token;
    }

    /**
     * 用户注册
     */
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
        //用户未禁用
        ucenterMember.setIsDisabled(false);
        ucenterMember.setAvatar("https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif");
        this.save(ucenterMember);

        //添加验证码进redis
        redisTemplate.opsForValue().set(mobile, code);

    }

    /**
     * 解析token获取用户信息
     */
    @Override
    public UcenterMember getMemberInfo(HttpServletRequest request) {

        //调用jwt工具类，根据request获取头信息并解析id
        String memberId = JwtUtils.getMemberIdByJwtToken(request);
        //查询数据库获取用户信息
        UcenterMember member = this.getById(memberId);

        return member;
    }

    /**
     * 展示个人信息
     */
    @Override
    public UcenterMember showUserInfo(String userId) {
        UcenterMember member = this.getById(userId);
        return member;
    }

}
