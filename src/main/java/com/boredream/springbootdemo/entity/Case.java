package com.boredream.springbootdemo.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 案例
 * </p>
 *
 * @author boredream
 * @since 2024-09-23
 */
@Getter
@Setter
@ApiModel(value = "Case对象", description = "案例")
@TableName("talk_case")
public class Case extends BaseEntity {

    @ApiModelProperty("所属用户")
    private Long userId;

    @TableField(exist = false)
    @ApiModelProperty("索引")
    private Integer caseIndex;

    @ApiModelProperty("类型 1-评估 2-咨询")
    private Integer type;

    @ApiModelProperty("文件地址")
    private String fileUrl;

    @ApiModelProperty("AI解析结果")
    private String aiResult;

    @ApiModelProperty("访客id")
    private Long visitorId;

    @TableField(exist = false)
    @ApiModelProperty("访问者")
    private Visitor visitor;

    @ApiModelProperty("本次对话日期")
    private Long contactTime;

}
