package com.boredream.springbootdemo.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 文件上传凭证
 */
@Data
public class FileUploadPolicyDTO {

    @ApiModelProperty(value = "口令")
    protected String token;

    @ApiModelProperty(value = "上传地址")
    protected String uploadHost;

    @ApiModelProperty(value = "下载地址")
    protected String downloadHost;

}
