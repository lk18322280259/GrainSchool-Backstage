package com.atguigu.eduservice.client;

import com.atguigu.commonutils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

//需要调用的服务名，从nacos中取
//使用熔断器，需要在接口上填写熔断的实现类
@FeignClient(name = "service-vod", fallback = VodFileDegradeFeignClient.class) //服务出错执行的实现类
@Component
public interface VodClient {

    //删除单个视频
    //写服务的全路径，且要在指定PathVariable中指定参数
    @DeleteMapping("/eduvod/video/removeTencentVideo/{fileId}")
    public R removeTencentVideoByFileId(@PathVariable("fileId") String fileId);

    //删除多个视频
    @DeleteMapping("/eduvod/video/delete-batch")
    public R removeTencentVideoByFileIds(@RequestParam("videoList") List<String> videoList);
}

