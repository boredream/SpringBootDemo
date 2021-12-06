package com.boredream.springbootdemo.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 意见反馈
 * </p>
 *
 * @author boredream
 * @since 2021-12-06
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="FeedBack对象", description="意见反馈")
public class FeedBack extends Belong2UserEntity {

    @ApiModelProperty(value = "描述")
    private String detail;

    @ApiModelProperty(value = "图片")
    private String images;


}
