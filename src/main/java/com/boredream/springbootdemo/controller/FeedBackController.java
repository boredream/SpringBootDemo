package com.boredream.springbootdemo.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.boredream.springbootdemo.entity.FeedBack;
import com.boredream.springbootdemo.entity.dto.FeedBackQueryDTO;
import com.boredream.springbootdemo.entity.dto.PageResultDTO;
import com.boredream.springbootdemo.entity.dto.ResponseDTO;
import com.boredream.springbootdemo.service.IFeedBackService;
import com.boredream.springbootdemo.utils.PageUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 意见反馈 前端控制器
 * </p>
 *
 * @author boredream
 * @since 2021-12-06
 */
@RestController
@RequestMapping("/feed_back")
@Api(tags = {"意见反馈"})
public class FeedBackController {

    @Autowired
    private IFeedBackService service;

    @ApiOperation(value = "分页查询意见反馈")
    @GetMapping("/page")
    public ResponseDTO<PageResultDTO<FeedBack>> queryByPage(FeedBackQueryDTO dto, Long curUserId) {
        QueryWrapper<FeedBack> wrapper = new QueryWrapper<FeedBack>().eq("user_id", curUserId);
        Page<FeedBack> page = PageUtil.convert2QueryPage(dto);
        Page<FeedBack> resultDto = service.page(page, wrapper);
        return ResponseDTO.success(PageUtil.convert2PageResult(resultDto));
    }

    @ApiOperation(value = "添加意见反馈")
    @PostMapping
    public ResponseDTO<Boolean> add(@RequestBody @Validated FeedBack body, Long curUserId) {
        body.setUserId(curUserId);
        return ResponseDTO.success(service.save(body));
    }

    @ApiOperation(value = "删除意见反馈")
    @DeleteMapping("/{id}")
    public ResponseDTO<Boolean> delete(@PathVariable("id") Long id) {
        return ResponseDTO.success(service.removeById(id));
    }

}
