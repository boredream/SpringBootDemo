package com.boredream.springbootdemo.controller;

import com.boredream.springbootdemo.entity.User;
import com.boredream.springbootdemo.entity.dto.*;
import com.boredream.springbootdemo.service.IUserService;
import com.boredream.springbootdemo.service.IVerifyCodeService;
import com.boredream.springbootdemo.service.IWxService;
import com.boredream.springbootdemo.utils.DateUtils;
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

    @Autowired
    private IVerifyCodeService verifyCodeService;

    @Autowired
    private IWxService wxService;

    @ApiOperation(value = "发送验证码")
    @GetMapping(value = "/sendVerifyCode", produces = "application/json")
    public ResponseDTO<Boolean> sendVerifyCode(@Valid PhoneDTO dto) {
        String code = verifyCodeService.sendVerifyCode(dto.getPhone(), 5 * DateUtils.ONE_MINUTE_MILLIONS, false);
        return ResponseDTO.success(code != null);
    }

    @ApiOperation(value = "使用验证码注册或登录用户")
    @PostMapping(value = "/loginWithVerifyCode", produces = "application/json")
    public ResponseDTO<String> loginWithVerifyCode(@Valid @RequestBody VerifyCodeDTO dto) {
        return ResponseDTO.success(service.loginWithVerifyCode(dto));
    }

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
        boolean checkMsgSec = wxService.checkMsgSec(params.getPlatform(), id, IWxService.SCENE_SOCIAL, params.getNickname());
        if(!checkMsgSec) {
            return ResponseDTO.errorMsgSecCheck();
        }
        params.setId(id);
        return ResponseDTO.success(service.updateById(params));
    }

    @ApiOperation(value = "设置密码")
    @PutMapping("/setPassword")
    public ResponseDTO<Boolean> setPassword(@RequestBody @Validated SetPasswordRequestDTO params, Long curUserId) {
        return ResponseDTO.success(service.setPassword(curUserId, params.getPassword()));
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
