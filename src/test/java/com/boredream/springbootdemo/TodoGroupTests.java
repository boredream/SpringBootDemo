package com.boredream.springbootdemo;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.boredream.springbootdemo.controller.TodoGroupController;
import com.boredream.springbootdemo.entity.TodoGroup;
import com.boredream.springbootdemo.entity.dto.ResponseDTO;
import com.boredream.springbootdemo.mapper.TodoGroupMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@MapperScan("com.boredream.springbootdemo.mapper")
class TodoGroupTests {

	@Autowired
	TodoGroupController controller;

	@Autowired
	TodoGroupMapper mapper;

	private Long curUserId = 1L;
	private Long cpUserId = 2L;

	@BeforeAll
	void before() {
		// delete all
		mapper.delete(new QueryWrapper<>());
	}

	@Test
	void test() {
		// add
		TodoGroup body = new TodoGroup();
		body.setName("情侣一起必做的10件事");
		ResponseDTO<Boolean> commitResponse = controller.add(body, curUserId);
		Assertions.assertTrue(commitResponse.getSuccess());

		body = new TodoGroup();
		body.setName("人生重大选择");
		commitResponse = controller.add(body, curUserId);
		Assertions.assertTrue(commitResponse.getSuccess());

		body = new TodoGroup();
		body.setName("待修改");
		commitResponse = controller.add(body, curUserId);
		Assertions.assertTrue(commitResponse.getSuccess());

		List<TodoGroup> list = mapper.selectList(new QueryWrapper<>());
		Assertions.assertEquals(3, list.size());

		// update
		Long updateId = mapper.selectOne(new QueryWrapper<TodoGroup>().eq("name", "待修改")).getId();
		String newColumn = "清单组XXX";
		body = new TodoGroup();
		body.setName(newColumn);
		commitResponse = controller.update(updateId, body);
		Assertions.assertTrue(commitResponse.getSuccess());
		Assertions.assertEquals(newColumn, mapper.selectById(updateId).getName());

		// delete
		commitResponse = controller.delete(list.get(1).getId());
		Assertions.assertTrue(commitResponse.getSuccess());
		Assertions.assertEquals(2, mapper.selectList(new QueryWrapper<>()).size());
	}

}
