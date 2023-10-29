package com.springboot.controller;


import com.springboot.entity.FileInfo;
import com.springboot.entity.MyException;
import com.springboot.entity.ReturnCode;
import com.springboot.mapper.FileInfoMapper;
import com.springboot.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping("/file")
public class FileInfoController {
    @Autowired
    private FileService fileService;

    @PostMapping("/fileList")
    public List<FileInfo> getFileList(@RequestBody Map<String,Object> map) throws MyException {
        Integer user_id = (Integer) map.get("user_id");
        Integer file_pid = (Integer) map.get("file_pid");
        List<FileInfo> fileInfos = fileService.getFileInfo(user_id, file_pid);

        return fileInfos;
    }

    @PostMapping("/createNewFolder")
    public String createNewFolder(@RequestBody Map<String,Object> map) throws MyException {
        Integer user_id = (Integer) map.get("user_id");
        Integer file_pid = (Integer) map.get("file_pid");
        String folder_name = (String) map.get("folder_name");
        System.out.println("folder_name: " + folder_name);

        try {
            fileService.createNewFolder(user_id, file_pid, folder_name);
            return "创建文件夹成功";
        } catch (Exception e) {
            int code = ReturnCode.FAILED_TO_CREATE_FOLDER.getCode();
            String message = ReturnCode.FAILED_TO_CREATE_FOLDER.getMessage();
            throw new MyException(code, message);
        }
    }
}
