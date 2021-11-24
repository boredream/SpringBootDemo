package com.boredream.springbootdemo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.boredream.springbootdemo.entity.Todo;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 待办事项 Mapper 接口
 * </p>
 *
 * @author boredream
 * @since 2021-09-18
 */
@Mapper
public interface TodoMapper extends BaseMapper<Todo> {

}
