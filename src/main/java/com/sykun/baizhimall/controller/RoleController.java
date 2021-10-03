package com.sykun.baizhimall.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sykun.baizhimall.entity.AdminEntity;
import com.sykun.baizhimall.entity.RoleEntity;
import com.sykun.baizhimall.entity.RoleResourceEntity;
import com.sykun.baizhimall.service.RoleResourceService;
import com.sykun.baizhimall.service.RoleService;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author sykun
 * @since 2021-07-17
 */
@RestController
@RequestMapping("/role")
public class RoleController {
    @Resource
    private RoleService roleService;
    @Resource
    private RoleResourceService roleResourceService;

    @PostMapping("/list")
    public Map<String, Object> showRole(@RequestBody RoleEntity role) {
        HashMap<String, Object> map = new HashMap<>();
        IPage<RoleEntity> iPage = new Page<>(role.getPageNum(), role.getPageSize());
        QueryWrapper<RoleEntity> wrapper = new QueryWrapper<>();
        if (!StringUtils.isEmpty(role.getRoleName())) {
            wrapper.eq("role_name", role.getRoleName());
        }
        IPage<RoleEntity> page = roleService.page(iPage, wrapper);
        map.put("code", 10000);
        map.put("data", page);
        return map;
    }
    @GetMapping("/list")
    public Map<String, Object> allRole() {
        HashMap<String, Object> map = new HashMap<>();
        List<RoleEntity> list = roleService.list();
        map.put("code", 10000);
        map.put("data", list);
        return map;
    }

    @GetMapping("info/{roleId}")
    public Map<String, Object> redyUpdate(@PathVariable String roleId) {
        HashMap<String, Object> map = new HashMap<>();
        RoleEntity role = roleService.getById(roleId);
        List<RoleResourceEntity> checkedKeys = roleResourceService.list(new QueryWrapper<RoleResourceEntity>().eq("role_id", roleId));
        List<String> collect = checkedKeys.stream().map(roleRes -> {
            return roleRes.getResourceId();
        }).collect(Collectors.toList());
        role.setResourceIds(collect);
        map.put("data", role);
        return map;
    }

    @PostMapping("/update")
    public Map<String, Object> updateUser(@RequestBody RoleEntity roleEntity) {
        HashMap<String, Object> map = new HashMap<>();
        RoleEntity old = roleService.getById(roleEntity.getRoleId());
        if (old != null) {
            this.changeMiddle(roleEntity);
        }
        boolean b = roleService.updateById(roleEntity);
        if (b) {
            map.put("code", 10000);
            map.put("data", "更新成功");
            map.put("msg", "success");
        } else {
            map.put("code", 0);
            map.put("data", "更新失败");
            map.put("msg", "error");
        }
        return map;
    }

    @PostMapping("/save")
    public Map<String, Object> saveUser(@RequestBody RoleEntity roleEntity) {
        HashMap<String, Object> map = new HashMap<>();
        boolean flag = false;
        RoleEntity old = roleService.getOne(new QueryWrapper<RoleEntity>().eq("role_name", roleEntity.getRoleName()));
        if (old == null) {
            // 向中间表添加数据
            changeMiddle(roleEntity);
            flag = roleService.save(roleEntity);
        }
        if (flag) {
            map.put("code", 10000);
            map.put("data", "添加成功");
            map.put("msg", "success");
        } else {
            map.put("code", 0);
            map.put("data", "添加失败,角色名存在");
            map.put("msg", "error");
        }
        return map;
    }

    /**
     * 添加或更新时向中间表中添加数据
     * @param roleEntity 角色对象
     */
    public void changeMiddle(RoleEntity roleEntity){
        roleResourceService.remove(new QueryWrapper<RoleResourceEntity>().eq("role_id", roleEntity.getRoleId()));
        List<RoleResourceEntity> list = new ArrayList<>();
        for (String resourceId : roleEntity.getResourceIds()) {
            RoleResourceEntity entity = new RoleResourceEntity();
            entity.setRoleId(roleEntity.getRoleId());
            entity.setResourceId(resourceId);
            list.add(entity);
        }
        roleResourceService.saveBatch(list);
    }
}

