package com.boredream.springbootdemo.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.boredream.springbootdemo.entity.TraceLocation;
import com.boredream.springbootdemo.entity.TraceRecord;
import com.boredream.springbootdemo.entity.dto.PageResultDTO;
import com.boredream.springbootdemo.entity.dto.ResponseDTO;
import com.boredream.springbootdemo.entity.dto.TraceRecordQueryDTO;
import com.boredream.springbootdemo.service.ITraceLocationService;
import com.boredream.springbootdemo.service.ITraceRecordService;
import com.boredream.springbootdemo.utils.PageUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 轨迹信息 前端控制器
 * </p>
 *
 * @author boredream
 * @since 2022-12-13
 */
@RestController
@RequestMapping("/trace_record")
@Api(tags = {"轨迹信息"})
public class TraceRecordController extends BaseController {

    @Autowired
    private ITraceRecordService service;

    @Autowired
    private ITraceLocationService traceLocationService;

    @ApiOperation(value = "分页查询轨迹信息")
    @GetMapping("/page")
    public ResponseDTO<PageResultDTO<TraceRecord>> queryByPage(TraceRecordQueryDTO dto, Long curUserId) {
        QueryWrapper<TraceRecord> wrapper = genUserQuery(curUserId);
        wrapper = wrapper.orderByDesc("start_time");
        Page<TraceRecord> page = PageUtil.convert2QueryPage(dto);
        Page<TraceRecord> resultDto = service.page(page, wrapper);
        return ResponseDTO.success(PageUtil.convert2PageResult(resultDto));
    }

    @ApiOperation(value = "添加轨迹信息")
    @PostMapping
    @Transactional
    public ResponseDTO<Boolean> add(@RequestBody @Validated TraceRecord body, Long curUserId) {
        body.setUserId(curUserId);
        boolean save = service.save(body);
        if(!save) {
            return ResponseDTO.error("保存TraceRecord失败");
        }

        List<TraceLocation> traceLocationList = new ArrayList<>();
        if (body.getTraceListStr() != null) {
            for (String s : body.getTraceListStr().split("_")) {
                String[] split = s.split(",");
                TraceLocation location = new TraceLocation();
                location.setTraceRecordId(body.getId());
                location.setTime(Long.parseLong(split[0]));
                location.setLatitude(Double.parseDouble(split[1]));
                location.setLongitude(Double.parseDouble(split[2]));
                traceLocationList.add(location);
            }
        }
        save = traceLocationService.saveBatch(traceLocationList);
        return ResponseDTO.success(save);
    }

    @ApiOperation(value = "修改轨迹信息")
    @PutMapping("/{id}")
    public ResponseDTO<Boolean> update(@PathVariable("id") Long id, @RequestBody @Validated TraceRecord body) {
        body.setId(id);
        return ResponseDTO.success(service.updateById(body));
    }

    @ApiOperation(value = "删除轨迹信息")
    @DeleteMapping("/{id}")
    public ResponseDTO<Boolean> delete(@PathVariable("id") Long id) {
        return ResponseDTO.success(service.removeById(id));
    }

}
