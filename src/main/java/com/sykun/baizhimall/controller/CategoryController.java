package com.sykun.baizhimall.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sykun.baizhimall.entity.CategoryEntity;
import com.sykun.baizhimall.service.CategoryService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 商品三级分类 前端控制器
 * </p>
 *
 * @author sykun
 * @since 2021-07-08
 */
@RestController
@RequestMapping("category")
public class CategoryController {
    @Resource
    private CategoryService categoryService;

    @GetMapping("/list")
    public Map<String, Object> getCategoryTree() {
        HashMap<String, Object> map = new HashMap<>();
        List<CategoryEntity> categoryTree = categoryService.getCategoryTree();
        map.put("data", categoryTree);
        return map;
    }

    @DeleteMapping("/del")
    public Map<String, Object> delCategory(@RequestBody Long[] ids) {
        HashMap<String, Object> map = new HashMap<>();
        boolean b = categoryService.removeByIds(Arrays.asList(ids));
        if (b) {
            map.put("code", 200);
            map.put("msg", "删除成功");
        } else {
            map.put("code", 300);
            map.put("msg", "删除失败");
        }
        return map;
    }

    @PutMapping("/save")
    public Map<String, Object> saveCategory(@RequestBody CategoryEntity categoryEntity) {
        HashMap<String, Object> map = new HashMap<>();
        QueryWrapper<CategoryEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("category_name", categoryEntity.getCategoryName());
        List<CategoryEntity> list = categoryService.list(wrapper);
        if (list.isEmpty()) {
            boolean save = categoryService.save(categoryEntity);
            if (save) {
                map.put("code", 200);
            } else {
                map.put("code", 300);
            }
        }else {
            map.put("code", 300);
        }
        return map;
    }

    @GetMapping("/readyUpdate/{id}")
    public Map<String,Object> readyUpdate(@PathVariable("id") String id){
        HashMap<String, Object> map = new HashMap<>();
        CategoryEntity categoryById = categoryService.getById(id);
        if (categoryById!=null){
            map.put("code",200);
            map.put("data",categoryById);
        }else {
            map.put("code",300);
        }
        return map;
    }
    @PutMapping("/update")
    public Map<String, Object> updateCategory(@RequestBody CategoryEntity categoryEntity) {
        HashMap<String, Object> map = new HashMap<>();
        CategoryEntity categoryOne = categoryService.getOne(new QueryWrapper<CategoryEntity>().eq("category_id", categoryEntity.getCategoryId()));
        if (categoryOne != null){
            boolean b = categoryService.updateById(categoryEntity);
            if (b) {
                map.put("code", 200);
            } else {
                map.put("code", 300);
            }
        }else {
            map.put("code", 300);
        }
        return map;
    }
}

