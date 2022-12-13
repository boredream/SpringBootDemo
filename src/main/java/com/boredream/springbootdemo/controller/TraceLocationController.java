package com.boredream.springbootdemo.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.boredream.springbootdemo.entity.TraceLocation;
import com.boredream.springbootdemo.entity.dto.ResponseDTO;
import com.boredream.springbootdemo.service.ITraceLocationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 轨迹点信息 前端控制器
 * </p>
 *
 * @author boredream
 * @since 2022-12-13
 */
@RestController
@RequestMapping("/trace_location")
@Api(tags = {"轨迹点信息"})
public class TraceLocationController {

    @Autowired
    private ITraceLocationService service;

    @ApiOperation(value = "查询轨迹点信息")
    @GetMapping("/{id}")
    public ResponseDTO<List<TraceLocation>> queryByGroupId(@PathVariable("id") Long groupId) {
        List<TraceLocation> todoList = service.list(new QueryWrapper<TraceLocation>()
                .eq("trace_record_id", groupId)
                .orderByAsc("time"));
        return ResponseDTO.success(todoList);
    }

    @ApiOperation(value = "添加轨迹点信息")
    @PostMapping
    public ResponseDTO<Boolean> add(@RequestBody @Validated TraceLocation body) {
        return ResponseDTO.success(service.save(body));
    }

    @ApiOperation(value = "修改轨迹点信息")
    @PutMapping("/{id}")
    public ResponseDTO<Boolean> update(@PathVariable("id") Long id, @RequestBody @Validated TraceLocation body) {
        body.setId(id);
        return ResponseDTO.success(service.updateById(body));
    }

    @ApiOperation(value = "删除轨迹点信息")
    @DeleteMapping("/{id}")
    public ResponseDTO<Boolean> delete(@PathVariable("id") Long id) {
        return ResponseDTO.success(service.removeById(id));
    }

}
