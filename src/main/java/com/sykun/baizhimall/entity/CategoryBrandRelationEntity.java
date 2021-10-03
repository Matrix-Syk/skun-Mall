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
 * 品牌分类关联
 * </p>
 *
 * @author sykun
 * @since 2021-07-11
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("bz_category_brand_relation")
public class CategoryBrandRelationEntity implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "id",type = IdType.ID_WORKER_STR)
    private String id;

    /**
     * 品牌id
     */
    @TableField("brand_id")
    private String brandId;

    /**
     * 分类id
     */
    @TableField("category_id")
    private String categoryId;

    @TableField("brand_name")
    private String brandName;

    @TableField("category_name")
    private String categoryName;


}
