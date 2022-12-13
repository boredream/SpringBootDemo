package com.boredream.springbootdemo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.boredream.springbootdemo.entity.FeedBack;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 意见反馈 Mapper 接口
 * </p>
 *
 * @author boredream
 * @since 2021-12-06
 */
@Mapper
public interface FeedBackMapper extends BaseMapper<FeedBack> {

}
