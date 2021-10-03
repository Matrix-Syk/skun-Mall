package com.sykun.baizhimall.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.time.LocalDateTime;
import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * <p>
 * 
 * </p>
 *
 * @author sykun
 * @since 2021-07-07
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("bz_log")
public class LogEntity implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "log_id",type = IdType.ID_WORKER_STR)
    private String logId;

    @TableField("log_type")
    private String logType;

    @DateTimeFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    @TableField(value = "log_create_time")
    private Date logCreateTime;

    @TableField("log_create_user")
    private String logCreateUser;

    @TableField("log_ip")
    private String logIp;

    @TableField("log_content")
    private String logContent;

    @TableField("log_params")
    private String logParams;


    @TableField(exist = false)
    private String key;
    @TableField(exist = false)
    private Integer pageNum;
    @TableField(exist = false)
    private Integer pageSize;


}
