package com.boredream.springbootdemo.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.boredream.springbootdemo.entity.User;
import com.boredream.springbootdemo.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;

public class BaseController {

    @Autowired
    UserMapper userMapper;

    protected <T> QueryWrapper<T> genUserQuery(long curUserId) {
        QueryWrapper<T> wrapper;
        User user = userMapper.selectById(curUserId);
        Long cpUserId = user.getCpUserId();
        if (cpUserId != null) {
            //  如果有伴侣，共享数据
            wrapper = new QueryWrapper<T>().in("user_id", curUserId, cpUserId);
        } else {
            wrapper = new QueryWrapper<T>().eq("user_id", curUserId);
        }
        return wrapper;
    }

}
