package com.boredream.springbootdemo.entity;

import com.boredream.springbootdemo.entity.BaseEntity;
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
public class Todo extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "名称")
    private String name;

    @ApiModelProperty(value = "类型")
    private String type;

    @ApiModelProperty(value = "代办日期")
    private String todoDate;

    @ApiModelProperty(value = "提醒日期")
    private String notifyDate;

    @ApiModelProperty(value = "详情")
    private String detail;


}
