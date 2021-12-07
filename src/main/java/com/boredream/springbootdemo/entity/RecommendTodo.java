package com.boredream.springbootdemo.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 清单推荐
 * </p>
 *
 * @author boredream
 * @since 2021-12-07
 */
@Getter
@Setter
@TableName("recommend_todo")
@ApiModel(value = "RecommendTodo对象", description = "清单推荐")
public class RecommendTodo extends BaseEntity {

    @ApiModelProperty("所属清单组名称")
    private String todoGroupName;

    @ApiModelProperty("已完成")
    private Boolean done;

    @ApiModelProperty("名称")
    private String name;

    @ApiModelProperty("完成日期")
    private String doneDate;

    @ApiModelProperty("描述")
    private String detail;

    @ApiModelProperty("图片")
    private String images;

}
