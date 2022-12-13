package com.boredream.springbootdemo.service.impl;

import com.boredream.springbootdemo.entity.TraceLocation;
import com.boredream.springbootdemo.mapper.TraceLocationMapper;
import com.boredream.springbootdemo.service.ITraceLocationService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 轨迹点信息 服务实现类
 * </p>
 *
 * @author boredream
 * @since 2022-12-13
 */
@Service
public class TraceLocationServiceImpl extends ServiceImpl<TraceLocationMapper, TraceLocation> implements ITraceLocationService {

}
