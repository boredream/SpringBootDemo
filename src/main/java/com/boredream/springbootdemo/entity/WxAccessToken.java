package com.boredream.springbootdemo.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Data
public class WxAccessToken extends BaseWxResponse {

    @SuppressWarnings("unused")
    private static final Logger log = LoggerFactory.getLogger(WxAccessToken.class);

    @ApiModelProperty(value = "获取到的凭证")
    private String access_token;

    @ApiModelProperty(value = "凭证有效时间，单位：秒。")
    private long expires_in;
}