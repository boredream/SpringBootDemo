package com.boredream.springbootdemo.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.boredream.springbootdemo.entity.Todo;
import com.boredream.springbootdemo.entity.TodoGroup;
import com.boredream.springbootdemo.entity.dto.ResponseDTO;
import com.boredream.springbootdemo.service.ITodoGroupService;
import com.boredream.springbootdemo.service.ITodoService;
import com.boredream.springbootdemo.service.IWxService;
import com.boredream.springbootdemo.service.impl.WxServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 清单组 前端控制器
 * </p>
 *
 * @author boredream
 * @since 2021-11-24
 */
@RestController
@RequestMapping("/todo_group")
@EnableTransactionManagement
@Api(tags = {"清单组"})
public class TodoGroupController extends BaseController {

    @Autowired
    private ITodoGroupService service;

    @Autowired
    private ITodoService todoService;

    @Autowired
    private WxServiceImpl wxService;

    @ApiOperation(value = "查询所有清单组，不包括组内所有清单")
    @GetMapping("/only_group")
    public ResponseDTO<List<TodoGroup>> queryOnlyGroup(Long curUserId) {
        QueryWrapper<TodoGroup> wrapper = genUserQuery(curUserId);
        return ResponseDTO.success(service.getTodoGroupListWithCount(wrapper));
    }

    @ApiOperation(value = "查询所有清单组，包括组内所有清单")
    @GetMapping()
    public ResponseDTO<List<TodoGroup>> query(Long curUserId) {
        QueryWrapper<TodoGroup> wrapper = genUserQuery(curUserId);
        List<TodoGroup> list = service.list(wrapper);
        for (TodoGroup group : list) {
            Long groupId = group.getId();
            List<Todo> todoList = todoService.list(
                    new QueryWrapper<Todo>()
                            .eq("todo_group_id", groupId)
                            .orderByAsc("done_date"));
            group.setTodoList(todoList);
        }
        return ResponseDTO.success(list);
    }

    @ApiOperation(value = "添加清单组")
    @PostMapping
    public ResponseDTO<Boolean> add(@RequestBody @Validated TodoGroup body, Long curUserId) {
        boolean checkMsgSec = wxService.checkMsgSec(body.getPlatform(), curUserId, IWxService.SCENE_SOCIAL, body.getName());
        if(!checkMsgSec) {
            return ResponseDTO.errorMsgSecCheck();
        }
        body.setUserId(curUserId);
        return ResponseDTO.success(service.save(body));
    }

    @ApiOperation(value = "修改清单组")
    @PutMapping("/{id}")
    public ResponseDTO<Boolean> update(@PathVariable("id") Long id, @RequestBody @Validated TodoGroup body, Long curUserId) {
        boolean checkMsgSec = wxService.checkMsgSec(body.getPlatform(), curUserId, IWxService.SCENE_SOCIAL, body.getName());
        if(!checkMsgSec) {
            return ResponseDTO.errorMsgSecCheck();
        }
        body.setId(id);
        return ResponseDTO.success(service.updateById(body));
    }

    @ApiOperation(value = "删除清单组")
    @DeleteMapping("/{id}")
    @Transactional
    public ResponseDTO<Boolean> delete(@PathVariable("id") Long id) {
        // 同时删除组内所有清单
        return ResponseDTO.success(service.removeById(id));
    }

}
