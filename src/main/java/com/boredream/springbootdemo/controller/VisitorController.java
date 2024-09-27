package com.boredream.springbootdemo.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.boredream.springbootdemo.entity.Case;
import com.boredream.springbootdemo.entity.Visitor;
import com.boredream.springbootdemo.entity.dto.PageResultDTO;
import com.boredream.springbootdemo.entity.dto.ResponseDTO;
import com.boredream.springbootdemo.entity.dto.VisitorQueryDTO;
import com.boredream.springbootdemo.service.ICaseService;
import com.boredream.springbootdemo.service.IVisitorService;
import com.boredream.springbootdemo.utils.PageUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 访客 前端控制器
 * </p>
 *
 * @author boredream
 * @since 2024-09-23
 */
@RestController
@RequestMapping("/visitor")
@Api(tags = {"访客"})
public class VisitorController {

    @Autowired
    private IVisitorService service;

    @Autowired
    private ICaseService caseService;

    @ApiOperation(value = "分页查询访客")
    @GetMapping("/page")
    public ResponseDTO<PageResultDTO<Visitor>> queryByPage(VisitorQueryDTO dto, Long curUserId) {
        QueryWrapper<Visitor> wrapper = new QueryWrapper<Visitor>().eq("user_id", curUserId);
        Page<Visitor> page = PageUtil.convert2QueryPage(dto);
        Page<Visitor> resultDto = service.page(page, wrapper);
        return ResponseDTO.success(PageUtil.convert2PageResult(resultDto));
    }


    @ApiOperation(value = "分页查询访客")
    @GetMapping("/queryWithCase")
    public ResponseDTO<PageResultDTO<Visitor>> queryWithCase(VisitorQueryDTO dto, Long curUserId) {
        QueryWrapper<Visitor> wrapper = new QueryWrapper<Visitor>().eq("user_id", curUserId);
        Page<Visitor> page = PageUtil.convert2QueryPage(dto);
        Page<Visitor> resultDto = service.page(page, wrapper);
        if (resultDto.getRecords() != null) {
            resultDto.getRecords().forEach(item -> {
                QueryWrapper<Case> caseWrapper = new QueryWrapper<Case>()
                        .eq("visitor_id", item.getId());
                item.setCaseList(caseService.list(caseWrapper));
            });
        }
        return ResponseDTO.success(PageUtil.convert2PageResult(resultDto));
    }

    @ApiOperation(value = "获取访客详情")
    @GetMapping("/queryByCaseId")
    public ResponseDTO<Visitor> queryByCaseId(Long caseId) {
        Case talkCase = caseService.getOne(new QueryWrapper<Case>().eq("id", caseId));
        if(talkCase == null || talkCase.getId() == null) {
            return ResponseDTO.error("案例信息中无法获取访客信息");
        }
        return ResponseDTO.success(service.getById(talkCase.getVisitorId()));
    }

    @ApiOperation(value = "添加访客")
    @PostMapping
    public ResponseDTO<Boolean> add(@RequestBody @Validated Visitor body, Long curUserId) {
        body.setUserId(curUserId);
        return ResponseDTO.success(service.save(body));
    }

    @ApiOperation(value = "修改访客")
    @PutMapping("/{id}")
    public ResponseDTO<Visitor> update(@PathVariable("id") Long id, @RequestBody @Validated Visitor body) {
        body.setId(id);
        service.updateById(body);
        return ResponseDTO.success(body);
    }

    @ApiOperation(value = "删除访客")
    @DeleteMapping("/{id}")
    public ResponseDTO<Boolean> delete(@PathVariable("id") Long id) {
        return ResponseDTO.success(service.removeById(id));
    }

}
