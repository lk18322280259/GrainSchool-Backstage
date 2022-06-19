package com.atguigu.eduservice.client;

import com.atguigu.commonutils.R;
import org.springframework.stereotype.Component;

import java.util.List;

//微服务出错执行代码
@Component
public class VodFileDegradeFeignClient implements VodClient{

    @Override
    public R removeTencentVideoByFileId(String fileId) {
        return R.error().message("微服务删除视频出错了");
    }

    @Override
    public R removeTencentVideoByFileIds(List<String> videoList) {
        return R.error().message("微服务批量删除视频出错了");
    }
}
