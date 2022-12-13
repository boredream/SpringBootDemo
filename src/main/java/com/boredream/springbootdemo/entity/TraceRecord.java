package com.boredream.springbootdemo.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 轨迹信息
 * </p>
 *
 * @author boredream
 * @since 2022-12-13
 */
@Getter
@Setter
@TableName("trace_record")
@ApiModel(value = "TraceRecord对象", description = "轨迹信息")
public class TraceRecord extends Belong2UserEntity {

    @ApiModelProperty("名称")
    private String name;

    @ApiModelProperty("描述")
    private String detail;

    @TableField(exist = false)
    @ApiModelProperty("轨迹列表")
    private String traceListStr;

}
