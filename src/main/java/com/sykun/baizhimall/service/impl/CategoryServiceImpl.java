package com.sykun.baizhimall.service.impl;

import com.sykun.baizhimall.entity.CategoryEntity;
import com.sykun.baizhimall.dao.CategoryDao;
import com.sykun.baizhimall.service.CategoryService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 商品三级分类 服务实现类
 * </p>
 *
 * @author sykun
 * @since 2021-07-08
 */
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryDao, CategoryEntity> implements CategoryService {

    @Override
    public List<CategoryEntity> getCategoryTree() {
        List<CategoryEntity> categoryEntities = baseMapper.selectList(null);
        Map<String, List<CategoryEntity>> collect = categoryEntities.stream().collect(Collectors.groupingBy(CategoryEntity::getParentId));
        for (CategoryEntity categoryEntity : categoryEntities) {
            for (Map.Entry<String, List<CategoryEntity>> stringListEntry : collect.entrySet()) {
                String key = stringListEntry.getKey();
                if (key.equals(categoryEntity.getCategoryId())){
                    categoryEntity.setChildren(stringListEntry.getValue());
                }
            }
        }
        return collect.get("0");
    }
}
