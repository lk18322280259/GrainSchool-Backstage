package com.atguigu.educms.controller;

import com.atguigu.commonutils.R;
import com.atguigu.educms.entity.CrmBanner;
import com.atguigu.educms.service.CrmBannerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 前台查看幻灯片
 * @Author luokai
 */
@Api("前台查看幻灯片")
@RestController
@RequestMapping("/educms/bannerfront")
@SuppressWarnings("AlibabaCommentsMustBeJavadocFormat")
//@CrossOrigin
public class BannerFrontController {

    @Autowired
    private CrmBannerService bannerService;

    //获取首页
    @ApiOperation(value = "获取首页banner")
    @GetMapping("getAllBanner")
    public R index() {

        List<CrmBanner> list = bannerService.getAllBannerList();
        return R.ok().data("list", list);
    }

}
