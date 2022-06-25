package com.atguigu.orderservice.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.atguigu.commonutils.R;
import com.atguigu.orderservice.common.ConstantPay;
import com.atguigu.orderservice.entity.Order;
import com.atguigu.orderservice.entity.PayLog;
import com.atguigu.orderservice.mapper.PayLogMapper;
import com.atguigu.orderservice.service.OrderService;
import com.atguigu.orderservice.service.PayLogService;
import com.atguigu.orderservice.utils.HttpClient;
import com.atguigu.servicebase.exceptionhandler.GuliException;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.wxpay.sdk.WXPayUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 支付日志表 服务实现类
 * </p>
 *
 * @author luokai
 * @since 2022-06-24
 */
@SuppressWarnings({"AlibabaCollectionInitShouldAssignCapacity", "unchecked", "DuplicatedCode"})
@Service
public class PayLogServiceImpl extends ServiceImpl<PayLogMapper, PayLog> implements PayLogService {

    @Autowired
    private OrderService orderService;

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 微信支付创建二维码
     * @param orderNo 订单号
     * @return 二维码地址和其他信息
     */
    @Override
    public Map createNative(String orderNo) {

        try {

            //1、根据订单号获取订单信息
            LambdaQueryWrapper<Order> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(Order::getOrderNo, orderNo);
            Order order = orderService.getOne(queryWrapper);

            Map m = new HashMap();
            //1、设置支付参数
            m.put("appid", ConstantPay.APPID);
            m.put("mch_id", ConstantPay.PARTNER);
            m.put("nonce_str", WXPayUtil.generateNonceStr());
            m.put("body", order.getCourseTitle());
            m.put("out_trade_no", orderNo);
            m.put("total_fee", order.getTotalFee().multiply(new BigDecimal("100")).longValue()+"");
            m.put("spbill_create_ip", ConstantPay.ADDR);
            m.put("notify_url", ConstantPay.NOTIFYURL);
            m.put("trade_type", "NATIVE");

            //2、HTTPClient来根据URL访问第三方接口并且传递参数
            HttpClient client = new HttpClient("https://api.mch.weixin.qq.com/pay/unifiedorder");
            //client设置参数（将map转为xml发送）
            client.setXmlParam(WXPayUtil.generateSignedXml(m, ConstantPay.PARTNERKEY));
            client.setHttps(true);
            client.post();

            //3、返回第三方的数据（xml格式）
            String xml = client.getContent();
            Map<String, String> resultMap = WXPayUtil.xmlToMap(xml);

            //4、封装返回结果集
            Map map = new HashMap<>();
            map.put("out_trade_no", orderNo);
            map.put("course_id", order.getCourseId());
            map.put("total_fee", order.getTotalFee());
            map.put("result_code", resultMap.get("result_code"));
            map.put("code_url", resultMap.get("code_url"));

            //微信支付二维码2小时过期，可采取2小时未支付取消订单
            redisTemplate.opsForValue().set(orderNo, map, 120, TimeUnit.MINUTES);

            //返回数据
            return map;

        } catch (Exception e) {

            throw new GuliException(20001,"生成支付二维码异常");
        }
    }

    /**
     * 查询订单状态
     * @param orderNo 订单号
     * @return 订单数据
     */
    @Override
    public Map<String, String> queryPayStatus(String orderNo) {

        try {

            Map m = new HashMap();
            //1、设置支付参数
            m.put("appid", ConstantPay.APPID);
            m.put("mch_id", ConstantPay.PARTNER);
            m.put("out_trade_no", orderNo);
            m.put("nonce_str", WXPayUtil.generateNonceStr());

            //2、发送请求
            HttpClient client = new HttpClient("https://api.mch.weixin.qq.com/pay/orderquery");
            //client设置参数（将map转为xml发送）
            client.setXmlParam(WXPayUtil.generateSignedXml(m, ConstantPay.PARTNERKEY));
            client.setHttps(true);
            client.post();

            //3、得到请求的返回内容
            String xml = client.getContent();
            Map<String, String> resultMap = WXPayUtil.xmlToMap(xml);

            return resultMap;

        } catch (Exception e) {

            throw new GuliException(20001,"支付异常");
        }

    }

    /**
     * 向支付表中添加记录，更新订单表订单状态
     * @param map 订单数据
     */
    @Override
    public void updateOrderStatus(Map<String, String> map) {

        //获取订单id
        String orderNo = map.get("out_trade_no");
        //根据订单id查询订单信息
        LambdaQueryWrapper<Order> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Order::getOrderNo,orderNo);
        Order order = orderService.getOne(queryWrapper);

        if(order.getStatus() == 1) {
            return;
        }

        // 1 代表已经支付
        order.setStatus(1);
        orderService.updateById(order);

        //记录支付日志
        PayLog payLog=new PayLog();
        //支付订单号
        payLog.setOrderNo(order.getOrderNo());
        //订单支付时间
        payLog.setPayTime(new Date());
        //支付类型(1-微信支付)
        payLog.setPayType(1);
        //总金额(分)
        payLog.setTotalFee(order.getTotalFee());
        //支付状态
        payLog.setTradeState(map.get("trade_state"));
        payLog.setTransactionId(map.get("transaction_id"));
        //其他属性
        payLog.setAttr(JSONObject.toJSONString(map));

        //插入到支付日志表
        this.save(payLog);
    }

    /**
     * 手动提交跳过支付
     * @param orderNo 订单号
     */
    @Override
    public void skipPay(String orderNo) {

        //查询订单表，修改订单支付状态
        LambdaQueryWrapper<Order> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Order::getOrderNo,orderNo);
        Order order = orderService.getOne(queryWrapper);

        //支付成功就跳过
        if(order.getStatus() == 1) {
            return;
        }
        //更新订单的支付状态
        order.setStatus(1);
        orderService.updateById(order);

        //记录支付日志
        PayLog payLog=new PayLog();
        //支付订单号
        payLog.setOrderNo(order.getOrderNo());
        //订单支付时间
        payLog.setPayTime(new Date());
        //支付类型(1-微信支付)
        payLog.setPayType(1);
        //总金额(分)
        payLog.setTotalFee(order.getTotalFee());
        //支付状态
        String tradeState = "SUCCESS";
        payLog.setTradeState(tradeState);
        //流水号
        payLog.setTransactionId("0000000000000000000000000000");
        //其他属性
        payLog.setAttr("手动跳过支付");

        //插入到支付日志表
        this.save(payLog);
    }
}
