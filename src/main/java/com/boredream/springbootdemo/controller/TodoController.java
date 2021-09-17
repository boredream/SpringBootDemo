package com.boredream.springbootdemo.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.boredream.springbootdemo.entity.PageResultDTO;
import com.boredream.springbootdemo.entity.ResponseDTO;
import com.boredream.springbootdemo.entity.Todo;
import com.boredream.springbootdemo.service.ITodoService;
import com.boredream.springbootdemo.utils.PageUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 前端控制器
 *
 * @author boredream
 * @since 2021-09-14
 */
@RestController
@RequestMapping("/todo" )
@Api(tags = {"待办事项" })
public class TodoController {

    @Autowired
    private ITodoService todoService;

    @ApiOperation(value = "分页查询待办事项" )
    @GetMapping("/page" )
    public ResponseDTO<PageResultDTO<Todo>> queryByPage(Page<Todo> params) {
        //根据条件分页查询
        Page<Todo> resultDto = todoService.page(params);
        //封装分页返回对象
        return ResponseDTO.succData(PageUtil.convert2PageResult(resultDto));
    }

    @ApiOperation(value = "添加待办事项" )
    @PostMapping
    public ResponseDTO<Boolean> add(@RequestBody @Validated Todo params) {
        return ResponseDTO.succData(todoService.save(params));
    }

    @ApiOperation(value = "修改待办事项" )
    @PutMapping
    public ResponseDTO<Boolean> update(@RequestBody @Validated Todo params) {
        return ResponseDTO.succData(todoService.updateById(params));
    }

    @ApiOperation(value = "删除待办事项" )
    @DeleteMapping
    public ResponseDTO<Boolean> delete(@RequestParam Integer id) {
        return ResponseDTO.succData(todoService.removeById(id));
    }

}

