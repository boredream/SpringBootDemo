package com.boredream.springbootdemo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    /**
     * 描述:
     * http方式走 Spring Security 过滤器链，在过滤器链中，给请求放行，而web方式是不走 Spring Security 过滤器链。
     * 通常http方式用于请求的放行和限制，web方式用于放行静态资源
     **/
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                // 配置除了某些接口之外都需要认证
                .antMatchers("/login").permitAll().anyRequest().authenticated()
                // 基于token，不需要session
                .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                // 禁用跨站伪造
                .and().csrf().disable();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                .withUser("admin").password(passwordEncoder().encode("12345")).roles("ADMIN")
                .and()
                .withUser("user").password(passwordEncoder().encode("12345")).roles("USER");

    }

    //    /**
//     * 描述：设置授权处理相关的具体类以及加密方式
//     */
//    @Override
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
//        // 设置不隐藏 未找到用户异常
//        provider.setHideUserNotFoundExceptions(true);
//        // 用户认证service - 查询数据库的逻辑
//        provider.setUserDetailsService(userDetailsService());
//        // 设置密码加密算法
//        provider.setPasswordEncoder(passwordEncoder());
//        auth.authenticationProvider(provider);
//    }

//    /**
//     * 描述: 通过自定义的UserDetailsService 来实现查询数据库用户数据
//     **/
//    @Override
//    @Bean
//    protected UserDetailsService userDetailsService() {
//        return new UserDetailsServiceImpl();
//    }

    /**
     * 描述: 密码加密算法 BCrypt 推荐使用
     **/
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

//    /**
//     * 描述: 注入AuthenticationManager管理器
//     **/
//    @Override
//    @Bean
//    public AuthenticationManager authenticationManager() throws Exception {
//        return super.authenticationManager();
//    }
}