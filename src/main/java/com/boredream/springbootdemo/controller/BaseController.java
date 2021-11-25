package com.boredream.springbootdemo.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.boredream.springbootdemo.entity.User;
import com.boredream.springbootdemo.mapper.UserMapper;
import io.netty.util.internal.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;

public class BaseController {

    @Autowired
    UserMapper userMapper;

    protected <T> QueryWrapper<T> genUserQuery(long curUserId) {
        // TODO: chunyang 2021/11/25 是否影响性能？
        return genUserQuery(null, curUserId);
    }

    protected <T> QueryWrapper<T> genUserQuery(String bean, long curUserId) {
        String column = "user_id";
        if(!StringUtil.isNullOrEmpty(bean)) {
            // 限定判断某个bean下面的字段
            column = bean + "." + column;
        }

        QueryWrapper<T> wrapper;
        User user = userMapper.selectById(curUserId);
        Long cpUserId = user.getCpUserId();
        if (cpUserId != null) {
            //  如果有伴侣，共享数据
            wrapper = new QueryWrapper<T>().in(column, curUserId, cpUserId);
        } else {
            wrapper = new QueryWrapper<T>().eq(column, curUserId);
        }
        return wrapper;
    }

}
