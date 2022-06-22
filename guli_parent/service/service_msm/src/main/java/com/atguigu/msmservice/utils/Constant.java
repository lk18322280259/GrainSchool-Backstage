package com.atguigu.msmservice.utils;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;

/**
 * @Author luokai
 */
public class Constant implements InitializingBean {

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

    public static String SECRETID;
    public static String SECRETKEY;
    public static String CONNTIMEOUT;
    public static String SDKAPPID;
    public static String SIGNNAME;
    public static String TEMPLATEID;
    public static String MSGHEAD;
    public static String SENDMSGENABLE;

    @Override
    public void afterPropertiesSet() throws Exception {
        SECRETID = secretId;
        SECRETKEY = secretKey;
        CONNTIMEOUT = connTimeout;
        SDKAPPID = sdkAppId;
        SIGNNAME = signName;
        TEMPLATEID = templateId;
        MSGHEAD = msgHead;
        SENDMSGENABLE = sendMsgEnable;

    }
}
