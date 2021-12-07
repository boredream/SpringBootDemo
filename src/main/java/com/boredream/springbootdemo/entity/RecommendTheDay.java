package com.boredream.springbootdemo.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 纪念日推荐
 * </p>
 *
 * @author boredream
 * @since 2021-12-07
 */
@Getter
@Setter
@TableName("recommend_the_day")
@ApiModel(value = "RecommendTheDay对象", description = "纪念日推荐")
public class RecommendTheDay extends BaseEntity {

    @ApiModelProperty("名称")
    private String name;

    @ApiModelProperty("纪念日期")
    private String theDayDate;

    @ApiModelProperty("提醒方式")
    private Integer notifyType;

}
