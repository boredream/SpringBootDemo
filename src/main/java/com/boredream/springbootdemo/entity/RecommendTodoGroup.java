package com.boredream.springbootdemo.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 清单组推荐
 * </p>
 *
 * @author boredream
 * @since 2021-12-07
 */
@Getter
@Setter
@TableName("recommend_todo_group")
@ApiModel(value = "RecommendTodoGroup对象", description = "清单组推荐")
public class RecommendTodoGroup extends BaseEntity {

    @ApiModelProperty("名称")
    private String name;

}
