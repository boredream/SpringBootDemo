package com.boredream.springbootdemo.service.impl;

import com.boredream.springbootdemo.entity.Visitor;
import com.boredream.springbootdemo.mapper.VisitorMapper;
import com.boredream.springbootdemo.service.IVisitorService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户 服务实现类
 * </p>
 *
 * @author boredream
 * @since 2024-09-23
 */
@Service
public class VisitorServiceImpl extends ServiceImpl<VisitorMapper, Visitor> implements IVisitorService {

}
