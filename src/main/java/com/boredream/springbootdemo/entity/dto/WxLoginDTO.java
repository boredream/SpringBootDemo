package com.boredream.springbootdemo.entity.dto;

import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.constraints.NotNull;

@Data
public class WxLoginDTO {

    @SuppressWarnings("unused")
    private static final Logger log = LoggerFactory.getLogger(WxLoginDTO.class);

    @NotNull(message = "code 为空")
    private String code;

}