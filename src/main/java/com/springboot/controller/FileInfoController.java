package com.springboot.controller;

import com.springboot.entity.*;
import com.springboot.service.ChunkInfoService;
import com.springboot.service.FileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@CrossOrigin
@RequestMapping("/file")
public class FileInfoController {
    @Autowired
    private FileService fileService;

    @Autowired
    private ChunkInfoService chunkInfoService;

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
    /**
     * 校验文件
     *
     * @param chunk
     * @return
     */
    @GetMapping("/uploadFile")
    public ChunkResult checkChunk(ChunkInfo chunk) throws MyException {
        log.info("校验文件：{}", chunk);
        ChunkResult chunkResult = chunkInfoService.checkChunkState(chunk);
        if (chunkResult.isOverflow()) {
            int code = ReturnCode.OVERFLOW.getCode();
            String message = ReturnCode.OVERFLOW.getMessage();
            throw new MyException(code, message);
        } else {
            return chunkResult;
        }
    }

    /**
     * 文件块上传
     *
     * @param chunk
     * @return
     */
    @PostMapping("/uploadFile")
    public ResultData uploadChunk(ChunkInfo chunk) {
        ResultData resultData = new ResultData<>();
        Integer code = chunkInfoService.uploadFile(chunk);
        if (code == ReturnCode.RC200.getCode()) {
            resultData.setStatus(ReturnCode.RC200.getCode());
            resultData.setMessage("上传成功");
        } else {
            resultData.setStatus(ReturnCode.UPLOAD_FAILED.getCode());
            resultData.setMessage("上传失败");
        }
        return resultData;
    }
    @PostMapping("/mergeFile")
    public ResultData mergeFile(@RequestBody Map<String,Object> map) {
        Integer user_id = (Integer) map.get("user_id");
        Integer file_pid = (Integer) map.get("file_pid");
        String file_md5 = (String)map.get("file_md5");
        String file_name = (String)map.get("file_name");
        Integer file_size = (Integer) map.get("file_size");

        FileInfo fileInfo = new FileInfo();
        fileInfo.setUser_id(user_id);
        fileInfo.setFile_pid(file_pid);
        fileInfo.setFile_md5(file_md5);
        fileInfo.setFile_name(file_name);
        fileInfo.setFile_size(file_size);
        fileInfo.setIs_folder(0);
        fileInfo.setUpdate_time(new Timestamp(System.currentTimeMillis()));

        ResultData resultData = new ResultData<>();
        Integer code = fileService.mergeFile(fileInfo);
        if (code == ReturnCode.RC200.getCode()) {
            resultData.setStatus(ReturnCode.RC200.getCode());
            resultData.setMessage("合并成功");
        } else if (code == ReturnCode.FILE_EXIT.getCode()) {

            resultData.setStatus(ReturnCode.RC200.getCode());
            resultData.setMessage("文件已存在，无需合并");
        } else {
            resultData.setStatus(ReturnCode.MERGE_ERROR.getCode());
            resultData.setMessage("合并失败");
        }
        return resultData;
    }
}
