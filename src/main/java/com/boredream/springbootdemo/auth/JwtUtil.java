package com.boredream.springbootdemo.auth;


import cn.hutool.core.date.DateUnit;
import cn.hutool.jwt.JWT;
import cn.hutool.jwt.JWTUtil;
import com.boredream.springbootdemo.entity.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtUtil implements Serializable {

    private static final long serialVersionUID = -3301605591108950415L;

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private Long expiration;

    public String getUserIdFromToken(String token) {
        return JWTUtil.parseToken(token).getPayload("id").toString();
    }

    public String generateToken(User user) {
        long expireDateMillions = System.currentTimeMillis() + expiration * DateUnit.DAY.getMillis();

        Map<String, Object> payload = new HashMap<>();
        payload.put("id", user.getId());
        payload.put(JWT.EXPIRES_AT, new Date(expireDateMillions));
        return JWTUtil.createToken(payload, secret.getBytes());
    }

    public boolean validateToken(String token) {
        if (token == null) return false;
        return JWTUtil.parseToken(token).setKey(secret.getBytes()).validate(0);
    }
}
