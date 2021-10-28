package com.boredream.springbootdemo.controller;

import com.boredream.springbootdemo.entity.LoginRequest;
import com.boredream.springbootdemo.entity.User;
import com.boredream.springbootdemo.entity.dto.ResponseDTO;
import com.boredream.springbootdemo.entity.dto.WxLoginDTO;
import com.boredream.springbootdemo.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private IUserService userService;

    @PostMapping(value = "/register", produces = "application/json")
    public ResponseDTO<String> register(@Valid @RequestBody LoginRequest authRequest) {
        return ResponseDTO.success(userService.register(authRequest));
    }

    @PostMapping(value = "/login", produces = "application/json")
    public ResponseDTO<String> login(@Valid @RequestBody LoginRequest authRequest) {
        return ResponseDTO.success(userService.login(authRequest));
    }

    @PostMapping(value = "/wxlogin", produces = "application/json")
    public ResponseDTO<String> wxLogin(@Valid @RequestBody WxLoginDTO dto) {
        return ResponseDTO.success(userService.wxLogin(dto));
    }

    @GetMapping(value = "/info", produces = "application/json")
    public ResponseDTO<User> getUserInfo(Authentication auth) {
        return ResponseDTO.success(userService.getUserInfo(auth));
    }

}
