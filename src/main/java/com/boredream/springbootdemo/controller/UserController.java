package com.boredream.springbootdemo.controller;

import com.boredream.springbootdemo.entity.BaseResponse;
import com.boredream.springbootdemo.entity.LoginRequest;
import com.boredream.springbootdemo.service.AuthService;
import com.boredream.springbootdemo.utils.MiscUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private AuthService authService;

    @RequestMapping(value = "/login", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity<BaseResponse> login(@Valid @RequestBody LoginRequest authRequest, BindingResult bindingResult) throws AuthenticationException {

        BaseResponse res;
        if (bindingResult.hasErrors()) {
            res = MiscUtil.getValidateError(bindingResult);
        } else {
            // Return the token
            final String token = authService.login(authRequest.getUsername(), authRequest.getPassword());
            res = new BaseResponse(200, "ok");
            res.putData("token", token);
        }

        return ResponseEntity.ok(res);
    }

    @RequestMapping(value = "/refresh", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<BaseResponse> refresh(HttpServletRequest request, @RequestParam String token) throws AuthenticationException {

        BaseResponse res = new BaseResponse(200, "ok");
        String refreshedToken = authService.refresh(token);

        if (refreshedToken == null) {
            res.setCode(400);
            res.setMessage("无效token");
            return new ResponseEntity<>(res, HttpStatus.BAD_REQUEST);
        }

        res.putData("token", token);
        return ResponseEntity.ok(res);
    }

}
