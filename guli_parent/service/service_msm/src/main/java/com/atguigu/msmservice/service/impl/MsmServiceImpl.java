package com.atguigu.msmservice.service.impl;

import com.atguigu.msmservice.service.MsmService;
import com.atguigu.msmservice.utils.Constant;
import com.atguigu.msmservice.utils.RandomUtil;
import com.atguigu.msmservice.utils.SMSUtils;
import com.atguigu.servicebase.exceptionhandler.GuliException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class MsmServiceImpl implements MsmService {

    @Autowired
    private RedisTemplate<String,String> redisTemplate;


    /**
     * 发送验证码
     */
    @Override
    public String sendTencentMsg(String phone) {

        //1 从redis中获取验证码，获取到直接返回
        String verificationCode = redisTemplate.opsForValue().get(phone);

        if (!StringUtils.isEmpty(verificationCode)) {
            return verificationCode;
        }

        // 获取手机号
        if (StringUtils.isNotEmpty(phone)) {

            String sendMsgEnable = "true";
            //启用腾讯短信验证码
            if (sendMsgEnable.equals(Constant.SENDMSGENABLE)) {
                // 生成随机的验证码
                verificationCode = RandomUtil.getFourBitRandom();
                // 调用腾讯云短信服务API完成发送短信
                String[] phoneNumberSet = {"+86" + phone};
                //验证码数组
                String[] captchaParameters = {Constant.MSGHEAD, verificationCode, Constant.CONNTIMEOUT};

                SMSUtils.sendMessage(Constant.SECRETID,
                        Constant.SECRETKEY,
                        Constant.CONNTIMEOUT,
                        Constant.SDKAPPID,
                        Constant.SIGNNAME,
                        Constant.TEMPLATEID,
                        captchaParameters,
                        phoneNumberSet);
            } else {
                //关闭腾讯短信验证码
                verificationCode = "1234";
            }
            //向redis中加入新的验证码，5分钟有效
            redisTemplate.opsForValue().set(phone, verificationCode, Long.parseLong(Constant.CONNTIMEOUT), TimeUnit.MINUTES);
            return verificationCode;
        }

        throw new GuliException(20001, "短信发送失败");
    }

}
