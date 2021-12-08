package com.boredream.springbootdemo.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class RecommendDataEntity extends Belong2UserEntity {

    @ApiModelProperty(value = "推荐数据id")
    private Long recommendId;

    @ApiModelProperty(value = "推荐数据的状态，0普通，1隐藏，2删除")
    private int recommendStatus;

    /**
     * 提交过数据
     */
    public boolean hasUpdated() {
        return false;
    }

}
