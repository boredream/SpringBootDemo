package com.boredream.springbootdemo.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class Belong2UserEntity extends BaseEntity {

    @ApiModelProperty(value = "所属用户id")
    private Long userId;

}
