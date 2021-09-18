package com.boredream.springbootdemo.service.impl;

import com.boredream.springbootdemo.entity.Todo;
import com.boredream.springbootdemo.mapper.TodoMapper;
import com.boredream.springbootdemo.service.ITodoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 待办事项 服务实现类
 * </p>
 *
 * @author boredream
 * @since 2021-09-18
 */
@Service
public class TodoServiceImpl extends ServiceImpl<TodoMapper, Todo> implements ITodoService {

}
