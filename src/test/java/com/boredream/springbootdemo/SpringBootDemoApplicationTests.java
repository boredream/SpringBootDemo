package com.boredream.springbootdemo;

import com.boredream.springbootdemo.entity.LoginRequest;
import com.boredream.springbootdemo.service.IUserService;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@MapperScan("com.boredream.springbootdemo.mapper")
class SpringBootDemoApplicationTests {

	@Autowired
	IUserService service;

	@Test
	void contextLoads() {
		LoginRequest request = new LoginRequest();
		request.setUsername("boredream");
		request.setPassword("123456");
		String register = service.register(request);
		System.out.println("register " + register);

		String login = service.login(request);
		System.out.println("login " + login);
	}

}
