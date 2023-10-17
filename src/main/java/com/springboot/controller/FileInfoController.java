package com.springboot.controller;

import com.springboot.entity.FileInfo;
import com.springboot.mapper.FileInfoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Timestamp;
import java.util.List;

@RestController
public class FileInfoController {
    @Autowired
    FileInfoMapper fileInfoMapper;

    @RequestMapping("/insert")
    public String insert(Integer file_id, String file_name, Integer file_type, Integer file_size, Timestamp update_time, Integer user_id, Integer file_pid, Integer recycled, String file_md5, String file_path, Integer is_folder){
        return fileInfoMapper.insert(new FileInfo(file_id, file_name, file_type, file_size, update_time, user_id, file_pid, recycled, file_md5, file_path, is_folder)) > 0 ? "success" : "fail";
    }

    @RequestMapping("/selectAll")
    public List<FileInfo> selectAll(){
        return fileInfoMapper.selectList(null);
    }

    @RequestMapping("/selectOne")
    public FileInfo selectOne(){
        return fileInfoMapper.selectOne();
    }
}
