package com.atguigu.educms.service;

import com.atguigu.educms.entity.CrmBanner;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 首页banner表 服务类
 * </p>
 *
 * @author luokai
 * @since 2022-06-19
 */
public interface CrmBannerService extends IService<CrmBanner> {

    /**
     * 获取首页幻灯片
     * @return 幻灯片列表
     */
    List<CrmBanner> getAllBannerList();

}
