package com.boredream.springbootdemo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.boredream.springbootdemo.entity.TodoGroup;
import com.boredream.springbootdemo.mapper.TodoGroupMapper;
import com.boredream.springbootdemo.service.ITodoGroupService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 清单组 服务实现类
 * </p>
 *
 * @author boredream
 * @since 2021-11-24
 */
@Service
public class TodoGroupServiceImpl extends ServiceImpl<TodoGroupMapper, TodoGroup> implements ITodoGroupService {

    @Override
    public List<TodoGroup> getTodoGroupListWithCount(QueryWrapper<TodoGroup> wrapper) {
        return getBaseMapper().getTodoGroupListWithCount(wrapper);
    }

}
