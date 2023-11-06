package com.springboot.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.springboot.entity.UserInfo;

import java.util.List;

public interface UserService {
    public UserInfo getInfo(String token);
    public IPage<UserInfo> getUserList(Integer currentPage, String query);
    public int updateUser(Integer user_id, Integer space, Integer isVIP);
    public int deleteUser(Integer user_id);
}
