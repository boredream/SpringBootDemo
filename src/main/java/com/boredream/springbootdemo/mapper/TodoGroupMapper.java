package com.boredream.springbootdemo.mapper;

import com.boredream.springbootdemo.entity.TodoGroup;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

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

}
