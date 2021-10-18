package com.boredream.springbootdemo.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 待办事项
 * </p>
 *
 * @author boredream
 * @since 2021-09-18
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="Todo对象", description="待办事项")
public class Todo extends Belong2UserEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "已完成")
    private boolean done;

    @ApiModelProperty(value = "名称")
    private String name;

    @ApiModelProperty(value = "类型")
    private String type;

    @ApiModelProperty(value = "待办日期")
    private String doneDate;

    @ApiModelProperty(value = "详情")
    private String detail;

    @ApiModelProperty(value = "图片")
    private String images;


}
