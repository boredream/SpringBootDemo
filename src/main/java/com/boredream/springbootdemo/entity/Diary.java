package com.boredream.springbootdemo.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 日记
 * </p>
 *
 * @author boredream
 * @since 2021-11-24
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="Diary对象", description="日记")
public class Diary extends Belong2UserEntity {

    @ApiModelProperty(value = "文字内容")
    private String content;

    @ApiModelProperty(value = "日记日期")
    private String diaryDate;

    @ApiModelProperty(value = "图片")
    private String images;

}
