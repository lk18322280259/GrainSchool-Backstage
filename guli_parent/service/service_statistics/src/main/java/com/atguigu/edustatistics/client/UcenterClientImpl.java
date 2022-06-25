package com.atguigu.edustatistics.client;

import com.atguigu.commonutils.R;
import com.atguigu.servicebase.exceptionhandler.GuliException;

/**
 * @Author luokai
 */
public class UcenterClientImpl implements UcenterClient{
    @Override
    public R registerCount(String day) {
        throw new GuliException(20001,"远程调用Ucenter统计当天人数服务异常");
    }
}
