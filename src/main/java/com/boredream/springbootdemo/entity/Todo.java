package com.boredream.springbootdemo.entity;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author boredream
 * @since 2021-09-14
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class Todo implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Integer id;

    private String detail;

    private String name;

    private String notifyDate;

    private String todoDate;

    private String type;


}
