package com.sykun.baizhimall.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sykun.baizhimall.entity.AttrEntity;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 商品属性 服务类
 * </p>
 *
 * @author sykun
 * @since 2021-07-15
 */
public interface AttrService extends IService<AttrEntity> {

    IPage<AttrEntity> queryPage(AttrEntity attrEntity, QueryWrapper<AttrEntity> wrapper);

}
