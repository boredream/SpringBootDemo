package com.boredream.springbootdemo.config;

import com.boredream.springbootdemo.auth.JwtAuthError;
import com.boredream.springbootdemo.auth.JwtAuthFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity // 添加security过滤器
@EnableGlobalMethodSecurity(prePostEnabled = true) // 可以在controller方法上配置权限
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    // 加载用户信息
    @Autowired
    private UserDetailsService myUserDetailsService;

    // 权限不足错误信息处理，包含认证错误与鉴权错误处理
    @Autowired
    private JwtAuthError myAuthErrorHandler;

    // 密码明文加密方式配置
    @Bean
    public PasswordEncoder myEncoder() {
        return new BCryptPasswordEncoder();
    }

    // jwt校验过滤器，从http头部Authorization字段读取token并校验
    @Bean
    public JwtAuthFilter myAuthFilter() throws Exception {
        return new JwtAuthFilter();
    }

    // 获取AuthenticationManager（认证管理器），可以在其他地方使用
    @Bean(name = "authenticationManagerBean")
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    // 认证用户时用户信息加载配置，注入myUserDetailsService
    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(myUserDetailsService);
    }

    // 配置http，包含权限配置
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http

                // 由于使用的是JWT，我们这里不需要csrf
                .csrf()
                .disable()

                // 基于token，所以不需要session
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()

                // 设置myUnauthorizedHandler处理认证失败、鉴权失败
                .exceptionHandling()
                .authenticationEntryPoint(myAuthErrorHandler)
                .accessDeniedHandler(myAuthErrorHandler)
                .and()

                // 设置权限
                .authorizeRequests()

                // 不用登陆
                .antMatchers(
                        "/hello",
                        "/user/register",
                        "/user/login",
                        "/user/wxlogin",
                        "/user/sendVerifyCode",
                        "/user/loginWithVerifyCode",
                        "/swagger-ui/*", "/v2/**", "/swagger-resources/**", "/swagger-ui.html", "/webjars/**")
                .permitAll()

                // 除上面外的所有请求都需要登录
                .anyRequest()
                .authenticated();

        // 添加JWT过滤器，JWT过滤器在用户名密码认证过滤器之前
        http.addFilterBefore(myAuthFilter(), UsernamePasswordAuthenticationFilter.class);

        // 禁用缓存
//      http.headers().cacheControl();
    }
}