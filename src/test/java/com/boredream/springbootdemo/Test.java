package com.boredream.springbootdemo;

import cn.hutool.jwt.JWTUtil;

import java.util.HashMap;
import java.util.Map;

public class Test {

    public static void main(String[] args) {
        String key = "com.boredream.springbootdemo.key";
        Map<String, Object> payload = new HashMap<>();
        payload.put("id", 123);
        payload.put("name", "myname");
        String token = JWTUtil.createToken(payload, key.getBytes());
        System.out.println(token);
    }

}
