package com.springboot.service;

import com.springboot.entity.UserInfo;
public interface UserService {
    public UserInfo getInfo(String token);
}
