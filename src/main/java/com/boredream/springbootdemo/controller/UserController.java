package com.boredream.springbootdemo.controller;

import com.boredream.springbootdemo.auth.AuthService;
import com.boredream.springbootdemo.entity.LoginRequest;
import com.boredream.springbootdemo.entity.dto.ResponseDTO;
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

}
