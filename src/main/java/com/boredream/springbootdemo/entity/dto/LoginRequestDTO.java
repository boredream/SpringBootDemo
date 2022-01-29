package com.boredream.springbootdemo.entity.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
public class LoginRequestDTO extends SetPasswordRequestDTO {

    @NotNull(message = "账号必须填")
    @Pattern(regexp = "^[1][0-9]{10}$", message = "账号请输入11位手机号") // 手机号
    private String username;

}