package com.boredream.springbootdemo.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.boredream.springbootdemo.entity.Todo;

import java.util.List;

/**
 * <p>
 * 清单 服务类
 * </p>
 *
 * @author boredream
 * @since 2021-09-18
 */
public interface ITodoService extends IService<Todo> {

    List<Todo> getTodoListWithGroup(QueryWrapper wrapper);

}
