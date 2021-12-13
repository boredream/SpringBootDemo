package com.boredream.springbootdemo.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
@ApiModel(value="VerifyCode对象", description="验证码")
public class VerifyCode {

    public static final int VERIFY_TYPE_REGISTER_OR_LOGIN = 0;
    public static final int VERIFY_TYPE_FORGET_PASSWORD = 1;

    @ApiModelProperty(value = "名称")
    @NotNull(message = "手机号必须填")
    @Pattern(regexp = "^[1][0-9]{10}$", message = "请输入11位手机号") // 手机号
    private String phone;

    @NotNull(message = "验证类型")
    private Integer verifyType;

    private Long expire;

}