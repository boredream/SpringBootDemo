package com.boredream.springbootdemo.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * <p>
 * 清单组
 * </p>
 *
 * @author boredream
 * @since 2021-11-24
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="TodoGroup对象", description="清单组")
public class TodoGroup extends Belong2UserEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "名称")
    private String name;

    @TableField(exist = false)
    @ApiModelProperty(value = "包含的所有清单")
    private List<Todo> todoList;

}
