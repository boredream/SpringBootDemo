package com.boredream.springbootdemo.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.HashMap;
import java.util.List;

@Data
public class CaseAiResultResponse {

    @ApiModelProperty("AI解析结果 result_1/2/3/4: xxxx")
    private List<HashMap<String, String>> label;

    private Integer code;
    private String msg;

}
