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

    /**
     * 类型 评估
     */
    public static final int TYPE_ASSESSMENT = 1;

    /**
     * 类型 咨询
     */
    public static final int TYPE_CONSULT = 2;

    /**
     * AI解析状态 闲置
     */
    public static final int AI_PARSE_STATUS_IDLE = 0;

    /**
     * AI解析状态 解析中
     */
    public static final int AI_PARSE_STATUS_PARSING = 1;

    /**
     * AI解析状态 解析成功
     */
    public static final int AI_PARSE_STATUS_SUCCESS = 2;

    /**
     * AI解析状态 解析失败
     */
    public static final int AI_PARSE_STATUS_FAIL = 3;


    @ApiModelProperty("删除标记 1=删除")
    private Integer deleteFlag;

    @ApiModelProperty("所属用户")
    private Long userId;

    @ApiModelProperty("索引")
    private Integer caseIndex;

    @ApiModelProperty("类型 1-评估 2-咨询")
    private Integer type;

    @ApiModelProperty("文件地址")
    private String fileUrl;

    @ApiModelProperty("主题")
    private String topic;

    @ApiModelProperty("AI解析状态 0-闲置 1-解析中 2-解析成功 3-解析失败")
    private Integer aiParseStatus;

    @ApiModelProperty("访客id")
    private Long visitorId;

    @TableField(exist = false)
    @ApiModelProperty("访问者")
    private Visitor visitor;

    @ApiModelProperty("本次对话日期")
    private Long contactTime;

}
