package com.springboot.service;

import com.springboot.entity.RootInfo;

public interface RootService {
    public RootInfo getInfo(String token);
}
