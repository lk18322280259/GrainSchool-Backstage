package com.atguigu.educms.service.impl;

import com.atguigu.educms.entity.CrmBanner;
import com.atguigu.educms.mapper.CrmBannerMapper;
import com.atguigu.educms.service.CrmBannerService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 首页banner表 服务实现类
 * </p>
 *
 * @author testjava
 * @since 2022-06-19
 */
@Service
public class CrmBannerServiceImpl extends ServiceImpl<CrmBannerMapper, CrmBanner> implements CrmBannerService {


    //获取首页
    @Override
    @Cacheable(key="'getAllBannerList'", value = "banner") //key需要再加引号
    public List<CrmBanner> getAllBannerList() {

        LambdaQueryWrapper<CrmBanner> queryWrapper = new LambdaQueryWrapper<>();
        //noinspection unchecked
        queryWrapper.orderByDesc(CrmBanner::getSort);
        queryWrapper.last("limit 2");

        List<CrmBanner> bannerList = this.list(queryWrapper);

        return bannerList;
    }
}
