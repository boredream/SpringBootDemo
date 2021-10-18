package com.boredream.springbootdemo.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class Belong2UserEntity extends BaseEntity {

    /**
     * 所属用户id
     */
    private Long userId;

}
