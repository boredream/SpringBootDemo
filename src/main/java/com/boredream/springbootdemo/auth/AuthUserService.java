package com.boredream.springbootdemo.auth;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.boredream.springbootdemo.entity.User;
import com.boredream.springbootdemo.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@Configuration
public class AuthUserService implements UserDetailsService {

    @Autowired
    UserMapper mapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        QueryWrapper<User> wrapper = new QueryWrapper<User>().eq("username", username);
        User user = mapper.selectOne(wrapper);
        if(user == null) throw new UsernameNotFoundException("User Not Found Exception");
        return new AuthUser(user);
    }
}