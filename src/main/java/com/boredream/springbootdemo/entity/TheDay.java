package com.boredream.springbootdemo.entity;

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
public class TheDay extends Belong2UserEntity {

    /**
     * 提醒方式 累计天数
     */
    public static final int NOTIFY_TYPE_TOTAL_COUNT = 1;

    /**
     * 提醒方式 按年倒计天数
     */
    public static final int NOTIFY_TYPE_YEAR_COUNT_DOWN = 2;

    @ApiModelProperty(value = "名称")
    private String name;

    @ApiModelProperty(value = "纪念日期")
    private String theDayDate;

    @ApiModelProperty(value = "提醒方式")
    private Integer notifyType;

}
