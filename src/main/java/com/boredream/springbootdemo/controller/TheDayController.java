package com.boredream.springbootdemo.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.boredream.springbootdemo.entity.TheDay;
import com.boredream.springbootdemo.entity.dto.PageResultDTO;
import com.boredream.springbootdemo.entity.dto.ResponseDTO;
import com.boredream.springbootdemo.entity.dto.TheDayQueryDto;
import com.boredream.springbootdemo.service.ITheDayService;
import com.boredream.springbootdemo.utils.PageUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 纪念日 前端控制器
 * </p>
 *
 * @author boredream
 * @since 2021-09-28
 */
@RestController
@RequestMapping("/theDay")
@Api(tags = {"纪念日" })
public class TheDayController {

    @Autowired
    private ITheDayService service;

    @ApiOperation(value = "分页查询纪念日")
    @GetMapping("/page")
    public ResponseDTO<PageResultDTO<TheDay>> queryByPage(TheDayQueryDto dto) {
        QueryWrapper<TheDay> wrapper = new QueryWrapper<TheDay>().likeRight("the_day_date", dto.getQueryDate());
        Page<TheDay> page = PageUtil.convert2QueryPage(dto);
        Page<TheDay> resultDto = service.page(page, wrapper);
        return ResponseDTO.succData(PageUtil.convert2PageResult(resultDto));
    }

    @ApiOperation(value = "添加纪念日")
    @PostMapping
    public ResponseDTO<Boolean> add(@RequestBody @Validated TheDay params) {
        return ResponseDTO.succData(service.save(params));
    }

    @ApiOperation(value = "修改纪念日")
    @PutMapping("/{id}")
    public ResponseDTO<Boolean> update(@PathVariable("id") Long id, @RequestBody @Validated TheDay params) {
        params.setId(id);
        return ResponseDTO.succData(service.updateById(params));
    }

    @ApiOperation(value = "删除纪念日")
    @DeleteMapping("/{id}")
    public ResponseDTO<Boolean> delete(@PathVariable("id") Long id) {
        return ResponseDTO.succData(service.removeById(id));
    }

}
