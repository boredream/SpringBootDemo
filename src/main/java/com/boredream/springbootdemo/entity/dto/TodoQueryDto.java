package com.boredream.springbootdemo.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class TodoQueryDto extends PageParamDTO {

    @ApiModelProperty(value = "类型")
    private String type;

}
