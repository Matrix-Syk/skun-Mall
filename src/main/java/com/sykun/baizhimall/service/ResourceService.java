package com.sykun.baizhimall.service;

import com.sykun.baizhimall.entity.ResourceEntity;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 菜单管理 服务类
 * </p>
 *
 * @author sykun
 * @since 2021-07-05
 */
public interface ResourceService extends IService<ResourceEntity> {

    List<ResourceEntity> getListWithTree();
}
