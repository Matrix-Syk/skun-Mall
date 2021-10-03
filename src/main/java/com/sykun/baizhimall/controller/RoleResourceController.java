package com.sykun.baizhimall.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sykun.baizhimall.entity.ResourceEntity;
import com.sykun.baizhimall.entity.RoleEntity;
import com.sykun.baizhimall.entity.RoleResourceEntity;
import com.sykun.baizhimall.service.ResourceService;
import com.sykun.baizhimall.service.RoleResourceService;
import com.sykun.baizhimall.service.RoleService;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author sykun
 * @since 2021-07-17
 */
@RestController
@RequestMapping("/res")
public class RoleResourceController {
    @Resource
    private ResourceService resourceService;
    @GetMapping("/all")
    public Map<String,Object> showRole(){
        HashMap<String, Object> map = new HashMap<>();
        List<ResourceEntity> resourceEntities = resourceService.list();
        Map<String, List<ResourceEntity>> collect = resourceEntities.stream().collect(Collectors.groupingBy(ResourceEntity::getParentId));
        for (ResourceEntity resourceEntity : resourceEntities) {
            for (Map.Entry<String, List<ResourceEntity>> stringListEntry : collect.entrySet()) {
                String key = stringListEntry.getKey();
                if (resourceEntity.getResourceId().equals(key)) {
                    resourceEntity.setChildren(stringListEntry.getValue());
                }
            }
        }
        List<ResourceEntity> list = resourceEntities.stream().filter(resource ->
            resource.getType() == 0
        ).collect(Collectors.toList());
        map.put("code",10000);
        map.put("data",list);
        return map;
    }
}

