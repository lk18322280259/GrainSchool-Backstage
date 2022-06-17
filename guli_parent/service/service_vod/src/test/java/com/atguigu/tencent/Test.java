package com.atguigu.tencent;

import java.util.Random;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import sun.misc.BASE64Encoder;

// 签名工具类
class Signature {
    private String secretId;
    private String secretKey;
    private long currentTime;
    private int random;
    private int signValidDuration;

    private static final String HMAC_ALGORITHM = "HmacSHA1"; //签名算法
    private static final String CONTENT_CHARSET = "UTF-8";

    public static byte[] byteMerger(byte[] byte1, byte[] byte2) {
        byte[] byte3 = new byte[byte1.length + byte2.length];
        System.arraycopy(byte1, 0, byte3, 0, byte1.length);
        System.arraycopy(byte2, 0, byte3, byte1.length, byte2.length);
        return byte3;
    }

    // 获取签名
    public String getUploadSignature() throws Exception {
        String strSign = "";
        String contextStr = "";

        // 生成原始参数字符串
        long endTime = (currentTime + signValidDuration);
        contextStr += "secretId=" + java.net.URLEncoder.encode(secretId, "utf8");
        contextStr += "&currentTimeStamp=" + currentTime;
        contextStr += "&expireTime=" + endTime;
        contextStr += "&random=" + random;

        try {
            Mac mac = Mac.getInstance(HMAC_ALGORITHM);
            SecretKeySpec secretKey = new SecretKeySpec(this.secretKey.getBytes(CONTENT_CHARSET), mac.getAlgorithm());
            mac.init(secretKey);

            byte[] hash = mac.doFinal(contextStr.getBytes(CONTENT_CHARSET));
            byte[] sigBuf = byteMerger(hash, contextStr.getBytes("utf8"));
            strSign = base64Encode(sigBuf);
            strSign = strSign.replace(" ", "").replace("\n", "").replace("\r", "");
        } catch (Exception e) {
            throw e;
        }
        return strSign;
    }

    private String base64Encode(byte[] buffer) {
        BASE64Encoder encoder = new BASE64Encoder();
        return encoder.encode(buffer);
    }

    public void setSecretId(String secretId) {
        this.secretId = secretId;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public void setCurrentTime(long currentTime) {
        this.currentTime = currentTime;
    }

    public void setRandom(int random) {
        this.random = random;
    }

    public void setSignValidDuration(int signValidDuration) {
        this.signValidDuration = signValidDuration;
    }
}

public class Test {
    public static void main(String[] args) {
        Signature sign = new Signature();

        long currentTime = System.currentTimeMillis() / 1000;
        int random = new Random().nextInt(java.lang.Integer.MAX_VALUE);
        // 设置 App 的云 API 密钥
        sign.setSecretId("AKIDX0V3SjmbGwdM1c8whMxRTXaVHqFYikeL");
        sign.setSecretKey("c5OfwFZoHZlssjZJxhIgu7Nf2SsLuMn6");
        sign.setCurrentTime(currentTime);
        sign.setRandom(random);
        sign.setSignValidDuration(3600 * 24 * 2); // 签名有效期：2天

        try {
            String signature = sign.getUploadSignature();

            System.out.println("当前时间 : " + currentTime);
            System.out.println("random : " + random);
            System.out.println("生成签名 : " + signature);
        } catch (Exception e) {
            System.out.print("获取签名失败");
            e.printStackTrace();
        }
    }
}
