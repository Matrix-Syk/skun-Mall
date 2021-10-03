package com.sykun.baizhimall.service.impl;

import com.sykun.baizhimall.entity.AdminEntity;
import com.sykun.baizhimall.dao.AdminDao;
import com.sykun.baizhimall.log.LogAnnotation;
import com.sykun.baizhimall.log.TypeEnum;
import com.sykun.baizhimall.service.AdminService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author sykun
 * @since 2021-07-05
 */
@Service
public class AdminServiceImpl extends ServiceImpl<AdminDao, AdminEntity> implements AdminService {

    @Override
    @LogAnnotation(type = TypeEnum.INSERT,content = "注册")
    public Boolean saveUser(AdminEntity adminEntity) {
        int insert = baseMapper.insert(adminEntity);
        return insert > 0;
    }
}
