package com.boredream.springbootdemo.service.impl;

import com.boredream.springbootdemo.entity.Case;
import com.boredream.springbootdemo.mapper.CaseMapper;
import com.boredream.springbootdemo.service.ICaseService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 案例 服务实现类
 * </p>
 *
 * @author boredream
 * @since 2024-09-23
 */
@Service
public class CaseServiceImpl extends ServiceImpl<CaseMapper, Case> implements ICaseService {

}
