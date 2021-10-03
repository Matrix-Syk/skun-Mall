package com.sykun.baizhimall.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sykun.baizhimall.entity.AdminEntity;
import com.sykun.baizhimall.entity.AdminRoleEntity;
import com.sykun.baizhimall.service.AdminRoleService;
import com.sykun.baizhimall.service.AdminService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author sykun
 * @since 2021-07-05
 */
@RestController
@RequestMapping("/admin-entity")
public class AdminController {
    @Resource
    private AdminService adminService;
    @Resource
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Resource
    private AdminRoleService adminRoleService;

    /**
     * 分页展示用户
     *
     * @param adminEntity 用户实体 封装有页码和页码显示条数
     */
    @PreAuthorize("hasAnyAuthority('sys:user:list')")
    @PostMapping
    public Map<String, Object> showUserPage(@RequestBody AdminEntity adminEntity) {
        HashMap<String, Object> map = new HashMap<>();
        Page<AdminEntity> page = new Page<>(adminEntity.getPageNum(), adminEntity.getPageSize());
        if (adminEntity.getUsername() != null && !"".equals(adminEntity.getUsername())) {
            IPage<AdminEntity> pages = adminService.page(page, new QueryWrapper<AdminEntity>().like("username", adminEntity.getUsername()));
            map.put("data", pages);
        } else {
            IPage<AdminEntity> pages = adminService.page(page, null);
            map.put("data", pages);
        }
        return map;
    }

    @GetMapping("/info/{id}")
    public Map<String, Object> changeUser(@PathVariable("id") String id) {
        HashMap<String, Object> map = new HashMap<>();
        if (id != null) {
            AdminEntity admin = adminService.getById(id);
            List<AdminRoleEntity> adminRoleList = adminRoleService.list(new QueryWrapper<AdminRoleEntity>().eq("admin_id", id));
            List<String> roleIds = adminRoleList.stream().map(AdminRoleEntity::getRoleId).collect(Collectors.toList());
            admin.setRoleIds(roleIds);
            map.put("data", admin);
        } else {
            map.put("data", "查询失败");
        }
        return map;
    }

    @DeleteMapping("/del")
    public Map<String, Object> delUser(@RequestBody Long[] ids) {
        HashMap<String, Object> map = new HashMap<>();
        boolean b = adminService.removeByIds(Arrays.asList(ids));
        for (Long id : ids) {
            adminRoleService.remove(new QueryWrapper<AdminRoleEntity>().eq("admin_id", id));
        }
        if (b) {
            map.put("data", "删除成功");
            map.put("msg", "success");
        } else {
            map.put("data", "删除失败");
            map.put("msg", "error");
        }
        return map;
    }

    @PostMapping("/update")
    public Map<String, Object> updateUser(@RequestBody AdminEntity adminEntity) {
        HashMap<String, Object> map = new HashMap<>();
        AdminEntity old = adminService.getById(adminEntity.getId());
        if (!old.getPassword().equals(adminEntity.getPassword())) {
            String encode = bCryptPasswordEncoder.encode(adminEntity.getPassword());
            adminEntity.setPassword(encode);
        }
        boolean b = adminService.updateById(adminEntity);
        changeMiddle(adminEntity);
        if (b) {
            map.put("data", "更新成功");
            map.put("msg", "success");
        } else {
            map.put("data", "更新失败");
            map.put("msg", "error");
        }
        return map;
    }

    @PostMapping("/save")
    public Map<String, Object> saveUser(@RequestBody AdminEntity adminEntity) {
        HashMap<String, Object> map = new HashMap<>();
        boolean flag = false;
        AdminEntity userOne = adminService.getOne(new QueryWrapper<AdminEntity>().eq("username", adminEntity.getUsername()));
        if (userOne == null) {
            String encode = bCryptPasswordEncoder.encode(adminEntity.getPassword());
            adminEntity.setPassword(encode);
            flag = adminService.saveUser(adminEntity);
            changeMiddle(adminEntity);
        }
        if (flag) {
            map.put("data", "添加成功");
            map.put("msg", "success");
        } else {
            map.put("data", "添加失败,用户名已存在");
            map.put("msg", "error");
        }
        return map;
    }

    /**
     * 添加或更新时向中间表中添加数据
     */
    public void changeMiddle(AdminEntity adminEntity) {
        String id = adminEntity.getId();
        adminRoleService.remove(new QueryWrapper<AdminRoleEntity>().eq("admin_id", id));
        List<AdminRoleEntity> list = new ArrayList<>();
        for (String roleId : adminEntity.getRoleIds()) {
            AdminRoleEntity adminRoleEntity = new AdminRoleEntity();
            adminRoleEntity.setAdminId(id);
            adminRoleEntity.setRoleId(roleId);
            list.add(adminRoleEntity);
        }
        adminRoleService.saveBatch(list);
    }
}

