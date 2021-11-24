package com.boredream.springbootdemo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.boredream.springbootdemo.entity.Todo;
import com.boredream.springbootdemo.mapper.TodoMapper;
import com.boredream.springbootdemo.service.ITodoService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 清单 服务实现类
 * </p>
 *
 * @author boredream
 * @since 2021-09-18
 */
@Service
public class TodoServiceImpl extends ServiceImpl<TodoMapper, Todo> implements ITodoService {

    @Override
    public List<Todo> getTodoListWithGroup(QueryWrapper wrapper) {
        return getBaseMapper().getTodoListWithGroup(wrapper);
    }
}
