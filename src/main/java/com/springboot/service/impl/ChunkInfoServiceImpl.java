package com.springboot.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.springboot.entity.ChunkInfo;
import com.springboot.entity.ChunkResult;
import com.springboot.entity.ReturnCode;
import com.springboot.mapper.ChunkInfoMapper;
import com.springboot.service.ChunkInfoService;
import com.springboot.utils.FileInfoUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import lombok.extern.slf4j.Slf4j;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

@Slf4j
@Service
public class ChunkInfoServiceImpl extends ServiceImpl<ChunkInfoMapper, ChunkInfo> implements ChunkInfoService {
    @Value("${base.file-path}")
    private String uploadFolder;
    @Autowired
    private ChunkInfoMapper chunkInfoMapper;

    /**
     * 校验当前文件
     * @param chunkInfo
     * @return 秒传？续传？新传？
     */
    @Override
    public ChunkResult checkChunkState(ChunkInfo chunkInfo) {
        ChunkResult chunkResult = new ChunkResult();
        String file = uploadFolder + File.separator + chunkInfo.getIdentifier() + File.separator + chunkInfo.getFilename();
        if(FileInfoUtil.fileExists(file)) {
            chunkResult.setSkipUpload(true);
            chunkResult.setLocation(file);
            chunkResult.setMessage("完整文件已存在，直接跳过上传，实现秒传");
            return chunkResult;
        }
        ArrayList<Integer> list = chunkInfoMapper.selectChunkNumbers(chunkInfo.getIdentifier(), chunkInfo.getFilename());

        if (list !=null && list.size() > 0) {
            chunkResult.setSkipUpload(false);
            chunkResult.setUploadedChunks(list);
            chunkResult.setMessage("部分文件块已存在，继续上传剩余文件块，实现断点续传");
            return chunkResult;
        }
        return chunkResult;
    }

    /**
     * 写文件
     * @param chunk
     * @return
     */
    @Override
    public Integer uploadFile(ChunkInfo chunk) {
        Integer code = ReturnCode.RC200.getCode();
        MultipartFile file = chunk.getUpfile();
        try {
            byte[] bytes = file.getBytes();
            Path path = Paths.get(FileInfoUtil.generatePath(uploadFolder, chunk));
            Files.write(path, bytes);
            if(chunkInfoMapper.insert(chunk) < 0){
                code = ReturnCode.UPLOAD_FAILED.getCode();
            }
        } catch (IOException e) {
            log.error("写文件出错：{}", e.getMessage());
            code = ReturnCode.UPLOAD_FAILED.getCode();
        }
        return code;
    }

}
