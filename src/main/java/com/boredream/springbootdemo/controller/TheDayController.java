package com.boredream.springbootdemo.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.boredream.springbootdemo.entity.TheDay;
import com.boredream.springbootdemo.entity.dto.PageResultDTO;
import com.boredream.springbootdemo.entity.dto.ResponseDTO;
import com.boredream.springbootdemo.entity.dto.TheDayQueryDTO;
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
@RequestMapping("/the_day")
@Api(tags = {"纪念日"})
public class TheDayController extends BaseController {

    @Autowired
    private ITheDayService service;

    @ApiOperation(value = "分页查询纪念日")
    @GetMapping("/page")
    public ResponseDTO<PageResultDTO<TheDay>> queryByPage(TheDayQueryDTO dto, Long curUserId) {
        QueryWrapper<TheDay> wrapper = genUserQuery(curUserId);
        // TODO: chunyang 2021/11/24 排序规则？
//        wrapper = wrapper.orderByDesc("the_day_date");
        Page<TheDay> page = PageUtil.convert2QueryPage(dto);
        Page<TheDay> resultDto = service.page(page, wrapper);
        return ResponseDTO.success(PageUtil.convert2PageResult(resultDto));
    }

    @ApiOperation(value = "添加纪念日")
    @PostMapping
    public ResponseDTO<Boolean> add(@RequestBody @Validated TheDay params, Long curUserId) {
        params.setUserId(curUserId);
        return ResponseDTO.success(service.save(params));
    }

    @ApiOperation(value = "修改纪念日")
    @PutMapping("/{id}")
    public ResponseDTO<Boolean> update(@PathVariable("id") Long id, @RequestBody @Validated TheDay params) {
        params.setId(id);
        return ResponseDTO.success(service.updateById(params));
    }

    @ApiOperation(value = "删除纪念日")
    @DeleteMapping("/{id}")
    public ResponseDTO<Boolean> delete(@PathVariable("id") Long id) {
        return ResponseDTO.success(service.removeById(id));
    }

}
