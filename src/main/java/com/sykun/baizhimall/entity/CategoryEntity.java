package com.sykun.baizhimall.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 商品三级分类
 * </p>
 *
 * @author sykun
 * @since 2021-07-08
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("bz_category")
public class CategoryEntity implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * 分类id
     */
    @TableId(value = "category_id",type = IdType.ID_WORKER_STR)
    private String categoryId;

    /**
     * 分类名称
     */
    @TableField("category_name")
    private String categoryName;

    /**
     * 父分类id
     */
    @TableField("parent_id")
    private String parentId;

    /**
     * 层级
     */
    @TableField("category_level")
    private Integer categoryLevel;

    /**
     * 是否显示[0-不显示，1显示]
     */
    @TableField("show_status")
    @TableLogic
    private Integer showStatus;

    /**
     * 排序
     */
    @TableField("sort")
    private Integer sort;

    /**
     * 图标地址
     */
    @TableField("icon")
    private String icon;

    /**
     * 计量单位
     */
    @TableField("product_unit")
    private String productUnit;

    /**
     * 商品数量
     */
    @TableField("product_count")
    private Integer productCount;

    @TableField(exist = false)
    private List<CategoryEntity> children;


}
