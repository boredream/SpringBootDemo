package com.boredream.springbootdemo.controller;

import com.boredream.springbootdemo.auth.AuthService;
import com.boredream.springbootdemo.comstant.ResponseCodeConst;
import com.boredream.springbootdemo.entity.LoginRequest;
import com.boredream.springbootdemo.entity.dto.ResponseDTO;
import com.boredream.springbootdemo.entity.dto.WxLoginDTO;
import com.boredream.springbootdemo.entity.dto.WxSessionDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/user" )
public class UserController {

    @Autowired
    private AuthService authService;

    @PostMapping(value = "/register" , produces = "application/json" )
    public ResponseDTO<String> register(@Valid @RequestBody LoginRequest authRequest) {
        authService.register(authRequest);
        return ResponseDTO.succData("注册成功" );
    }

    @PostMapping(value = "/login" , produces = "application/json" )
    public ResponseDTO<String> login(@Valid @RequestBody LoginRequest authRequest) {
        return ResponseDTO.succData(authService.login(authRequest));
    }

    @PostMapping(value = "/wxlogin", produces = "application/json")
    public ResponseDTO<WxSessionDTO> wxLogin(@Valid @RequestBody WxLoginDTO dto) {
        WxSessionDTO response = authService.wxLogin(dto);
        if (response == null) {
            // TODO: chunyang 2021/9/30 错误封装
            return ResponseDTO.wrap(ResponseCodeConst.ERROR_PARAM);
        }
        return ResponseDTO.succData(response);
    }

}
