package com.boredream.springbootdemo;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.boredream.springbootdemo.controller.DiaryController;
import com.boredream.springbootdemo.entity.Diary;
import com.boredream.springbootdemo.entity.dto.DiaryQueryDTO;
import com.boredream.springbootdemo.entity.dto.PageResultDTO;
import com.boredream.springbootdemo.entity.dto.ResponseDTO;
import com.boredream.springbootdemo.mapper.DiaryMapper;
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
class DiaryTests {

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

//	@Test
//	void testTemp() {
//		DiaryQueryDTO dto = new DiaryQueryDTO();
//		dto.setQueryDate("2021-12");
//		List<Diary> listResponse = controller.queryByMonth(dto, curUserId).getData();
//		Assertions.assertEquals(1, listResponse.size());
//	}

	@Test
	void test() {
		// add
		Diary body = new Diary();
		body.setContent("content " + System.currentTimeMillis());
		body.setDiaryDate("2021-02-05");
		ResponseDTO<Boolean> commitResponse = controller.add(body, curUserId);
		Assertions.assertTrue(commitResponse.getSuccess());

		body = new Diary();
		body.setContent("content " + System.currentTimeMillis());
		body.setDiaryDate("2021-12-21");
		commitResponse = controller.add(body, curUserId);
		Assertions.assertTrue(commitResponse.getSuccess());

		body = new Diary();
		body.setContent("content " + System.currentTimeMillis());
		body.setDiaryDate("2021-02-05");
		commitResponse = controller.add(body, -1L);
		Assertions.assertTrue(commitResponse.getSuccess());

		body = new Diary();
		body.setContent("content " + System.currentTimeMillis());
		body.setDiaryDate("2021-12-21");
		commitResponse = controller.add(body, -2L);
		Assertions.assertTrue(commitResponse.getSuccess());

		body = new Diary();
		body.setContent("content " + System.currentTimeMillis());
		body.setDiaryDate("2021-02-14");
		commitResponse = controller.add(body, cpUserId);
		Assertions.assertTrue(commitResponse.getSuccess());

		// query by pages
		DiaryQueryDTO dto = new DiaryQueryDTO();
		dto.setPage(1);
		dto.setSize(20);
		PageResultDTO<Diary> pageResponse = controller.queryByPage(dto, curUserId).getData();
		Assertions.assertEquals(3, pageResponse.getRecords().size());
		Assertions.assertEquals("2021-02-14", pageResponse.getRecords().get(1).getDiaryDate());
		Assertions.assertEquals(curUserId, pageResponse.getRecords().get(0).getUserId());
		Assertions.assertEquals(curUserId, pageResponse.getRecords().get(0).getUser().getId());

		// query by month
		dto = new DiaryQueryDTO();
		dto.setQueryDate("2021-12");
		List<Diary> listResponse = controller.queryByMonth(dto, curUserId).getData();
		Assertions.assertEquals(1, listResponse.size());

		// update
		Long updateId = listResponse.get(0).getId();
		String newContent = "new content " + System.currentTimeMillis();
		body = new Diary();
		body.setContent(newContent);
		commitResponse = controller.update(updateId, body);
		Assertions.assertTrue(commitResponse.getSuccess());
		Assertions.assertEquals(newContent, mapper.selectById(updateId).getContent());

		// delete
		Assertions.assertEquals(5, mapper.selectList(new QueryWrapper<>()).size());
		commitResponse = controller.delete(mapper.selectOne(
				new QueryWrapper<Diary>().eq("user_id", -1L)).getId());
		Assertions.assertTrue(commitResponse.getSuccess());
		Assertions.assertEquals(4, mapper.selectList(new QueryWrapper<>()).size());
	}

}
