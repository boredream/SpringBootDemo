package com.boredream.springbootdemo.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class TalkCaseDetailQueryDTO {

    @ApiModelProperty(value = "案例id")
    private String caseId;

    @ApiModelProperty(value = "类型", example = "result_1")
    private String resultType;

}
