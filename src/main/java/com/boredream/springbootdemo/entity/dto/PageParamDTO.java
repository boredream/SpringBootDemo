package com.boredream.springbootdemo.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 分页基础参数
 */
@Data
public class PageParamDTO {

    @NotNull(message = "分页参数不能为空")
    @ApiModelProperty(value = "页码(不能为空)", example = "1")
    protected Integer page;

    @NotNull(message = "每页数量不能为空")
    @ApiModelProperty(value = "每页数量(不能为空)", example = "10")
    protected Integer size;

}
