package com.boredream.springbootdemo.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.boredream.springbootdemo.entity.Diary;
import com.boredream.springbootdemo.entity.dto.DiaryQueryDTO;
import com.boredream.springbootdemo.entity.dto.PageResultDTO;
import com.boredream.springbootdemo.entity.dto.ResponseDTO;
import com.boredream.springbootdemo.service.IDiaryService;
import com.boredream.springbootdemo.utils.PageUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 日记 前端控制器
 * </p>
 *
 * @author boredream
 * @since 2021-11-24
 */
@RestController
@RequestMapping("/diary")
@Api(tags = {"日记"})
public class DiaryController extends BaseController {

    @Autowired
    private IDiaryService service;

    @ApiOperation(value = "分页查询日记")
    @GetMapping("/page")
    public ResponseDTO<PageResultDTO<Diary>> queryByPage(DiaryQueryDTO dto, Long curUserId) {
        QueryWrapper<Diary> wrapper = genUserQuery(curUserId);
        wrapper = wrapper.orderByDesc("diary_date");

        Page<Diary> page = PageUtil.convert2QueryPage(dto);
        Page<Diary> resultDto = service.queryByPage(page, wrapper);
        return ResponseDTO.success(PageUtil.convert2PageResult(resultDto));
    }

    @ApiOperation(value = "按月查询日记")
    @GetMapping("/month")
    public ResponseDTO<List<Diary>> queryByMonth(DiaryQueryDTO dto, Long curUserId) {
        QueryWrapper<Diary> wrapper = genUserQuery(curUserId);
        wrapper = wrapper.likeRight("diary_date", dto.getQueryDate())
                .orderByDesc("diary_date");

        List<Diary> resultDto = service.list(wrapper);
        return ResponseDTO.success(resultDto);
    }

    @ApiOperation(value = "添加日记")
    @PostMapping
    public ResponseDTO<Boolean> add(@RequestBody @Validated Diary body, Long curUserId) {
        body.setUserId(curUserId);
        return ResponseDTO.success(service.save(body));
    }

    @ApiOperation(value = "修改日记")
    @PutMapping("/{id}")
    public ResponseDTO<Boolean> update(@PathVariable("id") Long id, @RequestBody @Validated Diary body) {
        body.setId(id);
        return ResponseDTO.success(service.updateById(body));
    }

    @ApiOperation(value = "删除日记")
    @DeleteMapping("/{id}")
    public ResponseDTO<Boolean> delete(@PathVariable("id") Long id) {
        return ResponseDTO.success(service.removeById(id));
    }

}
