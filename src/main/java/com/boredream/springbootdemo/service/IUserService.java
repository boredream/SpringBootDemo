package com.boredream.springbootdemo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.boredream.springbootdemo.entity.User;
import com.boredream.springbootdemo.entity.dto.LoginRequestDTO;
import com.boredream.springbootdemo.entity.dto.VerifyCodeDTO;
import com.boredream.springbootdemo.entity.dto.WxLoginDTO;

/**
 * <p>
 * 用户 服务类
 * </p>
 *
 * @author boredream
 * @since 2021-09-28
 */
public interface IUserService extends IService<User> {

    String register(LoginRequestDTO request);

    String login(LoginRequestDTO request);

    String wxLogin(WxLoginDTO dto);

    String loginWithVerifyCode(VerifyCodeDTO dto);

    User getUserInfo(Long curUserId);

    User bindCp(Long curUserId, Long cpUserId);

    boolean unbindCp(Long curUserId, Long cpUserId);

}
