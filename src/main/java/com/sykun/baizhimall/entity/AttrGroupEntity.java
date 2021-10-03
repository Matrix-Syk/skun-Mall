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
 * 属性分组
 * </p>
 *
 * @author sykun
 * @since 2021-07-15
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("bz_attr_group")
public class AttrGroupEntity implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * 分组id
     */
    @TableId(value = "attr_group_id",type = IdType.ID_WORKER_STR)
    private String attrGroupId;

    /**
     * 组名
     */
    @TableField("attr_group_name")
    private String attrGroupName;

    /**
     * 排序
     */
    @TableField("sort")
    private Integer sort;

    /**
     * 描述
     */
    @TableField("descript")
    private String descript;

    /**
     * 组图标
     */
    @TableField("icon")
    private String icon;

    /**
     * 所属分类id
     */
    @TableField("category_id")
    private String categoryId;

    @TableField(exist = false)
    private Integer pageNum;

    @TableField(exist = false)
    private Integer pageSize;

    @TableField(exist = false)
    private String categoryName;
}
