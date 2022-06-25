package com.atguigu.orderservice.controller;


import com.atguigu.commonutils.R;
import com.atguigu.orderservice.service.PayLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * <p>
 * 支付日志表 前端控制器
 * </p>
 *
 * @author luokai
 * @since 2022-06-24
 */
@SuppressWarnings("unchecked")
@CrossOrigin
@RestController
@RequestMapping("/orderservice/paylog")
public class PayLogController {

    @Autowired
    private PayLogService payLogService;

    /**
     * 微信支付创建二维码
     * @return 成功
     */
    @GetMapping("createNative/{orderNo}")
    public R createNative (@PathVariable String orderNo) {

        //返回信息，包含二维码地址和其他信息
        Map map = payLogService.createNative(orderNo);
        System.out.println("返回二维码map集合"+map);
        return R.ok().data(map);
    }

    /**
     * 查询订单的支付状态
     * @return 成功
     */
    @GetMapping("queryPayStatus/{orderNo}")
    public R queryPayStatus(@PathVariable String orderNo) {

        //查询支付状态
        Map<String,String> map = payLogService.queryPayStatus(orderNo);
        System.out.println("查询订单状态map集合"+map);

        if(map==null) {
            return R.error().message("支付出错");
        }

        //支付成功
        String field = "trade_state";
        String flag = "SUCCESS";
        if(map.get(field).equals(flag)) {

            //向支付表中添加记录，更新订单表订单状态
            payLogService.updateOrderStatus(map);
            return R.ok().message("支付成功");
        }
        return R.ok().code(25000).message("支付中...");
    }

    /**
     * 手动提交跳过支付
     * @param orderNo 订单号
     * @return 结果
     */
    @GetMapping("skipPay/{orderNo}")
    public R skipPay(@PathVariable String orderNo) {

        payLogService.skipPay(orderNo);
        return R.ok().message("支付成功");
    }
}

