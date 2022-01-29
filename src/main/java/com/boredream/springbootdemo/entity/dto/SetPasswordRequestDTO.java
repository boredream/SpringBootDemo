package com.boredream.springbootdemo.entity.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
public class SetPasswordRequestDTO {

    @NotNull(message = "密码必须填")
    @Pattern(regexp = "[0-9a-zA-Z]{6,16}", message = "密码必须是6至16位的数字或字母组成")
    @Size(min = 6, max = 16, message = "密码必须是6至16位的数字或字母组成")
    private String password;

}