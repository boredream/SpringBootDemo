package com.boredream.springbootdemo.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.boredream.springbootdemo.entity.Todo;
import com.boredream.springbootdemo.entity.dto.ResponseDTO;
import com.boredream.springbootdemo.service.ITodoService;
import com.boredream.springbootdemo.service.IWxService;
import com.boredream.springbootdemo.service.impl.WxServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 清单 前端控制器
 * </p>
 *
 * @author boredream
 * @since 2021-09-18
 */
@RestController
@RequestMapping("/todo")
@Api(tags = {"清单"})
public class TodoController extends BaseController {

    @Autowired
    private ITodoService service;

    @Autowired
    private WxServiceImpl wxService;

    @ApiOperation(value = "查询所有清单")
    @GetMapping("/{id}")
    public ResponseDTO<List<Todo>> queryByGroupId(@PathVariable("id") Long groupId) {
        List<Todo> todoList = service.list(new QueryWrapper<Todo>()
                .eq("todo_group_id", groupId)
                .orderByAsc("done_date"));
        return ResponseDTO.success(todoList);
    }

    @ApiOperation(value = "查询所有清单")
    @GetMapping()
    public ResponseDTO<List<Todo>> query(Long curUserId) {
        QueryWrapper<Todo> wrapper = genUserQuery("todo", curUserId);
        return ResponseDTO.success(service.getTodoListWithGroup(wrapper));
    }

    @ApiOperation(value = "添加清单")
    @PostMapping
    public ResponseDTO<Boolean> add(@RequestBody @Validated Todo params, Long curUserId) {
        boolean checkMsgSec = wxService.checkMsgSec(params.getPlatform(), curUserId, IWxService.SCENE_SOCIAL, params.getName());
        if(!checkMsgSec) {
            return ResponseDTO.errorMsgSecCheck();
        }
        params.setUserId(curUserId);
        return ResponseDTO.success(service.save(params));
    }

    @ApiOperation(value = "修改清单")
    @PutMapping("/{id}")
    public ResponseDTO<Boolean> update(@PathVariable("id") Long id, @RequestBody @Validated Todo params, Long curUserId) {
        boolean checkMsgSec = wxService.checkMsgSec(params.getPlatform(), curUserId, IWxService.SCENE_SOCIAL, params.getName());
        if(!checkMsgSec) {
            return ResponseDTO.errorMsgSecCheck();
        }
        params.setId(id);
        return ResponseDTO.success(service.updateById(params));
    }

    @ApiOperation(value = "删除清单")
    @DeleteMapping("/{id}")
    public ResponseDTO<Boolean> delete(@PathVariable("id") Long id) {
        return ResponseDTO.success(service.removeById(id));
    }

}
