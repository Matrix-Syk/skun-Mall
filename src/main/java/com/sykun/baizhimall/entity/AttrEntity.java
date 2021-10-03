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
 * 商品属性
 * </p>
 *
 * @author sykun
 * @since 2021-07-15
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("bz_attr")
public class AttrEntity implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * 属性id
     */
    @TableId(value = "attr_id",type = IdType.ID_WORKER_STR)
    private String attrId;

    /**
     * 属性名
     */
    @TableField("attr_name")
    private String attrName;

    /**
     * 属性图标
     */
    @TableField("icon")
    private String icon;

    /**
     * 可选值列表[用逗号分隔]
     */
    @TableField("value_select")
    private String valueSelect;

    /**
     * 属性类型[0-销售属性，1-基本属性，2-既是销售属性又是基本属性]
     */
    @TableField("attr_type")
    private Integer attrType;

    /**
     * 启用状态[0 - 禁用，1 - 启用]
     */
    @TableField("enable")
    private Integer enable;

    /**
     * 所属分类
     */
    @TableField("category_id")
    private String categoryId;

    /**
     * 是否多选
     */
    @TableField("value_type")
    private String valueType;

    @TableField(exist = false)
    private Integer pageNum;

    @TableField(exist = false)
    private Integer pageSize;

    @TableField(exist = false)
    private String categoryName;

    @TableField(exist = false)
    private String attrGroupName;

    @TableField(exist = false)
    private String attrGroupId;

}
