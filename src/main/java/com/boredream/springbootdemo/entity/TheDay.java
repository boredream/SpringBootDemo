package com.boredream.springbootdemo.entity;

import com.boredream.springbootdemo.entity.BaseEntity;
import java.time.LocalDateTime;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 纪念日
 * </p>
 *
 * @author boredream
 * @since 2021-09-28
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="TheDay对象", description="纪念日")
public class TheDay extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "名称")
    private String name;

    @ApiModelProperty(value = "纪念日期")
    private String theDayDate;

    @ApiModelProperty(value = "提醒日期")
    private String notifyDate;

    @ApiModelProperty(value = "详情")
    private String detail;

    @ApiModelProperty(value = "图片")
    private String images;

}
