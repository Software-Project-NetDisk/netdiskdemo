package com.springboot.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.springboot.entity.FileInfo;
import com.springboot.mapper.FileInfoMapper;
import com.springboot.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

@Service
public class FileServiceImpl implements FileService {
    @Autowired
    private FileInfoMapper fileInfoMapper;
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

        return fileInfoMapper.insert(fileInfo);
    }
}
