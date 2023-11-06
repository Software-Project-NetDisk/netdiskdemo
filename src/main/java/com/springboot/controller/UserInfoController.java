package com.springboot.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.springboot.entity.MyException;
import com.springboot.entity.RootInfo;
import com.springboot.entity.UserInfo;
import com.springboot.service.RootService;
import com.springboot.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;


@RestController
@CrossOrigin
@RequestMapping("/user")
public class UserInfoController {
    @Autowired
    private RootService rootService;
    @Autowired
    private UserService userService;

    @PostMapping("/getInfo")
    public UserInfo getInfo(HttpServletRequest request) {

        String token = request.getHeader("token");
        return userService.getInfo(token);
    }
    @PostMapping("/getRootInfo")
    public RootInfo getRootInfo(HttpServletRequest request) {
        String token = request.getHeader("token");
        return rootService.getInfo(token);
    }

    @PostMapping("/getUserList")
    public IPage<UserInfo> getUserList(@RequestBody Map<String,Object> map) throws MyException {
        Integer currentPage = (Integer) map.get("currentPage");
        String query = (String) map.get("query");
        return userService.getUserList(currentPage, query);
    }
    @PostMapping("/updateUser")
    public int updateUser(@RequestBody Map<String,Object> map) throws MyException {
        Integer user_id = (Integer) map.get("user_id");
        Integer space = (Integer) map.get("space");
        Integer isVIP = (Integer) map.get("isVIP");
        return userService.updateUser(user_id, space, isVIP);
    }
    @PostMapping("/deleteUser")
    public int deleteUser(@RequestBody Map<String,Object> map) throws MyException {
        Integer user_id = (Integer) map.get("user_id");
        return userService.deleteUser(user_id);
    }
}


