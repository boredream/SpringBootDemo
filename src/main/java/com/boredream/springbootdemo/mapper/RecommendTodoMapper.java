package com.boredream.springbootdemo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.boredream.springbootdemo.entity.RecommendTodo;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 清单推荐 Mapper 接口
 * </p>
 *
 * @author boredream
 * @since 2021-12-07
 */
@Mapper
public interface RecommendTodoMapper extends BaseMapper<RecommendTodo> {

}
