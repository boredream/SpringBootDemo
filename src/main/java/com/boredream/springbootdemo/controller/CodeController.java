package com.boredream.springbootdemo.controller;

import com.boredream.springbootdemo.entity.BaseResponse;
import com.boredream.springbootdemo.exception.ApiException;
import com.boredream.springbootdemo.service.CodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/code")
public class CodeController {

    @Autowired
    CodeService service;

    @CrossOrigin
    @RequestMapping(value = "/docApi", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseEntity<BaseResponse<?>> register(@RequestBody Map<String, String> params) throws AuthenticationException {
        // TODO: chunyang 2021/9/7 参数统一校验？
        try {
            BaseResponse<Map<String, String>> res = new BaseResponse<>(200, "ok");
            res.setData(service.genCode(params.get("device"), params.get("doc")));
            return ResponseEntity.ok(res);
        } catch (ApiException e) {
            return ResponseEntity.ok(e.getBaseResponse());
        }
    }
}
