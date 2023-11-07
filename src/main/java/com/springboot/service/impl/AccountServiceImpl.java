package com.springboot.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.springboot.entity.RootInfo;
import com.springboot.entity.FileInfo;
import com.springboot.entity.UserInfo;
import com.springboot.mapper.RootInfoMapper;
import com.springboot.mapper.UserInfoMapper;
import com.springboot.service.AccountService;
import com.springboot.utils.TokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

@Service
public class AccountServiceImpl implements AccountService {
    @Autowired
    private UserInfoMapper userInfoMapper;

    @Autowired
    private RootInfoMapper rootInfoMapper;
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

    public String rootLogin(String email, String password) {
        String token = null;

        QueryWrapper<RootInfo> wrapper = new QueryWrapper<>();
        wrapper.eq("email", email);
        wrapper.eq("password", password);
        RootInfo rootInfo = rootInfoMapper.selectOne(wrapper);

        if (rootInfo != null) {
            token = tokenUtil.getToken(rootInfo.getUser_id().toString(), rootInfo.getEmail());
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
    public int changePassword(Integer user_id,String password,String new_password){
        QueryWrapper<UserInfo> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", user_id);
        List<UserInfo> userInfos = userInfoMapper.selectList(wrapper);
        String psw = userInfos.get(0).getPassword();
        if(psw.equals(password)){
            UserInfo userInfo = new UserInfo();
            userInfo.setUser_id(user_id);
            userInfo.setPassword(new_password);
            userInfoMapper.updateById(userInfo);
            return 0;
        }
        return 1;
    }
}
