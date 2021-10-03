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
@TableName("bz_file")
public class FileEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "file_id", type = IdType.ID_WORKER_STR)
    private String fileId;

    @TableField("file_url")
    private String fileUrl;

    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    @TableField(value = "file_create_time", fill = FieldFill.INSERT)
    private Date fileCreateTime;

    @TableField("file_create_user")
    private String fileCreateUser;

    @TableField("file_directory")
    private String fileDirectory;

    @TableField("file_old_name")
    private String fileOldName;

    @TableField("file_new_name")
    private String fileNewName;


}
