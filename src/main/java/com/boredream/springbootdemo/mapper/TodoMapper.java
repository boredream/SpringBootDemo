package com.boredream.springbootdemo.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.boredream.springbootdemo.entity.Todo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 清单 Mapper 接口
 * </p>
 *
 * @author boredream
 * @since 2021-09-18
 */
@Mapper
public interface TodoMapper extends BaseMapper<Todo> {

    List<Todo> getTodoListWithGroup(@Param(Constants.WRAPPER) QueryWrapper wrapper);

}
