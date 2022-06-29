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
 * @author luokai
 * @since 2022-06-19
 */
@Service
public class CrmBannerServiceImpl extends ServiceImpl<CrmBannerMapper, CrmBanner> implements CrmBannerService {

    /**
     * 获取首页
     * key需要再加引号 @Cacheable
     * @return 幻灯片列表
     */
    @Override
    @Cacheable(key="'getAllBannerList'", value = "banner")
    public List<CrmBanner> getAllBannerList() {

        LambdaQueryWrapper<CrmBanner> queryWrapper = new LambdaQueryWrapper<>();
        //noinspection unchecked
        queryWrapper.orderByDesc(CrmBanner::getSort);
        queryWrapper.last("limit 5");

        List<CrmBanner> bannerList = this.list(queryWrapper);

        return bannerList;
    }
}
