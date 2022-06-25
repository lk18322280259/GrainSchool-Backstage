package com.atguigu.orderservice.service;

import com.atguigu.orderservice.entity.PayLog;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * <p>
 * 支付日志表 服务类
 * </p>
 *
 * @author luokai
 * @since 2022-06-24
 */
public interface PayLogService extends IService<PayLog> {

    /**
     * 微信支付创建二维码
     * @param orderNo 订单号
     * @return 二维码地址和其他信息
     */
    Map createNative(String orderNo);

    /**
     * 查询订单状态
     * @param orderNo 订单号
     * @return 订单数据
     */
    Map<String, String> queryPayStatus(String orderNo);

    /**
     * 向支付表中添加记录，更新订单表订单状态
     * @param map 订单数据
     */
    void updateOrderStatus(Map<String, String> map);

    /**
     * 手动提交跳过支付
     * @param orderNo 订单号
     */
    void skipPay(String orderNo);
}
