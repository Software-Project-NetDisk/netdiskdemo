package com.springboot.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.springboot.entity.*;
import com.springboot.mapper.ChunkInfoMapper;
import com.springboot.mapper.FileInfoMapper;
import com.springboot.mapper.UserInfoMapper;
import com.springboot.service.ChunkInfoService;
import com.springboot.utils.FileInfoUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import lombok.extern.slf4j.Slf4j;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.util.ArrayList;

@Slf4j
@Service
public class ChunkInfoServiceImpl extends ServiceImpl<ChunkInfoMapper, ChunkInfo> implements ChunkInfoService {
    @Value("${base.file-path}")
    private String uploadFolder;
    @Autowired
    private ChunkInfoMapper chunkInfoMapper;
    @Autowired
    private FileInfoMapper fileInfoMapper;
    @Autowired UserInfoMapper userInfoMapper;

    /**
     * 校验当前文件
     * @param chunkInfo
     * @return 秒传？续传？新传？
     */
    @Override
    public ChunkResult checkChunkState(ChunkInfo chunkInfo) {
        ChunkResult chunkResult = new ChunkResult();
        String file = uploadFolder + File.separator + chunkInfo.getIdentifier() + File.separator + chunkInfo.getFilename();

        QueryWrapper<FileInfo> wrapper = new QueryWrapper<>();
        // 判断容量是否足够上传
        wrapper.eq("user_id", chunkInfo.getUserId());
        wrapper.select("sum(file_size) as space_used");
        BigDecimal space_used = (BigDecimal) fileInfoMapper.selectMaps(wrapper).get(0).get("space_used");
        wrapper.clear();
        long space =  userInfoMapper.selectById(chunkInfo.getUserId()).getSpace();
        System.out.println("space_used: "+space_used);
        System.out.println("space:" + space);
        System.out.println("total: "+(chunkInfo.getTotalSize() + Integer.parseInt(space_used.toString())));
        if (chunkInfo.getTotalSize() + Integer.parseInt(space_used.toString()) > space) {
            chunkResult.setSkipUpload(true);
            chunkResult.setLocation(file);
            chunkResult.setMessage("容量不足，无法上传");
            chunkResult.setOverflow(true);
            return chunkResult;
        }

        wrapper.clear();
        // 查询本地是否已经有该文件，有的话就跳过上传实现秒传

        if (FileInfoUtil.fileExists(file)) {
            chunkResult.setSkipUpload(true);
            chunkResult.setLocation(file);
            chunkResult.setMessage("完整文件已存在，直接跳过上传，实现秒传");
            // 判断当前用户的当前目录下是否有该文件，没有则新插入一条记录
            wrapper.eq("file_md5", chunkInfo.getIdentifier());
            wrapper.eq("user_id", chunkInfo.getUserId());
            wrapper.eq("file_pid", chunkInfo.getFilePid());
            FileInfo fileInfo1 = fileInfoMapper.selectOne(wrapper);
            if(fileInfo1 == null) {
                FileInfo fileInfo = new FileInfo();
                fileInfo.setFile_name(chunkInfo.getFilename());
                fileInfo.setFile_type(6);
                fileInfo.setFile_size(chunkInfo.getTotalSize());
                fileInfo.setUpdate_time(new Timestamp(System.currentTimeMillis()));
                fileInfo.setFile_md5(chunkInfo.getIdentifier());
                fileInfo.setUser_id(chunkInfo.getUserId());
                fileInfo.setFile_pid(chunkInfo.getFilePid());
                fileInfo.setRecycled(0);
                fileInfo.setFile_path(uploadFolder + File.separator + chunkInfo.getIdentifier());
                fileInfo.setIs_folder(0);
                fileInfoMapper.insert(fileInfo);
            }
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
