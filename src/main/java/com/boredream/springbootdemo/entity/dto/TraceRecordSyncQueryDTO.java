package com.boredream.springbootdemo.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class TraceRecordSyncQueryDTO extends PageParamDTO {

    @ApiModelProperty(value = "同步时间戳")
    private Long localTimestamp;

}
