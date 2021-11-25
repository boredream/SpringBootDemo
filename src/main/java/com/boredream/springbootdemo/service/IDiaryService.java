package com.boredream.springbootdemo.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.boredream.springbootdemo.entity.Diary;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 日记 服务类
 * </p>
 *
 * @author boredream
 * @since 2021-11-24
 */
public interface IDiaryService extends IService<Diary> {

    Page<Diary> queryByPage(Page<Diary> page, QueryWrapper<Diary> wrapper);

}
