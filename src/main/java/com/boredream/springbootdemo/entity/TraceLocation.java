package com.boredream.springbootdemo.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 轨迹点信息
 * </p>
 *
 * @author boredream
 * @since 2022-12-13
 */
@Getter
@Setter
@TableName("trace_location")
@ApiModel(value = "TraceLocation对象", description = "轨迹点信息")
public class TraceLocation extends BaseEntity {

    @ApiModelProperty("所属轨迹id")
    private Long traceRecordId;

    @ApiModelProperty("时间")
    private Long time;

    @ApiModelProperty("经度")
    private Double longitude;

    @ApiModelProperty("纬度")
    private Double latitude;


}
