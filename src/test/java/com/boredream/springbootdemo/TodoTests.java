package com.boredream.springbootdemo;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.boredream.springbootdemo.controller.TodoController;
import com.boredream.springbootdemo.controller.TodoGroupController;
import com.boredream.springbootdemo.entity.Todo;
import com.boredream.springbootdemo.entity.TodoGroup;
import com.boredream.springbootdemo.entity.dto.ResponseDTO;
import com.boredream.springbootdemo.mapper.TodoGroupMapper;
import com.boredream.springbootdemo.mapper.TodoMapper;
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
class TodoTests {

	@Autowired
	TodoGroupMapper groupMapper;

	@Autowired
	TodoGroupController  groupController;

	@Autowired
	TodoController controller;

	@Autowired
	TodoMapper mapper;

	private Long curUserId = 1L;
	private Long cpUserId = 2L;

	@BeforeAll
	void before() {
		// delete all
		groupMapper.delete(new QueryWrapper<>());
		mapper.delete(new QueryWrapper<>());
	}

	@Test
	void test() {
		// add group
		TodoGroup group = new TodoGroup();
		group.setName("情侣一起必做的10件事");
		ResponseDTO<Boolean> commitResponse = groupController.add(group, curUserId);
		Assertions.assertTrue(commitResponse.getSuccess());

		group = new TodoGroup();
		group.setName("人生重大选择");
		commitResponse = groupController.add(group, curUserId);
		Assertions.assertTrue(commitResponse.getSuccess());

		Long groupId = groupMapper.selectOne(new QueryWrapper<TodoGroup>().eq("name", "情侣一起必做的10件事")).getId();

		// add
		Todo body = new Todo();
		body.setTodoGroupId(groupId);
		body.setName("一起拉练");
		commitResponse = controller.add(body, curUserId);
		Assertions.assertTrue(commitResponse.getSuccess());

		body = new Todo();
		body.setTodoGroupId(groupId);
		body.setName("一起做饭");
		commitResponse = controller.add(body, curUserId);
		Assertions.assertTrue(commitResponse.getSuccess());

		// query with group
		List<Todo> list = controller.query(curUserId).getData();
		System.out.println(list);

//		TodoQueryDTO dto = new TodoQueryDTO();
//		dto.setPage(1);
//		dto.setSize(20);
//		PageResultDTO<Todo> pageResponse = controller.queryByPage(dto, curUserId).getData();
//		Assertions.assertEquals(5, pageResponse.getRecords().size());
////		Assertions.assertEquals("2021-02-14", pageResponse.getRecords().get(1).getTodoDate());
//
//		// update
//		Long updateId = mapper.selectOne(new QueryWrapper<Todo>().eq("name", "结婚")).getId();
//		String newDate = "2022-05-01";
//		body = new Todo();
//		body.setTodoDate(newDate);
//		commitResponse = controller.update(updateId, body);
//		Assertions.assertTrue(commitResponse.getSuccess());
//		Assertions.assertEquals(newDate, mapper.selectById(updateId).getTodoDate());
//
//		// delete
//		commitResponse = controller.delete(pageResponse.getRecords().get(3).getId());
//		Assertions.assertTrue(commitResponse.getSuccess());
//		Assertions.assertEquals(4, mapper.selectList(new QueryWrapper<>()).size());
	}

}
