package TestDemo;

import com.atguigu.educenter.utils.Constant;
import com.atguigu.servicebase.exceptionhandler.GuliException;
import org.junit.Test;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class TestEncode {

    public static void main(String[] args) {
        String redirectUrl = "http://guli.shop/api/ucenter/wx/callback;";
        try {
            //url编码
            redirectUrl = URLEncoder.encode(redirectUrl, "UTF-8");
            System.out.println(redirectUrl);
        } catch (UnsupportedEncodingException e) {
            throw new GuliException(20001, e.getMessage());
        }
    }
}
