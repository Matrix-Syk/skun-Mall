package com.sykun.baizhimall.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sykun.baizhimall.entity.AttrAttrgroupRelationEntity;
import com.sykun.baizhimall.entity.AttrGroupEntity;
import com.sykun.baizhimall.entity.CategoryEntity;
import com.sykun.baizhimall.service.AttrAttrgroupRelationService;
import com.sykun.baizhimall.service.AttrGroupService;
import com.sykun.baizhimall.service.AttrService;
import com.sykun.baizhimall.service.CategoryService;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.*;

/**
 * <p>
 * 属性分组 前端控制器
 * </p>
 *
 * @author sykun
 * @since 2021-07-15
 */
@RestController
@RequestMapping("/attr/group")
public class AttrGroupController {
    @Resource
    private CategoryService categoryService;
    @Resource
    private AttrGroupService groupService;
    @Resource
    private AttrAttrgroupRelationService relationService;
    @Resource
    private AttrService attrService;

    @PostMapping("/list")
    public Map<String,Object> groupPage(@RequestBody AttrGroupEntity groupEntity){
        HashMap<String, Object> map = new HashMap<>();
        IPage<AttrGroupEntity> groupEntityPage = new Page<>(groupEntity.getPageNum(), groupEntity.getPageSize());
        QueryWrapper<AttrGroupEntity> wrapper = new QueryWrapper<>();
        if (!StringUtils.isEmpty(groupEntity.getCategoryId())){
            wrapper.eq("category_id",groupEntity.getCategoryId());
        }else if (!StringUtils.isEmpty(groupEntity.getAttrGroupName())){
            wrapper.like("attr_group_name",groupEntity.getAttrGroupName());
        }
        IPage<AttrGroupEntity> page = groupService.page(groupEntityPage, wrapper);
        for (AttrGroupEntity record : page.getRecords()) {
            CategoryEntity categoryEntity = categoryService.getById(record.getCategoryId().split(",")[2]);
            if (categoryEntity!=null){
                record.setCategoryName(categoryEntity.getCategoryName());
            }
        }
        map.put("data",page);
        return map;
    }
    @GetMapping("/info/{groupId}")
    public Map<String,Object> getGroupById(@PathVariable("groupId")String attrGroupId){
        HashMap<String, Object> map = new HashMap<>();
        AttrGroupEntity byId = groupService.getById(attrGroupId);
        map.put("data",byId);
        return map;
    }
    @PutMapping("modify")
    public Map<String,Object> updateGroup(@RequestBody AttrGroupEntity attrGroupEntity){
        HashMap<String, Object> map = new HashMap<>();
        boolean b = groupService.updateById(attrGroupEntity);
        if (b){
            map.put("code",10000);
        }else {
            map.put("code",0);
        }
        return map;
    }
    @PostMapping("save")
    public Map<String,Object> savaGroup(@RequestBody AttrGroupEntity attrGroupEntity){
        HashMap<String, Object> map = new HashMap<>();
        QueryWrapper<AttrGroupEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("attr_group_name",attrGroupEntity.getAttrGroupName());
        wrapper.eq("category_id",attrGroupEntity.getCategoryId());
        List<AttrGroupEntity> list = groupService.list(wrapper);
        if (list.size()>0){
            map.put("code",0);
        }else {
            groupService.save(attrGroupEntity);
            map.put("code",10000);
        }
        return map;
    }
    @DeleteMapping("remove")
    public Map<String,Object> delGroup(@RequestBody Long[] ids){
        HashMap<String, Object> map = new HashMap<>();
        groupService.removeByIds(Arrays.asList(ids));
        for (Long id : ids) {
            List<AttrAttrgroupRelationEntity> relationList = relationService.list(new QueryWrapper<AttrAttrgroupRelationEntity>().eq("attr_group_id", id));
            if (relationList.size()>0){
                for (AttrAttrgroupRelationEntity relationEntity : relationList) {
                    attrService.removeById(relationEntity.getAttrId());
                }
            }
        }
        map.put("code",10000);
        return map;
    }
}

