package com.springboot.controller;

import com.springboot.entity.*;
import com.springboot.service.ChunkInfoService;
import com.springboot.service.FileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.charset.StandardCharsets;
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
    /*
    文件彻底删除
    */
    @PostMapping("/deleteFile")
    public int deleteFile (@RequestBody Map<String,Object> map) throws MyException {
        List<Integer> file_id = (List<Integer>) map.get("file_id");
        try{
            int deletefile = fileService.deleteFile(file_id);
            System.out.println("删除成功"+deletefile+"个文件");
            return deletefile;
        }catch (Exception e){
            int code = ReturnCode.FAILED_TO_CREATE_FOLDER.getCode();
            String message = ReturnCode.FAILED_TO_CREATE_FOLDER.getMessage();
            throw new MyException(code, message);
        }
    }
    /*
    逻辑删除
    */
    @PostMapping("/recycleFile")
    public int recycleFile (@RequestBody Map<String,Object> map) throws MyException{
        Integer user_id = (Integer) map.get("user_id");
        Integer file_pid = (Integer) map.get("file_pid");
        List<Integer> file_id = (List<Integer>) map.get("file_id");
        Integer recycled = (Integer) map.get("recycled");
        try{
            int fileInfos = fileService.recycleFile(user_id, file_pid,file_id,recycled);
            System.out.println("回收成功"+fileInfos+"个文件");
            return fileInfos;
        }catch (Exception e){
            int code = ReturnCode.FAILED_TO_CREATE_FOLDER.getCode();
            String message = ReturnCode.FAILED_TO_CREATE_FOLDER.getMessage();
            throw new MyException(code, message);
        }
    }
    @PostMapping("/RecycleList")
    public List<FileInfo> getRecycleList(@RequestBody Map<String,Object> map) throws MyException {
        Integer user_id = (Integer) map.get("user_id");
        Integer file_pid = (Integer) map.get("file_pid");
        List<FileInfo> fileInfos = fileService.getRecycledInfo(user_id, file_pid);

        return fileInfos;
    }
    @PostMapping("/DownLoad")
//    public void downLoad(@RequestBody Map<String,Object> map) throws IOException {
//        String file_name = (String) map.get("file_name");
//        String file_path = (String) map.get("file_path");
//        String destination = (String) map.get("destination");
//        String fileOrigin = file_path+"/"+file_name;
//        FileInputStream inputStream = new FileInputStream(fileOrigin);
//        FileOutputStream outputStream = new FileOutputStream(destination);
//        byte[] b = new byte[1024];
//        int len;
//        while((len=inputStream.read(b))!=-1){
//            outputStream.write(b,0,len);
//        }
//
//    }
    public void downLoad(@RequestBody Map<String,Object> map,HttpServletResponse response) throws MyException, IOException {
        String file_name = (String) map.get("file_name");
        String file_path = (String) map.get("file_path");
//        Integer VIP = (Integer) map.get("VIP");
        byte[] name = file_name.getBytes(StandardCharsets.UTF_8);
        String newFileName  = new String(name,"ISO-8859-1");
        String fileOrigin = file_path+"/"+file_name;
        FileInputStream inputStream = new FileInputStream(fileOrigin);
        response.setHeader("Content-Disposition","attachment;filename="+newFileName);
        BufferedOutputStream outputStream = new BufferedOutputStream(response.getOutputStream());
        byte[] b = new byte[1024];
        int len;
        while((len=inputStream.read(b))!=-1){
            outputStream.write(b,0,len);
        }
//        if(VIP.equals(1)) {
//            byte[] b = new byte[1024];
//            int len;
//            while((len=inputStream.read(b))!=-1){
//                outputStream.write(b,0,len);
//            }
//        }else{
//            byte[] b = new byte[1];
//            int len;
//            while((len=inputStream.read(b))!=-1){
//                outputStream.write(b,0,len);
//            }
//        }
        inputStream.close();
        outputStream.close();
    }
//    public String downLoad(HttpServletRequest request,HttpServletResponse response) throws MyException, IOException {
//        System.out.println("1");
//        String file_name = request.getParameter("file_name");
//        String file_path = request.getParameter("file_path");
//        String VIP = request.getParameter("VIP");
//        String velocity = "1";
//        String fileOrigin = file_path+"/"+file_name;
//        //文件名处理
//        byte[] name = file_name.getBytes(StandardCharsets.UTF_8);
//        String newFileName  = new String(name,"ISO-8859-1");
//        FileInputStream inputStream = new FileInputStream(fileOrigin);
//        response.setHeader("Content-Disposition","attachment;filename="+newFileName);
//        BufferedOutputStream outputStream = new BufferedOutputStream(response.getOutputStream());
//        if(VIP.equals(velocity)) {
//            byte[] b = new byte[1024];
//            int len;
//            while((len=inputStream.read(b))!=-1){
//                outputStream.write(b,0,len);
//            }
//        }else{
//            byte[] b = new byte[1];
//            int len;
//            while((len=inputStream.read(b))!=-1){
//                outputStream.write(b,0,len);
//            }
//
//        }
//
//        inputStream.close();
//        outputStream.close();
//        String a="1";
//        return a;
//    }
}
