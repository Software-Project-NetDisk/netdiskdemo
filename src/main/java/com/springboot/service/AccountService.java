package com.springboot.service;

import com.springboot.entity.UserInfo;

public interface AccountService {
    public String login(String email, String password);
    public int register(String email, String password, String user_name);
    public int changePassword(Integer user_id,String password, String new_password);
}
