package com.springboot.service;

import com.springboot.entity.UserInfo;

public interface AccountService {
    public String login(String email, String password);
    public String rootLogin(String email, String password);
    public int register(String email, String password, String user_name);
}
