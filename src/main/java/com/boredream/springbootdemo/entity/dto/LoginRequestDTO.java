package com.boredream.springbootdemo.entity.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
public class LoginRequestDTO {

    @NotNull(message = "账号必须填")
    @Pattern(regexp = "^[1][0-9]{10}$", message = "账号请输入11位手机号") // 手机号
    private String username;

    @NotNull(message = "密码必须填")
    @Size(min = 6, max = 16, message = "密码6~16位")
    private String password;

}