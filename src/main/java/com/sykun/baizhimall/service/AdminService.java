package com.sykun.baizhimall.service;

import com.sykun.baizhimall.entity.AdminEntity;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author sykun
 * @since 2021-07-05
 */
public interface AdminService extends IService<AdminEntity> {
    Boolean saveUser(AdminEntity adminEntity);
}
