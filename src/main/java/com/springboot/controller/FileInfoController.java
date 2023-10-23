package com.springboot.controller;


import com.springboot.mapper.FileInfoMapper;
import com.springboot.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
@RequestMapping("/file_info")
public class FileInfoController {

    private FileInfoMapper fileInfoMapper;
    private FileService fileService;

}
