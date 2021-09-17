package com.boredream.springbootdemo;

import org.junit.jupiter.api.Test;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@MapperScan("com.boredream.springbootdemo.mapper")
class SpringBootDemoApplicationTests {

	@Test
	void contextLoads() {
	}

}
