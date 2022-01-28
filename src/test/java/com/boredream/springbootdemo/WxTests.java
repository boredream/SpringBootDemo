package com.boredream.springbootdemo;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.boredream.springbootdemo.controller.DiaryController;
import com.boredream.springbootdemo.entity.Diary;
import com.boredream.springbootdemo.entity.dto.ResponseDTO;
import com.boredream.springbootdemo.mapper.DiaryMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@MapperScan("com.boredream.springbootdemo.mapper")
class WxTests {

    @Autowired
    DiaryController controller;

    @Autowired
    DiaryMapper mapper;

    private Long curUserId = 1L;
    private Long cpUserId = 2L;

    @BeforeAll
    void before() {
        // delete all
        mapper.delete(new QueryWrapper<>());
    }

    @Test
    void testMsgSec() {
        // 需要微信2小时内登录过 openId = oNLSd5G4kYWNlGlUITfF_41ofXzA
        Diary body = new Diary();
        body.setContent("是咪咪上白嫩午免费电影田佳妮强奸唱的嫂子闻铃身下硬棒");
        body.setDiaryDate("2021-12-21");
        body.setPlatform("wx");
        ResponseDTO<Boolean> commitResponse = controller.add(body, curUserId);
        Assertions.assertEquals("发送内容不合规", commitResponse.getMsg());

        body.setPlatform(null);
        commitResponse = controller.add(body, curUserId);
        Assertions.assertTrue(commitResponse.getSuccess());

    }

}
