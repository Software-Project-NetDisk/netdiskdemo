package com.springboot.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.springboot.entity.FileInfo;
import com.springboot.entity.UserInfo;
import com.springboot.mapper.FileInfoMapper;
import com.springboot.mapper.UserInfoMapper;
import com.springboot.service.UserService;
import com.springboot.utils.TokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserInfoMapper userInfoMapper;
    @Autowired
    private FileInfoMapper fileInfoMapper;

    @Autowired
    TokenUtil tokenUtil;
    public UserInfo getInfo(String token) {
        TokenUtil tokenUtil = new TokenUtil();
        HashMap<String, String> map = tokenUtil.parseToken(token);
        Integer user_id = Integer.parseInt(map.get("user_id"));
        UserInfo userInfo = userInfoMapper.selectById(user_id);

        // 抹去密码信息
        userInfo.setPassword(null);
        QueryWrapper<FileInfo> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", user_id);
        wrapper.select("sum(file_size) as space_used");
        userInfo.setSpace_used((BigDecimal) fileInfoMapper.selectMaps(wrapper).get(0).get("space_used"));
        return userInfo;
    }
}
