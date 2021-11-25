package com.boredream.springbootdemo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.boredream.springbootdemo.entity.Diary;
import com.boredream.springbootdemo.mapper.DiaryMapper;
import com.boredream.springbootdemo.service.IDiaryService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 日记 服务实现类
 * </p>
 *
 * @author boredream
 * @since 2021-11-24
 */
@Service
public class DiaryServiceImpl extends ServiceImpl<DiaryMapper, Diary> implements IDiaryService {

    @Override
    public Page<Diary> queryByPage(Page<Diary> page, QueryWrapper<Diary> wrapper) {
        return getBaseMapper().queryByPage(page, wrapper);
    }
}
