package com.sykun.baizhimall.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sykun.baizhimall.entity.AttrAttrgroupRelationEntity;
import com.sykun.baizhimall.entity.AttrEntity;
import com.sykun.baizhimall.service.AttrAttrgroupRelationService;
import com.sykun.baizhimall.service.AttrService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 属性&属性分组关联 前端控制器
 * </p>
 *
 * @author sykun
 * @since 2021-07-15
 */
@RestController
@RequestMapping("/attr/relation")
public class AttrRelationController {
    @Resource
    private AttrAttrgroupRelationService relationService;
    @Resource
    private AttrService attrService;

    @GetMapping("/{attrGroupId}")
    public Map<String, Object> getRelation(@PathVariable String attrGroupId) {
        HashMap<String, Object> map = new HashMap<>();
        List<AttrAttrgroupRelationEntity> list = relationService.list(new QueryWrapper<AttrAttrgroupRelationEntity>().eq("attr_group_id", attrGroupId));
        List<AttrAttrgroupRelationEntity> collect = list.stream().peek(relation -> {
            AttrEntity attrEntity = attrService.getById(relation.getAttrId());
            if (attrEntity != null) {
                relation.setAttrName(attrEntity.getAttrName());
                relation.setValueSelect(attrEntity.getValueSelect());
            }
        }).collect(Collectors.toList());
        map.put("data", collect);
        return map;
    }

    @PostMapping("/save")
    public Map<String, Object> savRelation(@RequestBody List<AttrAttrgroupRelationEntity> relations) {
        HashMap<String, Object> map = new HashMap<>();
        for (AttrAttrgroupRelationEntity relation : relations) {
            AttrAttrgroupRelationEntity oldRelation = relationService.getOne(new QueryWrapper<AttrAttrgroupRelationEntity>().eq("attr_id", relation.getAttrId()));
            if (oldRelation == null) {
                relationService.save(relation);
            }
        }
        map.put("code", 10000);
        return map;
    }
}

