package com.springboot.entity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("file_info")
public class FileInfo {
    @TableId(type = IdType.AUTO)
    private Integer file_id;
    private String file_name;
    private Integer file_type;
    private Integer file_size;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "Asia/Shanghai" )
    private Timestamp update_time;
    private Integer user_id;
    private Integer file_pid;
    private Integer recycled;
    private String file_md5;
    private String file_path;
    private Integer is_folder;
}
