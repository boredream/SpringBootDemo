package com.boredream.springbootdemo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.boredream.springbootdemo.auth.JwtUtil;
import com.boredream.springbootdemo.entity.LoginRequest;
import com.boredream.springbootdemo.entity.User;
import com.boredream.springbootdemo.entity.dto.WxLoginDTO;
import com.boredream.springbootdemo.entity.dto.WxSessionDTO;
import com.boredream.springbootdemo.exception.ApiException;
import com.boredream.springbootdemo.mapper.UserMapper;
import com.boredream.springbootdemo.service.IUserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 用户 服务实现类
 * </p>
 *
 * @author boredream
 * @since 2021-09-18
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    private JwtUtil jwtUtil;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    public UserServiceImpl(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    private User getUserByUsername(String username) {
        QueryWrapper<User> wrapper = new QueryWrapper<User>().eq("username", username);
        return getOne(wrapper);
    }

    public String register(LoginRequest request) {
        User oldUser = getUserByUsername(request.getUsername());
        if (oldUser != null) {
            throw new ApiException("用户已存在");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        save(user);

        return jwtUtil.generateToken(user);
    }

    public String login(LoginRequest request) {
        User user = getUserByUsername(request.getUsername());
        if (user == null) {
            throw new ApiException("用户不存在");
        }

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new ApiException("密码不正确");
        }

        return jwtUtil.generateToken(user);
    }

    public String wxLogin(WxLoginDTO dto) {
        // TODO: chunyang 2021/10/28 使用配置
        String url = "https://api.weixin.qq.com/sns/jscode2session?appid={appid}&secret={secret}&js_code={code}&grant_type=authorization_code";
        Map<String, String> requestMap = new HashMap<>();
        requestMap.put("appid", "wx0896ca1ddd64114e");
        requestMap.put("secret", "068602332cdecb1a9a5ec5c4b00b16f8");
        requestMap.put("code", dto.getCode());

        String json = restTemplate.getForObject(url, String.class, requestMap);
        try {
            WxSessionDTO session = new ObjectMapper().readValue(json, WxSessionDTO.class);
            QueryWrapper<User> wrapper = new QueryWrapper<User>().eq("openId", session.getOpenid());
            User user = getOne(wrapper);

            if (user == null) {
                // 自动新建账号
                user = new User();
                user.setUsername(session.getOpenid());
                user.setOpenId(session.getOpenid());
                save(user);
            }
            return jwtUtil.generateToken(user);
        } catch (Exception e) {
            throw new ApiException("微信登录失败 " + e.getMessage());
        }
    }

    @Override
    public User getUserInfo(Authentication auth) {
        User user = getUserByUsername(auth.getName());
        user.setPassword(null);
        return user;
    }
}
