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
		group.setName("到处旅游");
		commitResponse = groupController.add(group, curUserId);
		Assertions.assertTrue(commitResponse.getSuccess());

		group = new TodoGroup();
		group.setName("人生重大选择");
		commitResponse = groupController.add(group, cpUserId);
		Assertions.assertTrue(commitResponse.getSuccess());

		group = new TodoGroup();
		group.setName("待修改");
		commitResponse = groupController.add(group, cpUserId);
		Assertions.assertTrue(commitResponse.getSuccess());

		List<TodoGroup> groupList = groupMapper.selectList(new QueryWrapper<>());
		Assertions.assertEquals(4, groupList.size());

		// update group
		Long updateId = groupMapper.selectOne(new QueryWrapper<TodoGroup>().eq("name", "待修改")).getId();
		group = new TodoGroup();
		group.setName("清单组XXX");
		commitResponse = groupController.update(updateId, group);
		Assertions.assertTrue(commitResponse.getSuccess());
		Assertions.assertEquals("清单组XXX", groupMapper.selectById(updateId).getName());

		// delete group 空组
		updateId = groupMapper.selectOne(new QueryWrapper<TodoGroup>().eq("name", "到处旅游")).getId();
		commitResponse = groupController.delete(updateId);
		Assertions.assertTrue(commitResponse.getSuccess());
		Assertions.assertEquals(3, groupMapper.selectList(new QueryWrapper<>()).size());



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
		commitResponse = controller.add(body, cpUserId);
		Assertions.assertTrue(commitResponse.getSuccess());

		body = new Todo();
		body.setTodoGroupId(groupId);
		body.setName("一起旅行");
		commitResponse = controller.add(body, curUserId);
		Assertions.assertTrue(commitResponse.getSuccess());

		// query with group
		List<Todo> list = controller.query(curUserId).getData();
		Assertions.assertEquals(3, list.size());
		Assertions.assertEquals("情侣一起必做的10件事", list.get(0).getTodoGroupName());

		// update
		updateId = mapper.selectOne(new QueryWrapper<Todo>().eq("name", "一起旅行")).getId();
		String newData = "一起去旅行";
		body = new Todo();
		body.setDone(true);
		body.setName(newData);
		body.setDoneDate("2022-02-14");
		body.setDetail("去趟铁岭");
		commitResponse = controller.update(updateId, body);
		Assertions.assertTrue(commitResponse.getSuccess());
		Assertions.assertEquals(newData, mapper.selectById(updateId).getName());

		// delete
		commitResponse = controller.delete(list.get(1).getId());
		Assertions.assertTrue(commitResponse.getSuccess());
		Assertions.assertEquals(2, mapper.selectList(new QueryWrapper<>()).size());

		// delete group 清单组有内容的
		Long deleteGroupId = groupMapper.selectOne(new QueryWrapper<TodoGroup>().eq("name", "清单组XXX")).getId();
		for (int i = 0; i < 3; i++) {
			body = new Todo();
			body.setTodoGroupId(deleteGroupId);
			body.setName("清单" + i);
			controller.add(body, curUserId);
		}
		Assertions.assertEquals(3, mapper.selectList(new QueryWrapper<Todo>().eq("todo_group_id", deleteGroupId)).size());

		commitResponse = groupController.delete(deleteGroupId);
		Assertions.assertTrue(commitResponse.getSuccess());
		Assertions.assertEquals(0, mapper.selectList(new QueryWrapper<Todo>().eq("todo_group_id", deleteGroupId)).size());
	}

}
