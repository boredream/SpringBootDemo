package com.boredream.springbootdemo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.boredream.springbootdemo.entity.TheDay;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 纪念日 Mapper 接口
 * </p>
 *
 * @author boredream
 * @since 2021-09-28
 */
@Mapper
public interface TheDayMapper extends BaseMapper<TheDay> {

}
