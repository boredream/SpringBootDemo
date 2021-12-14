package com.boredream.springbootdemo.entity.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class VerifyCodeDTO extends PhoneDTO {

    @NotNull(message = "验证码必须填")
    private String code;

}