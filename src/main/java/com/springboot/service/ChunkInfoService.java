package com.springboot.service;

import com.springboot.entity.ChunkInfo;
import com.springboot.entity.ChunkResult;

import javax.servlet.http.HttpServletResponse;

public interface ChunkInfoService {
    /**
     * 校验当前文件
     *
     * @param chunkInfo
     * @return 秒传？续传？新传？
     */
    ChunkResult checkChunkState(ChunkInfo chunkInfo);

    /**
     * 上传文件
     *
     * @param chunk
     * @return
     */
    Integer uploadFile(ChunkInfo chunk);
}
