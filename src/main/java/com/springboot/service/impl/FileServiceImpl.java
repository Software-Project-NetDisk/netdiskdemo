package com.springboot.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.springboot.entity.ChunkInfo;
import com.springboot.entity.FileInfo;
import com.springboot.entity.ReturnCode;
import com.springboot.mapper.ChunkInfoMapper;
import com.springboot.mapper.FileInfoMapper;
import com.springboot.service.FileService;
import com.springboot.utils.FileInfoUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.sql.Timestamp;
import java.util.List;

@Service
public class FileServiceImpl extends ServiceImpl<FileInfoMapper, FileInfo> implements FileService {
    @Value("${base.file-path}")
    private String uploadFolder;
    @Autowired
    private FileInfoMapper fileInfoMapper;
    @Autowired
    private ChunkInfoMapper chunkInfoMapper;
    public List<FileInfo> getFileInfo(Integer user_id, Integer file_pid) {
        QueryWrapper<FileInfo> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", user_id);
        wrapper.eq("file_pid", file_pid);
        List<FileInfo> fileInfo = fileInfoMapper.selectList(wrapper);
        return fileInfo;
    }
    public int createNewFolder(Integer user_id, Integer file_pid, String folder_name) {
        FileInfo fileInfo = new FileInfo();
        fileInfo.setFile_name(folder_name);
        fileInfo.setUpdate_time(new Timestamp(System.currentTimeMillis()));
        fileInfo.setUser_id(user_id);
        fileInfo.setFile_pid(file_pid);
        fileInfo.setIs_folder(1);
        fileInfo.setFile_size(0);

        return fileInfoMapper.insert(fileInfo);
    }

    public int mergeFile(FileInfo fileInfo) {
        //进行文件的合并操作
        String filename = fileInfo.getFile_name();
        String file = uploadFolder + File.separator + fileInfo.getFile_md5() + File.separator + filename;
        String folder = uploadFolder + File.separator + fileInfo.getFile_md5();

        Integer fileSuccess = FileInfoUtil.merge(file, folder, filename);
        fileInfo.setFile_path(folder);
        QueryWrapper<ChunkInfo> wrapper = new QueryWrapper<>();
        wrapper
                .eq("identifier", fileInfo.getFile_md5())
                .eq("file_name", fileInfo.getFile_name());
        chunkInfoMapper.delete(wrapper);
        //文件合并成功后，保存记录
        if (fileSuccess == ReturnCode.RC200.getCode()) {
            fileInfoMapper.insert(fileInfo);
        }
        return fileSuccess;
    }
}


