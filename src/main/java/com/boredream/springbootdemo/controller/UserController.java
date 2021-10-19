package com.boredream.springbootdemo.controller;

import com.boredream.springbootdemo.auth.AuthService;
import com.boredream.springbootdemo.entity.LoginRequest;
import com.boredream.springbootdemo.entity.User;
import com.boredream.springbootdemo.entity.dto.ResponseDTO;
import com.boredream.springbootdemo.entity.dto.WxLoginDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private AuthService authService;

    @PostMapping(value = "/register", produces = "application/json")
    public ResponseDTO<String> register(@Valid @RequestBody LoginRequest authRequest) {
        return ResponseDTO.success(authService.register(authRequest));
    }

    @PostMapping(value = "/login", produces = "application/json")
    public ResponseDTO<String> login(@Valid @RequestBody LoginRequest authRequest) {
        return ResponseDTO.success(authService.login(authRequest));
    }

    @PostMapping(value = "/wxlogin", produces = "application/json")
    public ResponseDTO<String> wxLogin(@Valid @RequestBody WxLoginDTO dto) {
        return ResponseDTO.success(authService.wxLogin(dto));
    }

    @GetMapping(value = "/info", produces = "application/json")
    public ResponseDTO<User> getUserInfo(Authentication auth) {
        return ResponseDTO.success(authService.getUserInfo(auth));
    }

}
