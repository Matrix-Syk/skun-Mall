package com.sykun.baizhimall.entity;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.baomidou.mybatisplus.annotation.*;

import java.beans.ConstructorProperties;
import java.io.Serializable;
import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.apache.ibatis.annotations.ConstructorArgs;

/**
 * <p>
 * 
 * </p>
 *
 * @author sykun
 * @since 2021-07-09
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("bz_brand")
public class BrandEntity implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * 品牌id
     */
    @TableId(value = "brand_id",type = IdType.ID_WORKER_STR)
    private String brandId;

    /**
     * 名称
     */
    @Excel(name = "品牌名称")
    @TableField("name")
    private String name;

    /**
     * 首字母
     */
    @Excel(name = "首字母")
    @TableField("first_letter")
    private String firstLetter;

    /**
     * 排序
     */
    @Excel(name = "排序")
    @TableField("sort")
    private Integer sort;

    /**
     * 是否显示[0-不显示；1-显示]
     */
    @Excel(name = "显示状态",replace = {"显示_1","不显示_0"})
    @TableField("show_status")
    private Integer showStatus;

    /**
     * 品牌logo
     */
    @Excel(name = "logo")
    @TableField("logo")
    private String logo;

    /**
     * 品牌描述
     */
    @Excel(name = "品牌描述")
    @TableField("brand_story")
    private String brandStory;


    /**
     * 逻辑删除
     */
    @TableField("deleted")
    @TableLogic
    private Integer deleted;



    @TableField(exist = false)
    private Integer pageNum;

    @TableField(exist = false)
    private Integer pageSize;

    @TableField(exist = false)
    private List<CategoryEntity> categoryList;

}
