package com.atguigu.educenter.utils;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @Author luokai
 */
@Component
public class Constant implements InitializingBean {

    @Value("${custom-parameters.wx-open-param.app-id}")
    private String appId;

    @Value("${custom-parameters.wx-open-param.app-secret}")
    private String appSecret;

    @Value("${custom-parameters.wx-open-param.redirect-url}")
    private String redirectUrl;

    @Value("${custom-parameters.register-param.default-avatar}")
    private String defaultAvatar;

    public static String WX_OPEN_APP_ID;
    public static String WX_OPEN_APP_SECRET;
    public static String WX_OPEN_REDIRECT_URL;
    public static String USER_DEFAULT_AVATAR;

    @Override
    public void afterPropertiesSet() throws Exception {
        WX_OPEN_APP_ID = appId;
        WX_OPEN_APP_SECRET = appSecret;
        WX_OPEN_REDIRECT_URL = redirectUrl;
        USER_DEFAULT_AVATAR = defaultAvatar;
    }
}
