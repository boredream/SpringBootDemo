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

    // 同步用字段 start
    @ApiModelProperty(value = "前端本地数据库id")
    private Long dbId;

    @ApiModelProperty(value = "同步数据状态")
    private Boolean synced;

    @ApiModelProperty(value = "同步数据时间戳")
    private Long syncTimestamp;
    // 同步用字段 end

    @ApiModelProperty("名称")
    private String name;

    @ApiModelProperty("描述")
    private String detail;

    @TableField(exist = false)
    @ApiModelProperty("轨迹列表")
    private String traceListStr;

    @ApiModelProperty("开始时间")
    private Long startTime;

    @ApiModelProperty("结束时间")
    private Long endTime;

    @ApiModelProperty("轨迹总长(米)")
    private Integer distance;

}
