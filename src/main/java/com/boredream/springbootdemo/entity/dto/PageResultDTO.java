package com.boredream.springbootdemo.entity.dto;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * Page返回对象
 */
@Data
public class PageResultDTO<T> {

    /**
     * 当前页
     */
    @ApiModelProperty(value = "当前页")
    private Long current;

    /**
     * 每页的数量
     */
    @ApiModelProperty(value = "每页的数量")
    private Long size;

    /**
     * 总记录数
     */
    @ApiModelProperty(value = "总记录数")
    private Long total;

    /**
     * 总页数
     */
    @ApiModelProperty(value = "总页数")
    private Long pages;

    /**
     * 结果集
     */
    @ApiModelProperty(value = "结果集")
    private List<T> records;

}
