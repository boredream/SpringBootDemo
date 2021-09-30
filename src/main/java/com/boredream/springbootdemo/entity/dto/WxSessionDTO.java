package com.boredream.springbootdemo.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Data
public class WxSessionDTO {

    @SuppressWarnings("unused")
    private static final Logger log = LoggerFactory.getLogger(WxSessionDTO.class);

    @ApiModelProperty(value = "用户唯一标识")
    private String openid;

    @ApiModelProperty(value = "会话密钥")
    private String session_key;

    @ApiModelProperty(value = "用户在开放平台的唯一标识符，若当前小程序已绑定到微信开放平台帐号下会返回，详见 UnionID 机制说明。")
    private String unionid;

    @ApiModelProperty(value = "错误码")
    private int errcode;

    @ApiModelProperty(value = "错误信息")
    private String errmsg;
}