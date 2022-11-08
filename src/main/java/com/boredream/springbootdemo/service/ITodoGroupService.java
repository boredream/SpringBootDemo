package com.boredream.springbootdemo.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.boredream.springbootdemo.entity.TodoGroup;

import java.util.List;

/**
 * <p>
 * 清单组 服务类
 * </p>
 *
 * @author boredream
 * @since 2021-11-24
 */
public interface ITodoGroupService extends IService<TodoGroup> {

    List<TodoGroup> getTodoGroupListWithCount(QueryWrapper<TodoGroup> wrapper);

}
