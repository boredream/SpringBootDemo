package com.boredream.springbootdemo;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.boredream.springbootdemo.auth.JwtUtil;
import com.boredream.springbootdemo.controller.UserController;
import com.boredream.springbootdemo.entity.LoginRequest;
import com.boredream.springbootdemo.entity.User;
import com.boredream.springbootdemo.entity.dto.ResponseDTO;
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
		mapper.delete(new QueryWrapper<User>().eq("username", "二狗子")
				.or().eq("username", "小仙女"));
	}

	@Test
	void test() {
		// TODO: chunyang 2021/11/24
		// wx login
//		WxLoginDTO dto = new WxLoginDTO();
//		dto.setCode("123456");
//		String token = controller.wxLogin(dto).getData();
//		Assertions.assertNotNull(token);

		// register
		LoginRequest request = new LoginRequest();
		request.setUsername("二狗子");
		request.setPassword("123456");
		String token1 = controller.register(request).getData();
		Assertions.assertNotNull(token1);

		request = new LoginRequest();
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

		// bind cp
		ResponseDTO<Boolean> commitResponse = controller.bindCp(userId1, userId2);
		Assertions.assertTrue(commitResponse.getSuccess());
		Assertions.assertEquals(userId2, mapper.selectById(userId1).getCpUserId());
		Assertions.assertEquals(userId1, mapper.selectById(userId2).getCpUserId());

		// bind duplicate
		try {
			controller.bindCp(userId1, 1L);
		} catch (ApiException e) {
			Assertions.assertEquals("无法绑定。您已经绑定过伴侣了，请先解绑后再重新尝试", e.getMessage());
		}

		// unbind cp
		commitResponse = controller.unbindCp(userId2, userId1);
		Assertions.assertTrue(commitResponse.getSuccess());
		Assertions.assertNull(mapper.selectById(userId1).getCpUserId());
		Assertions.assertNull(mapper.selectById(userId2).getCpUserId());

//		User body = new User();
//		body.setContent("content " + System.currentTimeMillis());
//		body.setUserDate("2021-12-21");
//		ResponseDTO<Boolean> commitResponse = controller.add(body, curUserId);
//		Assertions.assertTrue(commitResponse.getSuccess());
//
//		body = new User();
//		body.setContent("content " + System.currentTimeMillis());
//		body.setUserDate("2021-02-05");
//		commitResponse = controller.add(body, curUserId);
//		Assertions.assertTrue(commitResponse.getSuccess());
//
//		body = new User();
//		body.setContent("content " + System.currentTimeMillis());
//		body.setUserDate("2021-02-05");
//		commitResponse = controller.add(body, cpUserId);
//		Assertions.assertTrue(commitResponse.getSuccess());
//
//		body = new User();
//		body.setContent("content " + System.currentTimeMillis());
//		body.setUserDate("2021-02-14");
//		commitResponse = controller.add(body, cpUserId);
//		Assertions.assertTrue(commitResponse.getSuccess());
//
//		// query by pages
//		UserQueryDTO dto = new UserQueryDTO();
//		dto.setPage(1);
//		dto.setSize(20);
//		PageResultDTO<User> pageResponse = controller.queryByPage(dto, curUserId).getData();
//		Assertions.assertEquals(4, pageResponse.getRecords().size());
//		Assertions.assertEquals("2021-02-14", pageResponse.getRecords().get(1).getUserDate());
//
//		// query by month
//		dto = new UserQueryDTO();
//		dto.setQueryDate("2021-12");
//		List<User> listResponse = controller.queryByMonth(dto, curUserId).getData();
//		Assertions.assertEquals(1, listResponse.size());
//
//		// update
//		Long updateId = listResponse.get(0).getId();
//		String newContent = "new content " + System.currentTimeMillis();
//		body = new User();
//		body.setContent(newContent);
//		commitResponse = controller.update(updateId, body);
//		Assertions.assertTrue(commitResponse.getSuccess());
//		Assertions.assertEquals(newContent, mapper.selectById(updateId).getContent());
//
//		// delete
//		commitResponse = controller.delete(pageResponse.getRecords().get(1).getId());
//		Assertions.assertTrue(commitResponse.getSuccess());
//		Assertions.assertEquals(3, mapper.selectList(new QueryWrapper<>()).size());
	}

}
