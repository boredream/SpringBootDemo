package com.boredream.springbootdemo.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 清单
 * </p>
 *
 * @author boredream
 * @since 2021-09-18
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="Todo对象", description="清单")
public class Todo extends Belong2UserEntity {

    @TableField(exist = false)
    @ApiModelProperty(value = "所属清单组名称")
    private String todoGroupName;

    @ApiModelProperty(value = "所属清单组id")
    private Long todoGroupId;

    @ApiModelProperty(value = "已完成")
    private boolean done;

    @ApiModelProperty(value = "名称")
    private String name;

    @ApiModelProperty(value = "完成日期")
    private String doneDate;

    @ApiModelProperty(value = "描述")
    private String detail;

    @ApiModelProperty(value = "图片")
    private String images;


}
