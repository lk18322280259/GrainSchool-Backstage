package com.atguigu.educenter.service.impl;

import com.atguigu.commonutils.JwtUtils;
import com.atguigu.commonutils.MD5;
import com.atguigu.educenter.entity.UcenterMember;
import com.atguigu.educenter.entity.vo.RegisterVo;
import com.atguigu.educenter.entity.vo.PwdLoginVo;
import com.atguigu.educenter.entity.vo.VerCodeLoginVo;
import com.atguigu.educenter.mapper.UcenterMemberMapper;
import com.atguigu.educenter.service.UcenterMemberService;
import com.atguigu.educenter.utils.Constant;
import com.atguigu.educenter.utils.HttpClientUtils;
import com.atguigu.servicebase.exceptionhandler.GuliException;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;


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
            member.setAvatar(Constant.USER_DEFAULT_AVATAR);
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

    /**
     * 根据微信扫码查询数据库是否存在用户
     * @param openid 微信用户openid
     * @return 用户类
     */
    @Override
    public UcenterMember getOpenIdMember(String openid) {

        LambdaQueryWrapper<UcenterMember> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UcenterMember::getOpenid, openid);
        UcenterMember member = this.getOne(wrapper);
        return member;
    }

    /**
     * 生成二维码的链接重定向显示二维码
     *
     * @return 重定向到二维码页面链接
     */
    @Override
    public String getWxCode() {

        // 微信开放平台授权baseUrl
        String baseUrl = "https://open.weixin.qq.com/connect/qrconnect" +
                "?appid=%s" +
                "&redirect_uri=%s" +
                "&response_type=code" +
                "&scope=snsapi_login" +
                "&state=%s" +
                "#wechat_redirect";

        // 回调地址
        //获取业务服务器重定向地址
        String redirectUrl = Constant.WX_OPEN_REDIRECT_URL;
        try {
            //url编码
            redirectUrl = URLEncoder.encode(redirectUrl, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new GuliException(20001, e.getMessage());
        }

        // 防止csrf攻击（跨站请求伪造攻击）
        //String state = UUID.randomUUID().toString().replaceAll("-", "");//一般情况下会使用一个随机数
        //为了让大家能够使用我搭建的外网的微信回调跳转服务器，这里填写你在ngrok的前置域名
        String state = "atguigu";

        // 采用redis等进行缓存state 使用sessionId为key 30分钟后过期，可配置
        //键："wechar-open-state-" + httpServletRequest.getSession().getId()
        //值：satte
        //过期时间：30分钟

        //生成qrcodeUrl
        String qrcodeUrl = String.format(
                baseUrl,
                Constant.WX_OPEN_APP_ID,
                redirectUrl,
                state);

        return qrcodeUrl;
    }

    /**
     * 从code中获取用户信息并生成token
     * @return token
     */
    @Override
    public String getCallBackToken(String code, String state) {

        //1 获取code值，临时票据，类似于验证码

        //2 拿着code请求微信固定的地址，得到两个值 access_token 和 openid
        String baseAccessTokenUrl = "https://api.weixin.qq.com/sns/oauth2/access_token" +
                "?appid=%s" +
                "&secret=%s" +
                "&code=%s" +
                "&grant_type=authorization_code";

        String accessTokenUrl = String.format(baseAccessTokenUrl,
                Constant.WX_OPEN_APP_ID,
                Constant.WX_OPEN_APP_SECRET,
                code);

        //使用拼接好的地址，得到 access_token 和 openid
        //使用httpclient发送请求，得到返回结果
        try {
            String accessTokenInfo = HttpClientUtils.get(accessTokenUrl);

            System.out.println(accessTokenInfo);

            //从accessTokenInfo字符串获取出来两个值access_token 和 openid
            //把accessTokenInfo字符串转换map集合，根据map里面key获取对应值
            //使用json转换工具 Gson
            Gson gson = new Gson();
            HashMap mapAccessToken = gson.fromJson(accessTokenInfo, HashMap.class);

            String accessToken = (String) mapAccessToken.get("access_token");
            double expiresIn = (double) mapAccessToken.get("expires_in");
            String refreshToken = (String) mapAccessToken.get("refresh_token");
            String openid = (String) mapAccessToken.get("openid");
            String scope = (String) mapAccessToken.get("scope");
            String unionid = (String) mapAccessToken.get("unionid");

            //判断数据库中是否存在相同微信信息，根据openid判断
            UcenterMember member = this.getOpenIdMember(openid);
            //member为空，需要新增到数据库
            if (member == null) {

                //访问微信的资源服务器，获取用户信息
                String baseUserInfoUrl = "https://api.weixin.qq.com/sns/userinfo" +
                        "?access_token=%s" +
                        "&openid=%s";

                String userInfoUrl = String.format(
                        baseUserInfoUrl,
                        accessToken,
                        openid);

                String resultUserInfo = HttpClientUtils.get(userInfoUrl);
                System.out.println("resultUserInfo==========" + resultUserInfo);

                HashMap userInfoMap = gson.fromJson(resultUserInfo, HashMap.class);
                //昵称
                String nickname = (String) userInfoMap.get("nickname");
                //头像
                String headimgurl = (String) userInfoMap.get("headimgurl");

                member = new UcenterMember();
                member.setOpenid(openid);
                member.setNickname(nickname);
                member.setAvatar(headimgurl);

                this.save(member);
            }

            //使用jwt根据member对象生成token
            String token = JwtUtils.getJwtToken(member.getId(), member.getNickname());

            return token;

        } catch (Exception e) {
            throw new GuliException(20001, "登陆失败");
        }
    }

    /**
     * 根据token字符串获取用户信息
     * @param id 用户id
     * @return 用户信息
     */
    @Override
    public UcenterMember getUserInfoById(String id) {

        UcenterMember member = this.getById(id);
        return member;
    }

}
