package com.boredream.springbootdemo;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.boredream.springbootdemo.auth.JwtUtil;
import com.boredream.springbootdemo.controller.UserController;
import com.boredream.springbootdemo.entity.dto.LoginRequestDTO;
import com.boredream.springbootdemo.entity.User;
import com.boredream.springbootdemo.entity.dto.ResponseDTO;
import com.boredream.springbootdemo.entity.dto.SetPasswordRequestDTO;
import com.boredream.springbootdemo.exception.ApiException;
import com.boredream.springbootdemo.mapper.UserMapper;
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
class UserTests {

	@Autowired
	UserController controller;

	@Autowired
	UserMapper mapper;

	@Autowired
	JwtUtil jwtUtil;

	@BeforeAll
	void before() {
		// delete all
		mapper.delete(new QueryWrapper<User>()
				.eq("username", "二狗子")
				.or().eq("username", "小仙女")
				.or().eq("username", "设置密码")
		);
	}

	@Test
	void testSetPassword() {
		// register
		LoginRequestDTO loginDto = new LoginRequestDTO();
		loginDto.setUsername("设置密码");
		loginDto.setPassword("123456");
		ResponseDTO<String> response = controller.register(loginDto);
		Assertions.assertTrue(response.getSuccess());

		Long curUserId = Long.parseLong(jwtUtil.getUserIdFromToken(response.getData()));

		// setPassword error
		SetPasswordRequestDTO setPswDto = new SetPasswordRequestDTO();
		try {
			setPswDto.setPassword("123");
			controller.setPassword(setPswDto, curUserId);
		} catch (ApiException e) {
			Assertions.assertEquals("密码必须是6至16位的数字或字母组成", e.getMessage());
		}

		try {
			setPswDto.setPassword("__________");
			controller.setPassword(setPswDto, curUserId);
		} catch (ApiException e) {
			Assertions.assertEquals("密码必须是6至16位的数字或字母组成", e.getMessage());
		}

		try {
			setPswDto.setPassword("12345678901234567890");
			controller.setPassword(setPswDto, curUserId);
		} catch (ApiException e) {
			Assertions.assertEquals("密码必须是6至16位的数字或字母组成", e.getMessage());
		}

		// setPassword
		setPswDto.setPassword("123456abcd");
		ResponseDTO<Boolean> booleanResponse = controller.setPassword(setPswDto, curUserId);
		Assertions.assertTrue(booleanResponse.getSuccess());

		// login error
		try {
			controller.login(loginDto);
		} catch (ApiException e) {
			Assertions.assertEquals("密码不正确", e.getMessage());
		}

		// login success
		loginDto.setPassword(setPswDto.getPassword());
		response = controller.login(loginDto);
		Assertions.assertTrue(response.getSuccess());
	}

	@Test
	void test() {
		// register
		LoginRequestDTO request = new LoginRequestDTO();
		request.setUsername("二狗子");
		request.setPassword("123456");
		String token1 = controller.register(request).getData();
		Assertions.assertNotNull(token1);

		request = new LoginRequestDTO();
		request.setUsername("小仙女");
		request.setPassword("123456");
		String token2 = controller.register(request).getData();
		Assertions.assertNotNull(token2);

		// get user info
		Long userId1 = Long.parseLong(jwtUtil.getUserIdFromToken(token1));
		User user1 = controller.getUserInfo(userId1).getData();
		Assertions.assertEquals("二狗子", user1.getUsername());

		Long userId2 = Long.parseLong(jwtUtil.getUserIdFromToken(token2));
		User user2 = controller.getUserInfo(userId2).getData();
		Assertions.assertEquals("小仙女", user2.getUsername());

		// login
		token2 = controller.login(request).getData();
		userId2 = Long.parseLong(jwtUtil.getUserIdFromToken(token2));
		user2 = controller.getUserInfo(userId2).getData();
		Assertions.assertEquals("小仙女", user2.getUsername());

		// update
		user1.setBirthday("1989-12-21");
		controller.update(userId1, user1);
		Assertions.assertEquals("1989-12-21", mapper.selectById(userId1).getBirthday());
	}

}
