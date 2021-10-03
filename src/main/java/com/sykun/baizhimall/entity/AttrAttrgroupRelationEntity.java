package com.sykun.baizhimall.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 属性&属性分组关联
 * </p>
 *
 * @author sykun
 * @since 2021-07-15
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("bz_attr_attrgroup_relation")
public class AttrAttrgroupRelationEntity implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * id
     */
    @TableId(value = "id",type = IdType.ID_WORKER_STR)
    private String id;

    /**
     * 属性id
     */
    @TableField("attr_id")
    private String attrId;

    /**
     * 属性分组id
     */
    @TableField("attr_group_id")
    private String attrGroupId;

    @TableField(exist = false)
    private String attrName;
    @TableField(exist = false)
    private String valueSelect;
}
