package com.sykun.baizhimall.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sykun.baizhimall.entity.AttrAttrgroupRelationEntity;
import com.sykun.baizhimall.entity.AttrEntity;
import com.sykun.baizhimall.entity.CategoryEntity;
import com.sykun.baizhimall.service.AttrAttrgroupRelationService;
import com.sykun.baizhimall.service.AttrGroupService;
import com.sykun.baizhimall.service.AttrService;
import com.sykun.baizhimall.service.CategoryService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.security.acl.Group;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 商品属性 前端控制器
 * </p>
 *
 * @author sykun
 * @since 2021-07-15
 */
@RestController
@RequestMapping("/attr")
public class AttrController {
    @Resource
    private AttrService attrService;
    @Resource
    private AttrAttrgroupRelationService relationService;

    @PostMapping("list")
    public Map<String, Object> showSaleAttr(@RequestBody AttrEntity attrEntity) {
        HashMap<String, Object> map = new HashMap<>();
        QueryWrapper<AttrEntity> wrapper = new QueryWrapper<>();
        // 根据属性类型查询
        if(!StringUtils.isEmpty(attrEntity.getAttrType())){
            wrapper.eq("attr_type", attrEntity.getAttrType());
        }
        if (!StringUtils.isEmpty(attrEntity.getCategoryId())) {
            // 商品分类id(点击分类树)和属性名称(按名查找)存在就加上条件
            wrapper.eq("category_id", attrEntity.getCategoryId());
        }
        if (!StringUtils.isEmpty(attrEntity.getAttrName())) {
            wrapper.like("attr_name", attrEntity.getAttrName());
        }
        IPage<AttrEntity> data = attrService.queryPage(attrEntity, wrapper);
        map.put("code",10000);
        map.put("data", data);
        return map;
    }

    @GetMapping("info/{attrId}")
    public Map<String, Object> changeAttrReady(@PathVariable("attrId") String attrId) {
        HashMap<String, Object> map = new HashMap<>();
        AttrEntity attr = attrService.getById(attrId);
        map.put("data", attr);
        return map;
    }

    @PutMapping("modify")
    public Map<String, Object> changeAttr(@RequestBody AttrEntity attrEntity) {
        HashMap<String, Object> map = new HashMap<>();
        if (attrEntity != null) {
            attrService.updateById(attrEntity);
            AttrAttrgroupRelationEntity relation = relationService.getOne(new QueryWrapper<AttrAttrgroupRelationEntity>().eq("attr_id", attrEntity.getAttrId()));
            if (attrEntity.getAttrType() != 0) {
                if (relation != null) {
                    relationService.update(new UpdateWrapper<AttrAttrgroupRelationEntity>().eq("id", relation.getId()).set("attr_group_id", attrEntity.getAttrGroupId()));
                } else {
                    AttrAttrgroupRelationEntity relationEntity = new AttrAttrgroupRelationEntity();
                    relationEntity.setAttrId(attrEntity.getAttrId());
                    relationEntity.setAttrGroupId(attrEntity.getAttrGroupId());
                    relationService.save(relationEntity);
                }
            }
            map.put("code", 10000);
        } else {
            map.put("code", 0);
        }
        return map;
    }

    @DeleteMapping("remove")
    public Map<String, Object> delAttr(@RequestBody Long[] ids) {
        HashMap<String, Object> map = new HashMap<>();
        boolean b = attrService.removeByIds(Arrays.asList(ids));
        relationService.remove(new QueryWrapper<AttrAttrgroupRelationEntity>().in("attr_id", Arrays.asList(ids)));
        return map;
    }

    /**
     * 保存
     * @param attrEntity
     * @return
     */
    @PostMapping("/save")
    public Map<String, Object> saveAttr(@RequestBody AttrEntity attrEntity) {
        HashMap<String, Object> map = new HashMap<>();
        AttrEntity attrOld = attrService.getOne(new QueryWrapper<AttrEntity>().eq("attr_name", attrEntity.getAttrName()));
        if (attrOld==null){
            attrService.save(attrEntity);
            if (!StringUtils.isEmpty(attrEntity.getAttrGroupId())) {
                AttrAttrgroupRelationEntity relationEntity = new AttrAttrgroupRelationEntity();
                relationEntity.setAttrId(attrEntity.getAttrId());
                relationEntity.setAttrGroupId(attrEntity.getAttrGroupId());
                relationService.save(relationEntity);
            }
            map.put("code",10000);
        }else {
            map.put("code",0);
        }
        return map;
    }
}

