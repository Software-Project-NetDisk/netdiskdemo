package com.springboot.controller;

import com.springboot.mapper.UserInfoMapper;
import com.springboot.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@CrossOrigin
@RequestMapping("/user_info")
public class UserInfoController {
    @Autowired
    private UserInfoMapper userInfoMapper;
    @Autowired
    private UserService userService;
}


