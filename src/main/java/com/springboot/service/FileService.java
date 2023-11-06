package com.springboot.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.springboot.entity.FileInfo;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.List;

public interface FileService extends IService<FileInfo> {
    public List<FileInfo> getFileInfo(Integer user_id, Integer file_pid);
    public int createNewFolder(Integer user_id, Integer file_pid, String folder_name);
    public int recycleFile(Integer user_id, Integer file_pid,List<Integer> file_id, Integer recycle);
    public int deleteFile(List<Integer> file_id);
    public List<FileInfo> getRecycledInfo(Integer user_id, Integer file_pid);
    int mergeFile(FileInfo fileInfo);
    public void deleteDir(File src);
}
