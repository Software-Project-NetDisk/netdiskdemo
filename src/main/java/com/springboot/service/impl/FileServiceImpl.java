package com.springboot.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.springboot.entity.ChunkInfo;
import com.springboot.entity.FileInfo;
import com.springboot.entity.MyException;
import com.springboot.entity.ReturnCode;
import com.springboot.mapper.ChunkInfoMapper;
import com.springboot.mapper.FileInfoMapper;
import com.springboot.service.FileService;
import com.springboot.utils.FileInfoUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.io.File;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import java.util.Stack;

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
    //回收站文件-------
    public int recycleFile(Integer user_id, Integer file_pid,List<Integer> file_id, Integer recycled){
        FileInfo recycledInfo = new FileInfo();
        Stack<Integer> file = new Stack<>();
        QueryWrapper<FileInfo> wrapper = new QueryWrapper<>();
        List<FileInfo> fileInfo = fileInfoMapper.selectList(wrapper);
        Integer i;
        int count=0;
        for(i=0;i<file_id.size();i++)
            file.push(file_id.get(i));
        while(!file.isEmpty()){
            {
                Integer flag = file.pop();
                recycledInfo.setFile_id(flag);
                recycledInfo.setRecycled(recycled);
                fileInfoMapper.updateById(recycledInfo);
                count++;
                QueryWrapper<FileInfo> wrappers = new QueryWrapper<>();
                wrappers.eq("file_id", flag);
                List<FileInfo> foldInfo = fileInfoMapper.selectList(wrappers);
                if(foldInfo.get(0).getIs_folder()==1){
                    for(i=0;i<fileInfo.size();i++){
                        Integer pid = fileInfo.get(i).getFile_pid();
                        if(flag.equals(pid)){
                            Integer id = fileInfo.get(i).getFile_id();
                            file.push(fileInfo.get(i).getFile_id());
                        }
                    }
                }
            }
        }
        return count;
    }
    //彻底删除文件-------
    public int deleteFile(List<Integer> file_id){
        Stack<Integer> file = new Stack<>();
        QueryWrapper<FileInfo> wrapper = new QueryWrapper<>();
        List<FileInfo> fileInfo = fileInfoMapper.selectList(wrapper);

        Integer i;
        int count=0;
        for(i=0;i<file_id.size();i++)
            file.push(file_id.get(i));
        while(!file.isEmpty()){
                Integer flag = file.pop();
                QueryWrapper<FileInfo> wrappers = new QueryWrapper<>();
                count++;
                wrappers.eq("file_id", flag);
                List<FileInfo> flagInfo = fileInfoMapper.selectList(wrappers);
                if(flagInfo.get(0).getIs_folder()!=0){
                    for(i=0;i<fileInfo.size();i++){
                        Integer pid = fileInfo.get(i).getFile_pid();
                        if(flag.equals(pid)){
                            file.push(fileInfo.get(i).getFile_id());
                            count++;
                        }
                    }
                }
                fileInfoMapper.deleteById(flag);
                fileInfo = fileInfoMapper.selectList(wrapper);
                String flag_md5 = flagInfo.get(0).getFile_md5();
                for(i=0;i<fileInfo.size();i++){
                    String file_md5 = fileInfo.get(i).getFile_md5();
                    if ( flag_md5.equals(file_md5)){
                        break;
                    }
                }
                if(i>=fileInfo.size()){
                    String path = flagInfo.get(0).getFile_path();
                    File disfile = new File(path);
                    deleteDir(disfile);
                }
            }
        return count;
    }
    public List<FileInfo> getRecycledInfo(Integer user_id, Integer file_pid) {
        QueryWrapper<FileInfo> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", user_id);
        wrapper.eq("file_pid", file_pid);
        wrapper.eq("recycled",1);
        List<FileInfo> fileInfo = fileInfoMapper.selectList(wrapper);
        return fileInfo;
    }
    public void deleteDir(File src) {
        //先删掉这个文件夹里面所有的内容.
        //递归 方法在方法体中自己调用自己.
        //注意: 可以解决所有文件夹和递归相结合的题目
        //2.遍历这个File对象,获取它下边的每个文件和文件夹对象
        File[] files = src.listFiles();
        //3.判断当前遍历到的File对象是文件还是文件夹
        for (File file : files) {
            //4.如果是文件,直接删除
            if(file.isFile()){
                file.delete();
            }else{
                //5.如果是文件夹,递归调用自己,将当前遍历到的File对象当做参数传递
                deleteDir(file);//参数一定要是src文件夹里面的文件夹File对象
            }
        }
        //6.参数传递过来的文件夹File对象已经处理完成,最后直接删除这个空文件夹
        src.delete();
    }
}



