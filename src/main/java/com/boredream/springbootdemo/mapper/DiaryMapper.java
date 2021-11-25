package com.boredream.springbootdemo.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.boredream.springbootdemo.entity.Diary;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

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

    Page<Diary> queryByPage(Page<Diary> page, @Param(Constants.WRAPPER) QueryWrapper<Diary> wrapper);

    List<Diary> queryByMonth(@Param(Constants.WRAPPER) QueryWrapper<Diary> wrapper);
}
