package com.sykun.baizhimall.service;

import com.sykun.baizhimall.entity.CategoryEntity;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 商品三级分类 服务类
 * </p>
 *
 * @author sykun
 * @since 2021-07-08
 */
public interface CategoryService extends IService<CategoryEntity> {

    List<CategoryEntity> getCategoryTree();
}
