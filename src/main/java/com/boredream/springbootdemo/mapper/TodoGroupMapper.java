package com.boredream.springbootdemo.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.boredream.springbootdemo.entity.TodoGroup;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 清单组 Mapper 接口
 * </p>
 *
 * @author boredream
 * @since 2021-11-24
 */
@Mapper
public interface TodoGroupMapper extends BaseMapper<TodoGroup> {

    List<TodoGroup> getTodoGroupListWithCount(@Param(Constants.WRAPPER) QueryWrapper wrapper);

}
