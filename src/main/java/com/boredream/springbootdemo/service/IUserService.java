package com.boredream.springbootdemo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.boredream.springbootdemo.entity.LoginRequest;
import com.boredream.springbootdemo.entity.User;
import com.boredream.springbootdemo.entity.dto.WxLoginDTO;
import org.springframework.security.core.Authentication;

/**
 * <p>
 * 用户 服务类
 * </p>
 *
 * @author boredream
 * @since 2021-09-28
 */
public interface IUserService extends IService<User> {

    String register(LoginRequest request);

    String login(LoginRequest request);

    String wxLogin(WxLoginDTO dto);

    User getUserInfo(Authentication auth);
}
