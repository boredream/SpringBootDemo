package com.boredream.springbootdemo.controller;

import com.boredream.springbootdemo.entity.dto.LoginRequestDTO;
import com.boredream.springbootdemo.entity.User;
import com.boredream.springbootdemo.entity.dto.ResponseDTO;
import com.boredream.springbootdemo.entity.dto.WxLoginDTO;
import com.boredream.springbootdemo.service.IUserService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@EnableTransactionManagement
@RequestMapping("/user")
public class UserController {

    @Autowired
    private IUserService service;

    @PostMapping(value = "/register", produces = "application/json")
    public ResponseDTO<String> register(@Valid @RequestBody LoginRequestDTO authRequest) {
        return ResponseDTO.success(service.register(authRequest));
    }

    @PostMapping(value = "/login", produces = "application/json")
    public ResponseDTO<String> login(@Valid @RequestBody LoginRequestDTO authRequest) {
        return ResponseDTO.success(service.login(authRequest));
    }

    @PostMapping(value = "/wxlogin", produces = "application/json")
    public ResponseDTO<String> wxLogin(@Valid @RequestBody WxLoginDTO dto) {
        return ResponseDTO.success(service.wxLogin(dto));
    }

    @GetMapping(value = "/info", produces = "application/json")
    public ResponseDTO<User> getUserInfo(Long curUserId) {
        return ResponseDTO.success(service.getUserInfo(curUserId));
    }

    @ApiOperation(value = "修改用户")
    @PutMapping("/{id}")
    public ResponseDTO<Boolean> update(@PathVariable("id") Long id, @RequestBody @Validated User params) {
        params.setId(id);
        return ResponseDTO.success(service.updateById(params));
    }

    @Transactional
    @PutMapping(value = "/cp/{cpUserId}", produces = "application/json")
    public ResponseDTO<User> bindCp(Long curUserId, @PathVariable("cpUserId") Long cpUserId) {
        return ResponseDTO.success(service.bindCp(curUserId, cpUserId));
    }

    @Transactional
    @DeleteMapping(value = "/cp/{cpUserId}", produces = "application/json")
    public ResponseDTO<Boolean> unbindCp(Long curUserId, @PathVariable("cpUserId") Long cpUserId) {
        return ResponseDTO.success(service.unbindCp(curUserId, cpUserId));
    }


}
