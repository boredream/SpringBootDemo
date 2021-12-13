package com.boredream.springbootdemo;

import com.boredream.springbootdemo.entity.VerifyCode;
import com.boredream.springbootdemo.service.IVerifyCodeService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@MapperScan("com.boredream.springbootdemo.mapper")
class VerifyCodeTests {

    @Autowired
    IVerifyCodeService service;

    @BeforeAll
    void before() {

    }

    @Test
    void test() {
        VerifyCode verifyCode = service.sendVerifyCode("18501683421", VerifyCode.VERIFY_TYPE_REGISTER_OR_LOGIN);
    }

}
