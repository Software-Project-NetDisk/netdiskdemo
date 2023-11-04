package com.springboot.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.springboot.entity.UserInfo;
import com.springboot.mapper.UserInfoMapper;
import com.springboot.service.AccountService;
import com.springboot.utils.TokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;

@Service
public class AccountServiceImpl implements AccountService {
    @Autowired
    private UserInfoMapper userInfoMapper;

    @Autowired
    TokenUtil tokenUtil;

    public String login(String email, String password) {
        String token = null;

        QueryWrapper<UserInfo> wrapper = new QueryWrapper<>();
        wrapper.eq("email", email);
        wrapper.eq("password", password);
        UserInfo userInfo = userInfoMapper.selectOne(wrapper);

        if (userInfo != null) {
            token = tokenUtil.getToken(userInfo.getUser_id().toString(), userInfo.getEmail());
        }

        return token;
    }

    public int register(String email, String password, String user_name) {
        UserInfo userInfo = new UserInfo();
        userInfo.setEmail(email);
        userInfo.setPassword(password);
        userInfo.setUser_name(user_name);
        userInfo.setDeadline(new Timestamp(System.currentTimeMillis()));

        return userInfoMapper.insert(userInfo);
    }
}
