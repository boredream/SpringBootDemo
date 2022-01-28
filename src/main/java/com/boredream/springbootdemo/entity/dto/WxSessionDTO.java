package com.boredream.springbootdemo.entity.dto;

import com.boredream.springbootdemo.entity.BaseWxResponse;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Data
public class WxSessionDTO extends BaseWxResponse {

    @SuppressWarnings("unused")
    private static final Logger log = LoggerFactory.getLogger(WxSessionDTO.class);

    @ApiModelProperty(value = "用户唯一标识")
    private String openid;

    @ApiModelProperty(value = "会话密钥")
    private String session_key;

    @ApiModelProperty(value = "用户在开放平台的唯一标识符，若当前小程序已绑定到微信开放平台帐号下会返回，详见 UnionID 机制说明。")
    private String unionid;
}