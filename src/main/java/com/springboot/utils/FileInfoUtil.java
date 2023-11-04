package com.springboot.utils;

import com.springboot.entity.ChunkInfo;
import com.springboot.entity.ReturnCode;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.file.*;

/**
 * @ClassName FileInfoUntils
 * @Description 文件工具类
 * @Author huqiang
 * @Date 2020/11/9 17:36
 * @Version 1.0
 */
@Slf4j
public class FileInfoUtil {

    /**
     * 创建路径
     *
     * @param uploadFolder
     * @param chunk
     * @return 文件
     */
    public static String generatePath(String uploadFolder, ChunkInfo chunk) {
        StringBuilder sb = new StringBuilder();
        sb.append(uploadFolder).append("/").append(chunk.getIdentifier());
        //判断uploadFolder/identifier 路径是否存在，不存在则创建
        if (!Files.isWritable(Paths.get(sb.toString()))) {
            log.info("路径不存在，新建路径: {}", sb.toString());
            try {
                Files.createDirectories(Paths.get(sb.toString()));
            } catch (IOException e) {
                log.error("创建路径错误:{},{}", e.getMessage(), e);
            }
        }
        return sb.append("/")
                .append(chunk.getFilename())
                .append("-")
                .append(chunk.getChunkNumber()).toString();
    }

    /**
     * 合并
     *
     * @param file
     * @param folder
     * @param filename
     * @return 状态
     */
    public static Integer merge(String file, String folder, String filename) {
        //默认合并成功
        Integer rlt = ReturnCode.RC200.getCode();
        try {
            //先判断文件是否存在
            if (fileExists(file)) {
                //文件已存在
                rlt = ReturnCode.FILE_EXIT.getCode();
            } else {
                //不存在的话，进行合并
                Files.createFile(Paths.get(file));
                Files.list(Paths.get(folder))
                        .filter(path -> !path.getFileName().toString().equals(filename))
                        .sorted((o1, o2) -> {
                            String p1 = o1.getFileName().toString();
                            String p2 = o2.getFileName().toString();
                            int i1 = p1.lastIndexOf("-");
                            int i2 = p2.lastIndexOf("-");
                            return Integer.valueOf(p2.substring(i2)).compareTo(Integer.valueOf(p1.substring(i1)));
                        })
                        .forEach(path -> {
                            try {
                                //以追加的形式写入文件
                                Files.write(Paths.get(file), Files.readAllBytes(path), StandardOpenOption.APPEND);
                                //合并后删除该块
                                Files.delete(path);
                            } catch (IOException e) {
                                log.error("删除文件失败：{},{}", e.getMessage(), e);
                            }
                        });
            }
        } catch (IOException e) {
            log.error("合并失败：{},{}", e.getMessage(), e);
            //合并失败
            rlt = ReturnCode.MERGE_ERROR.getCode();
        }
        return rlt;
    }

    /**
     * 根据文件的全路径名判断文件是否存在
     *
     * @param file
     * @return
     */
    public static boolean fileExists(String file) {
        boolean fileExists = false;
        Path path = Paths.get(file);
        fileExists = Files.exists(path, new LinkOption[]{LinkOption.NOFOLLOW_LINKS});
        return fileExists;
    }
}
