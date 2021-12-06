package com.boredream.springbootdemo.service.impl;

import com.boredream.springbootdemo.entity.FeedBack;
import com.boredream.springbootdemo.mapper.FeedBackMapper;
import com.boredream.springbootdemo.service.IFeedBackService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 意见反馈 服务实现类
 * </p>
 *
 * @author boredream
 * @since 2021-12-06
 */
@Service
public class FeedBackServiceImpl extends ServiceImpl<FeedBackMapper, FeedBack> implements IFeedBackService {

}
