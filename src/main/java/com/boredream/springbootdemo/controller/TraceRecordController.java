package com.boredream.springbootdemo.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.boredream.springbootdemo.entity.TraceLocation;
import com.boredream.springbootdemo.entity.TraceRecord;
import com.boredream.springbootdemo.entity.dto.PageResultDTO;
import com.boredream.springbootdemo.entity.dto.ResponseDTO;
import com.boredream.springbootdemo.entity.dto.TraceRecordQueryDTO;
import com.boredream.springbootdemo.entity.dto.TraceRecordSyncQueryDTO;
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

    @ApiOperation(value = "按同步时间查询戳轨迹信息，会返回时间戳以后的所有数据")
    @GetMapping("/sync")
    public ResponseDTO<List<TraceRecord>> queryBySyncTimestamp(TraceRecordSyncQueryDTO dto, Long curUserId) {
        QueryWrapper<TraceRecord> wrapper = genUserQuery(curUserId);
        wrapper = wrapper.gt("sync_timestamp", dto.getLocalTimestamp());
        return ResponseDTO.success(service.list(wrapper));
    }

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
    public ResponseDTO<TraceRecord> add(@RequestBody @Validated TraceRecord body, Long curUserId) {
        body.setUserId(curUserId);
        // 服务端保存的时候，更新同步相关数据
        body.setSyncTimestamp(System.currentTimeMillis());
        body.setSynced(true);

        // 如果dbId相同，代表手机端发起过同步，但未收到成功回执，所以会再次发起
        QueryWrapper<TraceRecord> wrapper = new QueryWrapper<TraceRecord>()
                .eq("db_id", body.getDbId());

        boolean save = service.saveOrUpdate(body, wrapper);
        if (!save) {
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
        traceLocationService.saveBatch(traceLocationList);
        return ResponseDTO.success(body);
    }

    @ApiOperation(value = "修改轨迹信息")
    @PutMapping("/{id}")
    public ResponseDTO<TraceRecord> update(@PathVariable("id") Long id, @RequestBody @Validated TraceRecord body) {
        body.setId(id);
        body.setSyncTimestamp(System.currentTimeMillis());
        service.updateById(body);

        body.setSynced(true);
        return ResponseDTO.success(body);
    }

    @ApiOperation(value = "删除轨迹信息")
    @DeleteMapping("/{id}")
    public ResponseDTO<TraceRecord> delete(@PathVariable("id") Long id) {
        TraceRecord record = service.getById(id);
        record.setIsDelete(true);
        return update(id, record);
    }

}
