package com.sykun.baizhimall.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author sykun
 * @since 2021-07-17
 */
@Getter
@Setter
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("bz_admin_role")
public class AdminRoleEntity implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "id",type = IdType.ID_WORKER_STR)
    private String id;

    @TableField("admin_id")
    private String adminId;

    @TableField("role_id")
    private String roleId;

    @TableField(exist = false)
    private Integer pageNum;
    @TableField(exist = false)
    private Integer pageSize;
}
