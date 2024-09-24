package com.boredream.springbootdemo.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * <p>
 * 案例详情
 * </p>
 *
 * @author boredream
 * @since 2024-09-24
 */
@Getter
@Setter
@TableName("talk_case_detail")
@ApiModel(value = "TalkCaseDetail对象", description = "案例详情")
public class TalkCaseDetail extends BaseEntity {

    @ApiModelProperty("所属案例")
    private Long caseId;

    @ApiModelProperty("解析结果类型 result_1/2/3/4...")
    private String resultType;

    @ApiModelProperty("内容")
    private String aiResult;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;


}
