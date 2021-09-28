package com.boredream.springbootdemo.service.impl;

import com.boredream.springbootdemo.entity.TheDay;
import com.boredream.springbootdemo.mapper.TheDayMapper;
import com.boredream.springbootdemo.service.ITheDayService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 纪念日 服务实现类
 * </p>
 *
 * @author boredream
 * @since 2021-09-28
 */
@Service
public class TheDayServiceImpl extends ServiceImpl<TheDayMapper, TheDay> implements ITheDayService {

}
