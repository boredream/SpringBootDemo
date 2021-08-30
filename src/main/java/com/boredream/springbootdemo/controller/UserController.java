package com.boredream.springbootdemo.controller;

import com.boredream.springbootdemo.entity.BaseResponse;
import com.boredream.springbootdemo.entity.LoginRequest;
import com.boredream.springbootdemo.exception.ApiException;
import com.boredream.springbootdemo.service.AuthService;
import com.boredream.springbootdemo.utils.MiscUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private AuthService authService;

    @RequestMapping(value = "/register", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity<BaseResponse> register(@Valid @RequestBody LoginRequest authRequest, BindingResult bindingResult) throws AuthenticationException {
        BaseResponse res;
        if (bindingResult.hasErrors()) {
            res = MiscUtil.getValidateError(bindingResult);
        } else {
            try {
                authService.register(authRequest);
                res = new BaseResponse(200, "ok");
            } catch (ApiException e) {
                res = e.getBaseResponse();
            }
        }

        return ResponseEntity.ok(res);
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity<BaseResponse> login(@Valid @RequestBody LoginRequest authRequest, BindingResult bindingResult) throws AuthenticationException {

        BaseResponse res;
        if (bindingResult.hasErrors()) {
            res = MiscUtil.getValidateError(bindingResult);
        } else {
            try {
                // Return the token
                final String token = authService.login(authRequest);
                res = new BaseResponse(200, "ok");
                res.putData("token", token);
            } catch (ApiException e) {
                res = e.getBaseResponse();
            }
        }
        return ResponseEntity.ok(res);
    }

}
