package com.boredream.springbootdemo.service.impl;

import com.boredream.springbootdemo.entity.TraceRecord;
import com.boredream.springbootdemo.mapper.TraceRecordMapper;
import com.boredream.springbootdemo.service.ITraceRecordService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 轨迹信息 服务实现类
 * </p>
 *
 * @author boredream
 * @since 2022-12-13
 */
@Service
public class TraceRecordServiceImpl extends ServiceImpl<TraceRecordMapper, TraceRecord> implements ITraceRecordService {

}
