package com.boredream.springbootdemo.auth;

import com.boredream.springbootdemo.entity.LoginRequest;
import com.boredream.springbootdemo.entity.User;
import com.boredream.springbootdemo.entity.dto.WxLoginDTO;
import com.boredream.springbootdemo.entity.dto.WxSessionDTO;
import com.boredream.springbootdemo.exception.ApiException;
import com.boredream.springbootdemo.mapper.UserMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class AuthService {

    private static final Logger log = LoggerFactory.getLogger(AuthService.class);

    private AuthenticationManager authenticationManager;
    private UserDetailsService userDetailsService;
    private JwtUtil jwtUtil;

    @Autowired
    UserMapper mapper;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    public AuthService(
            AuthenticationManager authenticationManager,
            UserDetailsService userDetailsService,
            JwtUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.jwtUtil = jwtUtil;
    }

    public String register(LoginRequest request) {
        User oldUser = mapper.findUser(request.getUsername());
        if (oldUser != null) {
            // 会抛出401
            // throw new BadCredentialsException("用户已存在");
            throw new ApiException("用户已存在");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        mapper.insert(user);

        return genToken(request.getUsername(), request.getPassword());
    }

    public String login(LoginRequest request) {
        User user = mapper.findUser(request.getUsername());
        if (user == null) {
            throw new ApiException("用户不存在");
        }

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new ApiException("密码不正确");
        }

        return genToken(request.getUsername(), request.getPassword());
    }

    private String genToken(String username, String password) {
        // 认证用户，认证失败抛出异常，由JwtAuthError的commence类返回401
        UsernamePasswordAuthenticationToken upToken = new UsernamePasswordAuthenticationToken(username, password);
        final Authentication authentication = authenticationManager.authenticate(upToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // 如果认证通过，返回jwt
        final AuthUser userDetails = (AuthUser) userDetailsService.loadUserByUsername(username);
        return jwtUtil.generateToken(userDetails.getUser());
    }

    public String wxLogin(WxLoginDTO dto) {
        String url = "https://api.weixin.qq.com/sns/jscode2session?appid={appid}&secret={secret}&js_code={code}&grant_type=authorization_code";
        Map<String, String> requestMap = new HashMap<>();
        requestMap.put("appid", "wx0896ca1ddd64114e");
        requestMap.put("secret", "068602332cdecb1a9a5ec5c4b00b16f8");
        requestMap.put("code", dto.getCode());

        String json = restTemplate.getForObject(url, String.class, requestMap);
        try {
            WxSessionDTO session = new ObjectMapper().readValue(json, WxSessionDTO.class);
            User user = mapper.findUserByWxOpenId(session.getOpenid());
            String password = "123456";
            if (user == null) {
                // 自动新建账号
                user = new User();
                user.setOpenId(session.getOpenid());
                user.setUsername(session.getOpenid());
                user.setPassword(passwordEncoder.encode(password));
                mapper.insert(user);
            }
            return genToken(user.getUsername(), password);
        } catch (Exception e) {
            throw new ApiException("微信登录失败 " + e.getMessage());
        }
    }

    public User getUserInfo(Authentication auth) {
        String username = auth.getName();
        User user = mapper.findUser(username);
        user.setPassword(null);
        return user;
    }
}
