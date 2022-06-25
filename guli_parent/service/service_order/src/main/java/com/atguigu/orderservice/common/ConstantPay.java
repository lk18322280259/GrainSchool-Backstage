package com.atguigu.orderservice.common;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @Author luokai
 */
@Component
public class ConstantPay implements InitializingBean {

    @Value("${custom-parameters.wx-pay.app-id}")
    private String appId;
    @Value("${custom-parameters.wx-pay.partner}")
    private String partner;
    @Value("${custom-parameters.wx-pay.partner-key}")
    private String partnerKey;
    @Value("${custom-parameters.wx-pay.notify-url}")
    private String notifyUrl;
    @Value("${custom-parameters.wx-pay.addr}")
    private String addr;

    public static String APPID;
    public static String PARTNER;
    public static String PARTNERKEY;
    public static String NOTIFYURL;
    public static String ADDR;


    @Override
    public void afterPropertiesSet() throws Exception {

        APPID = appId;
        PARTNER = partner;
        PARTNERKEY = partnerKey;
        NOTIFYURL = notifyUrl;
        ADDR = addr;
    }
}
