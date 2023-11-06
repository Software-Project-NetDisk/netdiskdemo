package com.springboot.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
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
    // 分页查询中每页的最大数目
    private static final Integer PAGE_RECORDS_NUM = 10;
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

    public IPage<UserInfo> getUserList(Integer currentPage, String query){
        QueryWrapper<UserInfo> wrapper = new QueryWrapper<>();
//        System.out.println(query);
        wrapper.like("user_name", query);
        // 构造分页信息，其中的Page<>(page, PAGE_RECORDS_NUM)的第一个参数是页数，而第二个参数是每页的记录数
        Page<UserInfo> userInfoPage = new Page<>(currentPage, PAGE_RECORDS_NUM);
        // page(postPage, wrapper)这里的第一个参数就是上面定义了的Page对象，第二个参数就是上面定义的条件构造器对象，通过调用这个方法就可以根据你的分页信息以及查询信息获取分页数据
        IPage<UserInfo> userInfoIPage = userInfoMapper.selectPage(userInfoPage, wrapper);
        // 封装数据，其中getRecords()是获取记录数，getCurrent()获取当前页数，getPages()获取总页数，getTotal()获取记录总数，还要其他更多的方法，大家可以自行查看，在这里就不过多赘述了

        return userInfoIPage;
    }
    public int updateUser(Integer user_id, Integer space, Integer isVIP){
        UpdateWrapper<UserInfo> wrapper = new UpdateWrapper<>();
        wrapper.eq("user_id", user_id);
        wrapper.set("space", space);
        wrapper.set("is_VIP", isVIP);
        return userInfoMapper.update(null, wrapper);
    }
    public int deleteUser(Integer user_id){
        QueryWrapper<UserInfo> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", user_id);
        return userInfoMapper.delete(wrapper);
    }
}
