package com.springboot.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.springboot.entity.FileInfo;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface FileService extends IService<FileInfo> {
    public List<FileInfo> getFileInfo(Integer user_id, Integer file_pid);
    public int createNewFolder(Integer user_id, Integer file_pid, String folder_name);

    int mergeFile(FileInfo fileInfo);
}
