package com.boredream.springbootdemo;

import com.boredream.springbootdemo.service.IVerifyCodeService;
import org.junit.jupiter.api.*;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@MapperScan("com.boredream.springbootdemo.mapper")
class VerifyCodeTests {

    @Autowired
    IVerifyCodeService service;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    String phone = "18501683421";
    long duration = 2000;

    @BeforeAll
    void before() {
        redisTemplate.delete("VerifyCode:" + phone);
        redisTemplate.delete("VerifyCode1m:" + phone);
        redisTemplate.delete("VerifyCode1h:" + phone);
        redisTemplate.delete("VerifyCode1d:" + phone);
    }

    @Test
    void verifySuccess() {
        // 发送验证码
        String code = service.sendVerifyCode(phone, duration, true);
        Assertions.assertNotNull(code);

        // 正确验证码
        Assertions.assertTrue(service.checkVerifyCode(phone, code));
        try {
            // 验证成功后会删除验证码
            service.checkVerifyCode(phone, code);
        } catch (Exception e) {
            Assertions.assertEquals("短信验证码已过期，请重新发送", e.getMessage());
        }
    }

    @Test
    void verifyWrong() {
        // 发送验证码
        String code = service.sendVerifyCode(phone, duration, true);
        // 错误验证码
        Assertions.assertFalse(service.checkVerifyCode(phone, code + "0"));
    }

    @Test
    void verifyExpired() {
        // 发送验证码
        String code = service.sendVerifyCode(phone, duration, true);
        try {
            Thread.sleep(duration + 500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        try {
            // 过期验证码
            service.checkVerifyCode(phone, code);
        } catch (Exception e) {
            Assertions.assertEquals("短信验证码已过期，请重新发送", e.getMessage());
        }
    }

    @Test
    void verifyLimit1m() {
        // 发送验证码
        service.sendVerifyCode(phone, duration, true);
        try {
            // 需要把实际时间从1分改成1秒
            // TODO: chunyang 2021/12/14 配置
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        service.sendVerifyCode(phone, duration, true);
        try {
            service.sendVerifyCode(phone, duration, true);
        } catch (Exception e) {
            Assertions.assertEquals("短信发送过于频繁，请于1分钟后再重新尝试", e.getMessage());
        }
    }

    @Test
    void verifyLimit1hAnd1d() {
        // 发送验证码
        for (int i = 0; i < 4; i++) {
            service.sendVerifyCode(phone, duration, true);
        }
        try {
            service.sendVerifyCode(phone, duration, true);
        } catch (Exception e) {
            Assertions.assertEquals("短信发送过于频繁，请于1小时后再重新尝试", e.getMessage());
        }

        try {
            // 需要把实际时间从1小时改成5秒
            Thread.sleep(5500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // 超过限制时间重新发送
        for (int i = 0; i < 4; i++) {
            service.sendVerifyCode(phone, duration, true);
        }
        try {
            service.sendVerifyCode(phone, duration, true);
        } catch (Exception e) {
            Assertions.assertEquals("短信发送过于频繁，请于明天再重新尝试", e.getMessage());
        }
    }

}
