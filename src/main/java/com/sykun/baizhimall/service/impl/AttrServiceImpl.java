package com.sykun.baizhimall.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sykun.baizhimall.dao.AttrAttrgroupRelationDao;
import com.sykun.baizhimall.dao.AttrDao;
import com.sykun.baizhimall.dao.AttrGroupDao;
import com.sykun.baizhimall.dao.CategoryDao;
import com.sykun.baizhimall.entity.AttrAttrgroupRelationEntity;
import com.sykun.baizhimall.entity.AttrEntity;
import com.sykun.baizhimall.entity.AttrGroupEntity;
import com.sykun.baizhimall.service.AttrService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * <p>
 * 商品属性 服务实现类
 * </p>
 *
 * @author sykun
 * @since 2021-07-15
 */
@Service
public class AttrServiceImpl extends ServiceImpl<AttrDao, AttrEntity> implements AttrService {
    @Resource
    private CategoryDao categoryDao;
    @Resource
    private AttrAttrgroupRelationDao relationDao;
    @Resource
    private AttrGroupDao groupDao;

    @Override
    public IPage<AttrEntity> queryPage(AttrEntity attrEntity, QueryWrapper<AttrEntity> wrapper) {
        Page<AttrEntity> page = new Page<>(attrEntity.getPageNum(), attrEntity.getPageSize());
        IPage<AttrEntity> data = baseMapper.selectPage(page, wrapper);
        // 为属性添加上所属分类和所属分组的名字
        for (AttrEntity record : data.getRecords()) {
            String categoryName = categoryDao.selectById(record.getCategoryId().split(",")[2]).getCategoryName();
            record.setCategoryName(categoryName);
        }
        if (attrEntity.getAttrType() == 1) {
            for (AttrEntity entity : data.getRecords()) {
                AttrAttrgroupRelationEntity relation = relationDao.selectOne(new QueryWrapper<AttrAttrgroupRelationEntity>().eq("attr_id", entity.getAttrId()));
                if (relation != null) {
                    AttrGroupEntity group = groupDao.selectById(relation.getAttrGroupId());
                    if (group != null) {
                        entity.setAttrGroupName(group.getAttrGroupName());
                    }
                }
            }
        }
        if (!StringUtils.isEmpty(attrEntity.getAttrGroupId())) {
            // 遍历查询出的非销售属性
            List<AttrEntity> list = new ArrayList<>();
            for (AttrEntity entity : data.getRecords()) {
                // 根据属性id查询该属性是否有关联了分组
                AttrAttrgroupRelationEntity relation = relationDao.selectOne(new QueryWrapper<AttrAttrgroupRelationEntity>().eq("attr_id", entity.getAttrId()));
                if (relation != null) {
                    list.add(entity);
                }
            }
            if (list.size() > 0) {
                for (AttrEntity entity : list) {
                    data.getRecords().remove(entity);
                }
            }
        }
        return data;
    }
}