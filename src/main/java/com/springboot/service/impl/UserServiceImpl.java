package com.springboot.service.impl;

import com.springboot.entity.UserInfo;
import com.springboot.mapper.UserInfoMapper;
import com.springboot.service.UserService;
import com.springboot.utils.TokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserInfoMapper userInfoMapper;

    @Autowired
    TokenUtil tokenUtil;
    public UserInfo getInfo(String token) {
        TokenUtil tokenUtil = new TokenUtil();
        HashMap<String, String> map = tokenUtil.parseToken(token);
        Integer user_id = Integer.parseInt(map.get("user_id"));
        UserInfo userInfo = userInfoMapper.selectById(user_id);

        // 抹去密码信息
        userInfo.setPassword(null);
        return userInfo;
    }
}
