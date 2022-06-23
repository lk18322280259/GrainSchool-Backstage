package com.atguigu.orderservice.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 订单号工具类
 *
 * @author luokai
 * @since 1.0
 */
@SuppressWarnings("StringConcatenationInLoop")
public class OrderNoUtil {

    /**
     * 获取订单号
     * @return 订单id
     */
    public static String getOrderNo() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String newDate = sdf.format(new Date());
        String result = "";
        Random random = new Random();
        for (int i = 0; i < 3; i++) {
            result += random.nextInt(10);
        }
        return newDate + result;
    }

}
