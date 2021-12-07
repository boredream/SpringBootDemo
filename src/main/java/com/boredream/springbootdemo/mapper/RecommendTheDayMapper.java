package com.boredream.springbootdemo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.boredream.springbootdemo.entity.RecommendTheDay;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 纪念日推荐 Mapper 接口
 * </p>
 *
 * @author boredream
 * @since 2021-12-07
 */
@Mapper
public interface RecommendTheDayMapper extends BaseMapper<RecommendTheDay> {

}
