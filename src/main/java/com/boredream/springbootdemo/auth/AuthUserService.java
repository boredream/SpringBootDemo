package com.boredream.springbootdemo.auth;

import com.boredream.springbootdemo.auth.AuthUser;
import com.boredream.springbootdemo.entity.User;
import com.boredream.springbootdemo.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@Configuration
public class AuthUserService implements UserDetailsService {

    @Autowired
    UserRepository repository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 此处应从数据库加载用户信息，为简便起见，直接创建一个用户
        // password的值：$2a$10$EmsokMb6Vkav7m61kY0PtO.ZCLe0h.uJqVAZW7YYBpSUxd/DMkZuG，
        // 是明文123456使用BCryptPasswordEncoder加密的值
        User user = new User();
        user.setUsername(username);
        user.setPassword("$2a$10$EmsokMb6Vkav7m61kY0PtO.ZCLe0h.uJqVAZW7YYBpSUxd/DMkZuG");
        return new AuthUser(user);
    }
}