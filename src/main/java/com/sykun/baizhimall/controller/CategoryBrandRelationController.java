package com.sykun.baizhimall.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sykun.baizhimall.entity.CategoryBrandRelationEntity;
import com.sykun.baizhimall.service.CategoryBrandRelationService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 品牌分类关联 前端控制器
 * </p>
 *
 * @author sykun
 * @since 2021-07-11
 */
@RestController
@RequestMapping("/relation")
public class CategoryBrandRelationController {
    @Resource
    private CategoryBrandRelationService relationService;

    @GetMapping("cate/{brandId}")
    public Map<String,Object> getRelation(@PathVariable("brandId") String brandId){
        HashMap<String, Object> map = new HashMap<>();
        List<CategoryBrandRelationEntity> cateList = relationService.list(new QueryWrapper<CategoryBrandRelationEntity>().eq("brand_id", brandId));
        map.put("code",10000);
        map.put("data",cateList);
        return map;
    }
    @DeleteMapping("/{brandId}")
    public Map<String,Object> delRelation(@PathVariable("brandId") String brandId){
        HashMap<String, Object> map = new HashMap<>();
        boolean b = relationService.removeById(brandId);
        if (b){
            map.put("code",10000);
        }else {
            map.put("code",300);
        }
        return map;
    }
}

