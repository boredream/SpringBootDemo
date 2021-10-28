package com.boredream.springbootdemo.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.boredream.springbootdemo.entity.dto.PageResultDTO;
import com.boredream.springbootdemo.entity.dto.ResponseDTO;
import com.boredream.springbootdemo.entity.Todo;
import com.boredream.springbootdemo.entity.dto.TodoQueryDTO;
import com.boredream.springbootdemo.service.ITodoService;
import com.boredream.springbootdemo.utils.PageUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 待办事项 前端控制器
 * </p>
 *
 * @author boredream
 * @since 2021-09-18
 */
@RestController
@RequestMapping("/todo")
@Api(tags = {"待办事项" })
public class TodoController {

    // TODO: chunyang 2021/9/18 controller 不应该携带具体的mybatis引用类如page

    @Autowired
    private ITodoService service;

    @ApiOperation(value = "分页查询待办事项")
    @GetMapping("/page")
    public ResponseDTO<PageResultDTO<Todo>> queryByPage(TodoQueryDTO dto, Long curUserId) {
        // TODO: chunyang 2021/9/28 条件自动化添加
        QueryWrapper<Todo> wrapper = new QueryWrapper<Todo>()
                .eq("type", dto.getType())
                .eq("user_id", curUserId);
        Page<Todo> page = PageUtil.convert2QueryPage(dto);
        Page<Todo> resultDto = service.page(page, wrapper);
        return ResponseDTO.success(PageUtil.convert2PageResult(resultDto));
    }

    @ApiOperation(value = "添加待办事项")
    @PostMapping
    public ResponseDTO<Boolean> add(@RequestBody @Validated Todo params, Long curUserId) {
        params.setUserId(curUserId);
        return ResponseDTO.success(service.save(params));
    }

    @ApiOperation(value = "修改待办事项")
    @PutMapping("/{id}")
    public ResponseDTO<Boolean> update(@PathVariable("id") Long id, @RequestBody @Validated Todo params) {
        params.setId(id);
        return ResponseDTO.success(service.updateById(params));
    }

    @ApiOperation(value = "删除待办事项")
    @DeleteMapping("/{id}")
    public ResponseDTO<Boolean> delete(@PathVariable("id") Long id) {
        return ResponseDTO.success(service.removeById(id));
    }

}
