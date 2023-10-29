package com.springboot.service;

import com.springboot.entity.FileInfo;

import java.util.List;

public interface FileService {
    public List<FileInfo> getFileInfo(Integer user_id, Integer file_pid);
    public int createNewFolder(Integer user_id, Integer file_pid, String folder_name);
}
