package com.boredream.springbootdemo.entity.dto;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class LoginRequestDTO {

    @SuppressWarnings("unused")
    private static final Logger log = LoggerFactory.getLogger(LoginRequestDTO.class);

    @NotNull(message = "账号必须填")
    @Pattern(regexp = "^[1][0-9]{10}$", message = "账号请输入11位手机号") // 手机号
    private String username;

    @NotNull(message = "密码必须填")
    @Size(min = 6, max = 16, message = "密码6~16位")
    private String password;


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}