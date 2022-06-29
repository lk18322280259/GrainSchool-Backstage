package com.atguigu.educms.controller;


import com.atguigu.commonutils.R;
import com.atguigu.educms.entity.CrmBanner;
import com.atguigu.educms.service.CrmBannerService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 首页banner表 前端控制器
 * </p>
 * 幻灯片后台管理
 * @author luokai
 * @since 2022-06-19
 */
@Api("幻灯片后台管理")
@RestController
@RequestMapping("/educms/banneradmin")
@SuppressWarnings("AlibabaCommentsMustBeJavadocFormat")
//@CrossOrigin
public class BannerAdminController {

    @Autowired
    private CrmBannerService bannerService;

    @ApiOperation(value = "获取所有Banner列表")
    @GetMapping("getAllBanner")
    public R getAllBanner() {

        List<CrmBanner> bannerList = bannerService.list(null);
        return R.ok().data("list",bannerList);
    }

    @ApiOperation(value = "获取Banner分页列表")
    @GetMapping("pageBanner/{page}/{limit}")
    public R index(
            @ApiParam(name = "page", value = "当前页码", required = true)
            @PathVariable Long page,
            @ApiParam(name = "limit", value = "每页记录数", required = true)
            @PathVariable Long limit) {

        Page<CrmBanner> pageParam = new Page<>(page, limit);
        bannerService.page(pageParam,null);
        return R.ok().data("items", pageParam.getRecords()).data("total", pageParam.getTotal());
    }

    @ApiOperation(value = "新增Banner")
    @PostMapping("addBanner")
    public R save(
            @ApiParam(name = "banner", value = "幻灯片实体", required = true)
            @RequestBody CrmBanner banner) {

        bannerService.save(banner);
        return R.ok();
    }


    @ApiOperation(value = "获取Banner")
    @GetMapping("get/{id}")
    public R get(
            @ApiParam(name = "id", value = "幻灯片ID", required = true)
            @PathVariable String id) {

        CrmBanner banner = bannerService.getById(id);
        return R.ok().data("item", banner);
    }

    @ApiOperation(value = "修改Banner")
    @PostMapping("update")
    public R updateById(
            @ApiParam(name = "banner", value = "幻灯片实体", required = true)
            @RequestBody CrmBanner banner) {

        bannerService.updateById(banner);
        return R.ok();
    }

    @ApiOperation(value = "删除Banner")
    @DeleteMapping("remove/{id}")
    public R remove(
            @ApiParam(name = "id", value = "幻灯片ID", required = true)
            @PathVariable String id) {

        bannerService.removeById(id);
        return R.ok();
    }
}

