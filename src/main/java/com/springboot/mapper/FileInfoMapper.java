package com.springboot.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.springboot.entity.FileInfo;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

@Repository
public interface FileInfoMapper extends BaseMapper<FileInfo> {
    @Select("select * from file_info where file_id=34;")
    FileInfo selectOne();
}
