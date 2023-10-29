package com.springboot.controller;

import com.springboot.entity.UserInfo;
import com.springboot.mapper.UserInfoMapper;
import com.springboot.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;


@RestController
@CrossOrigin
@RequestMapping("/user")
public class UserInfoController {
    @Autowired
    private UserInfoMapper userInfoMapper;
    @Autowired
    private UserService userService;

    @PostMapping("/getInfo")
    public UserInfo getInfo(HttpServletRequest request) {

        String token = request.getHeader("token");
        return userService.getInfo(token);
    }
}


