package com.atguigu.msmservice.service;

public interface MsmService {

    //获取验证码
    String sendTencentMsg(String phone);

}
