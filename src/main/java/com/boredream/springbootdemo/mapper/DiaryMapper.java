package com.boredream.springbootdemo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.boredream.springbootdemo.entity.Diary;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 日记 Mapper 接口
 * </p>
 *
 * @author boredream
 * @since 2021-11-24
 */
@Mapper
public interface DiaryMapper extends BaseMapper<Diary> {

}
