package com.springboot.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("chunk_info")
public class ChunkInfo {
    @TableId(type = IdType.AUTO, value = "chunk_id")
    private Integer id;
    @TableField(value = "chunk_number")
    private Integer chunkNumber;
    @TableField(value = "chunk_size")
    private Integer chunkSize;
    @TableField(value = "current_chunk_size")
    private Integer currentChunkSize;
    @TableField(value = "identifier")
    private String identifier;
    @TableField(value = "file_name")
    private String filename;
    @TableField(value = "relative_path")
    private String relativePath;
    @TableField(value = "total_chunks")
    private Integer totalChunks;
    @TableField(value = "total_size")
    private Integer totalSize;
    @TableField(value = "file_type")
    private String filetype;
    @TableField(value = "user_id")
    private Integer userId;
    @TableField(value = "file_pid")
    private Integer filePid;

    @TableField(exist = false)
    // 块内容
    private MultipartFile upfile;
}
