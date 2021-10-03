package com.sykun.baizhimall.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author sykun
 * @since 2021-07-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("bz_role_resource")
public class RoleResourceEntity implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "id",type = IdType.ID_WORKER_STR)
    private String id;

    @TableField("role_id")
    private String roleId;

    @TableField("resource_id")
    private String resourceId;


    @TableField(exist = false)
    private Integer pageNum;
    @TableField(exist = false)
    private Integer pageSize;

}
