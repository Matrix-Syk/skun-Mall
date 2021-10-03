package com.sykun.baizhimall.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sykun.baizhimall.entity.LogEntity;
import com.sykun.baizhimall.service.LogService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author sykun
 * @since 2021-07-07
 */
@RestController
@RequestMapping("/log")
public class LogController {
    @Resource
    private LogService logService;

    /**
     * 查询入库的日志
     * @param logEntity
     * @return
     */
    @PostMapping("list")
    public Map<String,Object> getLog(@RequestBody LogEntity logEntity){
        HashMap<String, Object> map = new HashMap<>();
        Page<LogEntity> logEntityPage = new Page<>(logEntity.getPageNum(), logEntity.getPageSize());
        QueryWrapper<LogEntity> wrapper = new QueryWrapper<>();
        IPage<LogEntity> page = null;
        if (logEntity.getKey() != null&&!"".equals(logEntity.getKey())){
            wrapper.like("log_create_user",logEntity.getKey());
            wrapper.or();
            wrapper.like("log_type",logEntity.getKey());
            page = logService.page(logEntityPage,wrapper);
        }else {
            page = logService.page(logEntityPage);
        }
        map.put("data",page);
        map.put("code",200);
        return map;
    }

}

