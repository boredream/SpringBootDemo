package com.boredream.springbootdemo.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class TraceRecordQueryDTO extends PageParamDTO {

    @ApiModelProperty(value = "查询日期(年-月)", example = "2021-12")
    private String queryDate;

}
