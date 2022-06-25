package com.atguigu.educenter.mapper;

import com.atguigu.educenter.entity.UcenterMember;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.lettuce.core.dynamic.annotation.Param;

/**
 * <p>
 * 会员表 Mapper 接口
 * </p>
 *
 * @author LuoKai
 * @since 2022-06-20
 */
public interface UcenterMemberMapper extends BaseMapper<UcenterMember> {

    /**
     * 统计某一天的注册人数
     * @param day 某一天
     * @return 注册总人数
     */
    Integer selectRegisterCount(@Param("oneDay") String day);
}
