package com.boredream.springbootdemo.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class BaseWxResponse {

    @ApiModelProperty(value = "错误码")
    private int errcode;

    @ApiModelProperty(value = "错误信息")
    private String errmsg;

}
