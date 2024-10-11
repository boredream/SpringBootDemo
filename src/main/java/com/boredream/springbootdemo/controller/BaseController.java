package com.boredream.springbootdemo.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.boredream.springbootdemo.mapper.UserMapper;
import io.netty.util.internal.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;

public class BaseController {

    @Autowired
    UserMapper userMapper;

    protected <T> QueryWrapper<T> genUserQuery(long curUserId) {
        return genUserQuery(null, curUserId);
    }

    protected <T> QueryWrapper<T> genUserQuery(String bean, long curUserId) {
        String column = "user_id";
        if(!StringUtil.isNullOrEmpty(bean)) {
            // 限定判断某个bean下面的字段
            column = bean + "." + column;
        }
        return new QueryWrapper<T>().eq(column, curUserId);
    }

}
