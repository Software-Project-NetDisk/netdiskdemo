package com.springboot.service.impl;

import com.springboot.entity.RootInfo;
import com.springboot.mapper.RootInfoMapper;
import com.springboot.service.RootService;
import com.springboot.utils.TokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public class RootServiceImpl implements RootService {
    @Autowired
    private RootInfoMapper rootInfoMapper;

    @Autowired
    TokenUtil tokenUtil;
    public RootInfo getInfo(String token) {
        TokenUtil tokenUtil = new TokenUtil();
        HashMap<String, String> map = tokenUtil.parseToken(token);
        Integer user_id = Integer.parseInt(map.get("user_id"));
        RootInfo rootInfo = rootInfoMapper.selectById(user_id);
        // 抹去密码信息
        rootInfo.setPassword(null);
        return rootInfo;
    }
}
