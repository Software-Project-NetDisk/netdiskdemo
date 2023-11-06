package com.springboot.controller;

import com.springboot.entity.MyException;
import com.springboot.entity.ReturnCode;
import com.springboot.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping("/account")
public class AccountController {
    @Autowired
    private AccountService accountService;

    @PostMapping("/login")
    public HashMap<String, Object> login(@RequestBody Map<String,Object> map) throws MyException {
        String email = (String) map.get("email");
        String password = (String) map.get("password");
        String token = accountService.rootLogin(email, password);
        if (token != null) {
            HashMap<String, Object> res = new HashMap<>();
            res.put("token", token);
            res.put("isRoot", true);
            return res;
        }
        token = accountService.login(email, password);
        if (token == null) {
            int code = ReturnCode.EMAIL_OR_PASSWORD_ERROR.getCode();
            String message = ReturnCode.EMAIL_OR_PASSWORD_ERROR.getMessage();
            throw new MyException(code, message);
        }

        HashMap<String, Object> res = new HashMap<>();
        res.put("token", token);
        return res;
    }

    @PostMapping("/register")
    public String register(@RequestBody Map<String,Object> map) throws MyException {
        String email = (String) map.get("email");
        String password = (String) map.get("password");
        String user_name = (String) map.get("user_name");

        try {
            accountService.register(email, password, user_name);
            return "注册成功";
        } catch (Exception e){
            int code = ReturnCode.EMAIL_EXIST.getCode();
            String message = ReturnCode.EMAIL_EXIST.getMessage();
            throw new MyException(code, message);
        }
    }
}
