package com.springboot.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.springboot.entity.ChunkInfo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
@Repository
public interface ChunkInfoMapper extends BaseMapper<ChunkInfo> {

    @Select("select CHUNK_NUMBER from CHUNK_INFO where IDENTIFIER = #{identifier} and FILE_NAME = #{file_name}")
    ArrayList<Integer> selectChunkNumbers(@Param("identifier")String identifier, @Param("file_name")String file_name);
}
