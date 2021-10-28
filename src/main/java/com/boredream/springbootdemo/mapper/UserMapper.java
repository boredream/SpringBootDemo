package com.boredream.springbootdemo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.boredream.springbootdemo.entity.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 用户 Mapper 接口
 * </p>
 *
 * @author boredream
 * @since 2021-09-18
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

}
