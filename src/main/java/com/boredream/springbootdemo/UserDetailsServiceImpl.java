package com.boredream.springbootdemo;

import com.boredream.springbootdemo.entity.UserInfo;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@Configuration
public class UserDetailsServiceImpl implements UserDetailsService {

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 模拟一个用户，替代数据库获取逻辑
        UserInfo user = new UserInfo();
        user.setUsername(username);
        // user.setPassword(SecurityUtils.encryptPassword("123456"));
        // 输出加密后的密码
        System.out.println(user.getPassword());

        return new User(username, user.getPassword(),
                AuthorityUtils.commaSeparatedStringToAuthorityList("admin"));
    }
}