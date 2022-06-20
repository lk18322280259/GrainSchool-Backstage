package com.atguigu.msmservice.service.impl;

import com.atguigu.msmservice.service.MsmService;
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

    // 腾讯云短信相关参数
    @Value("${custom-parameters.send-msg.secret-id}")
    private String secretId;
    @Value("${custom-parameters.send-msg.secret-key}")
    private String secretKey;
    @Value("${custom-parameters.send-msg.conn-timeout}")
    private String connTimeout;
    @Value("${custom-parameters.send-msg.sdk-app-id}")
    private String sdkAppId;
    @Value("${custom-parameters.send-msg.sign-name}")
    private String signName;
    @Value("${custom-parameters.send-msg.template-id}")
    private String templateId;
    @Value("${custom-parameters.send-msg.msg-head}")
    private String msgHead;
    @Value("${custom-parameters.send-msg.enable}")
    private String sendMsgEnable;

    //发送验证码
    @Override
    public String sendTencentMsg(String phone) {

        //1 从redis中获取验证码，获取到直接返回
        String verificationCode = redisTemplate.opsForValue().get(phone);

        if (!StringUtils.isEmpty(verificationCode)) {
            return verificationCode;
        }

        // 获取手机号
        if (StringUtils.isNotEmpty(phone)) {

            //启用腾讯短信验证码
            if (sendMsgEnable.equals("true")) {
                // 生成随机的验证码
                verificationCode = RandomUtil.getFourBitRandom();
                // 调用腾讯云短信服务API完成发送短信
                String[] phoneNumberSet = {"+86" + phone};
                //验证码数组
                String[] captchaParameters = {msgHead, verificationCode, connTimeout};

                SMSUtils.sendMessage(secretId, secretKey, connTimeout, sdkAppId, signName, templateId, captchaParameters, phoneNumberSet);
            } else {
                //关闭腾讯短信验证码
                verificationCode = "1234";
            }
            //向redis中加入新的验证码，5分钟有效
            redisTemplate.opsForValue().set(phone, verificationCode, Long.parseLong(connTimeout), TimeUnit.MINUTES);
            return verificationCode;
        }

        throw new GuliException(20001, "短信发送失败");
    }

}
