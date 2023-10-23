package com.springboot.controller;

import com.springboot.entity.UserInfo;
import com.springboot.mapper.UserInfoMapper;
import com.springboot.service.AccountService;
import com.springboot.utils.TokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping("/user")
public class AccountController {
    @Autowired
    private UserInfoMapper userInfoMapper;
    @Autowired
    private AccountService accountService;

    @PostMapping("/login")
    public HashMap<String, String> login(@RequestBody Map<String,Object> map){
        String email = (String) map.get("email");
        String password = (String) map.get("password");

        String token = accountService.login(email, password);
        if (token == null) {
            throw new RuntimeException("邮箱或密码错误");
        }

        HashMap<String, String> res = new HashMap<>();
        res.put("token", token);
        return res;
    }

    @PostMapping("/register")
    public String register(@RequestBody Map<String,Object> map){
        String email = (String) map.get("email");
        String password = (String) map.get("password");
        String user_name = (String) map.get("user_name");

        try {
            accountService.register(email, password, user_name);
            return "注册成功";
        } catch (Exception e){
            throw new RuntimeException("该邮箱已被注册");
        }
    }

    @PostMapping("/getInfo")
    public UserInfo getInfo(HttpServletRequest request) {

        String token = request.getHeader("token");

        return accountService.getInfo(token);
    }
}
