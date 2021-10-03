package com.sykun.baizhimall.controller;


import com.sykun.baizhimall.entity.ResourceEntity;
import com.sykun.baizhimall.service.ResourceService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 菜单管理 前端控制器
 * </p>
 *
 * @author sykun
 * @since 2021-07-05
 */
@RestController
@RequestMapping("/resource-entity")
public class ResourceController {
    @Resource
    private ResourceService resourceService;

    @GetMapping("/list")
    public Map<String,Object> getMenus(){
        HashMap<String, Object> map = new HashMap<>();
        List<ResourceEntity> listWithTree = resourceService.getListWithTree();
        if (listWithTree.size()!=0){
            map.put("code",10000);
            map.put("status","success");
            map.put("data",listWithTree);
        }else {
            map.put("code",10012);
            map.put("status","您没有任何权限,请联系管理员");
        }
        return map;
    }
}

