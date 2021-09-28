package com.boredream.springbootdemo.utils;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.boredream.springbootdemo.entity.dto.PageParamDTO;
import com.boredream.springbootdemo.entity.dto.PageResultDTO;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 分页工具类
 *
 * @author GHQ
 * @date 2017-12-23 16:40
 */

public class PageUtil {

    public static <T> Page<T> convert2QueryPage(PageParamDTO baseDTO) {
        Page<T> page = new Page<>();
        page.setCurrent(baseDTO.getPage());
        page.setSize(baseDTO.getSize());
        return page;
    }

    public static <T> PageResultDTO<T> convert2PageResult(IPage<T> page) {
        PageResultDTO<T> result = new PageResultDTO<>();
        result.setCurrent(page.getCurrent());
        result.setSize(page.getSize());
        result.setTotal(page.getTotal());
        result.setPages(page.getPages());
        result.setRecords(page.getRecords());
        return result;
    }

}
