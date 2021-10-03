package com.sykun.baizhimall.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sykun.baizhimall.dao.AdminDao;
import com.sykun.baizhimall.dao.AdminRoleDao;
import com.sykun.baizhimall.dao.RoleResourceDao;
import com.sykun.baizhimall.entity.AdminEntity;
import com.sykun.baizhimall.entity.AdminRoleEntity;
import com.sykun.baizhimall.entity.ResourceEntity;
import com.sykun.baizhimall.dao.ResourceDao;
import com.sykun.baizhimall.entity.RoleResourceEntity;
import com.sykun.baizhimall.service.AdminService;
import com.sykun.baizhimall.service.ResourceService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 菜单管理 服务实现类
 * </p>
 *
 * @author sykun
 * @since 2021-07-05
 */
@Service
public class ResourceServiceImpl extends ServiceImpl<ResourceDao, ResourceEntity> implements ResourceService {
    @Resource
    private AdminDao adminDao;
    @Resource
    private AdminRoleDao adminRoleDao;
    @Resource
    private RoleResourceDao roleResourceDao;

    @Override
    public List<ResourceEntity> getListWithTree() {
        // 对应权限的用户只显示相应菜单
        // 获取认证的用户
        Authentication user = SecurityContextHolder.getContext().getAuthentication();
        // 从库中查询用户以获取adminId
        AdminEntity adminEntity = adminDao.selectOne(new QueryWrapper<AdminEntity>().eq("username", user.getName()));
        // 根据用户id查询中间表获取角色信息并通过流返回对应角色的id
        List<String> roleIds = adminRoleDao.selectList(new QueryWrapper<AdminRoleEntity>().eq("admin_id", adminEntity.getId()))
                .stream().map(AdminRoleEntity::getRoleId).collect(Collectors.toList());
        // 遍历角色id查询中间表获取菜单的id
        List<String> resourceIds = new ArrayList<>();
        for (String roleId : roleIds) {
            List<String> ids = roleResourceDao.selectList(new QueryWrapper<RoleResourceEntity>().eq("role_id", roleId))
                    .stream().map(RoleResourceEntity::getResourceId).collect(Collectors.toList());
            resourceIds.addAll(ids);
        }
        List<ResourceEntity> listWithTree = new ArrayList<>();
        if (resourceIds.size() > 0) {
            List<ResourceEntity> resourceEntities = baseMapper.selectBatchIds(resourceIds);
            Map<String, List<ResourceEntity>> collect = resourceEntities.stream().collect(Collectors.groupingBy(ResourceEntity::getParentId));
            for (ResourceEntity resourceEntity : resourceEntities) {
                for (Map.Entry<String, List<ResourceEntity>> stringListEntry : collect.entrySet()) {
                    String key = stringListEntry.getKey();
                    if (resourceEntity.getResourceId().equals(key) && stringListEntry.getValue().get(0).getType() < 2) {
                        resourceEntity.setChildren(stringListEntry.getValue());
                    }
                }
            }
            listWithTree = collect.get("0");
        }
        return listWithTree;
    }

}
