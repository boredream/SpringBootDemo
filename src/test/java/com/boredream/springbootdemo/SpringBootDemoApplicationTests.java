package com.boredream.springbootdemo;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.boredream.springbootdemo.controller.DiaryController;
import com.boredream.springbootdemo.entity.Diary;
import com.boredream.springbootdemo.entity.dto.DiaryQueryDTO;
import com.boredream.springbootdemo.entity.dto.PageResultDTO;
import com.boredream.springbootdemo.entity.dto.ResponseDTO;
import com.boredream.springbootdemo.mapper.DiaryMapper;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@MapperScan("com.boredream.springbootdemo.mapper")
class SpringBootDemoApplicationTests {

	@Autowired
	DiaryController diaryController;

	@Autowired
	DiaryMapper diaryMapper;

	private Long curUserId = 1L;
	private Long cpUserId = 2L;

	@BeforeAll
	void before() {
		// delete all
		diaryMapper.delete(new QueryWrapper<>());
	}

	@Test
	void testDiary() {
		// add
		Diary body = new Diary();
		body.setContent("content " + System.currentTimeMillis());
		body.setDiaryDate("2021-12-21");
		ResponseDTO<Boolean> commitResponse = diaryController.add(body, curUserId);
		Assertions.assertTrue(commitResponse.getSuccess());

		body = new Diary();
		body.setContent("content " + System.currentTimeMillis());
		body.setDiaryDate("2021-02-05");
		commitResponse = diaryController.add(body, curUserId);
		Assertions.assertTrue(commitResponse.getSuccess());

		body = new Diary();
		body.setContent("content " + System.currentTimeMillis());
		body.setDiaryDate("2021-02-05");
		commitResponse = diaryController.add(body, cpUserId);
		Assertions.assertTrue(commitResponse.getSuccess());

		body = new Diary();
		body.setContent("content " + System.currentTimeMillis());
		body.setDiaryDate("2021-02-14");
		commitResponse = diaryController.add(body, cpUserId);
		Assertions.assertTrue(commitResponse.getSuccess());

		// query by pages
		DiaryQueryDTO dto = new DiaryQueryDTO();
		dto.setPage(1);
		dto.setSize(20);
		PageResultDTO<Diary> pageResponse = diaryController.queryByPage(dto, curUserId).getData();
		Assertions.assertEquals(4, pageResponse.getRecords().size());
		Assertions.assertEquals("2021-02-14", pageResponse.getRecords().get(1).getDiaryDate());

		// query by month
		dto = new DiaryQueryDTO();
		dto.setQueryDate("2021-12");
		List<Diary> listResponse = diaryController.queryByMonth(dto, curUserId).getData();
		Assertions.assertEquals(1, listResponse.size());

		// update
		Long updateId = listResponse.get(0).getId();
		String newContent = "new content " + System.currentTimeMillis();
		body = new Diary();
		body.setContent(newContent);
		commitResponse = diaryController.update(updateId, body);
		Assertions.assertTrue(commitResponse.getSuccess());
		Assertions.assertEquals(newContent, diaryMapper.selectById(updateId).getContent());

		// delete
		commitResponse = diaryController.delete(pageResponse.getRecords().get(1).getId());
		Assertions.assertTrue(commitResponse.getSuccess());
		Assertions.assertEquals(3, diaryMapper.selectList(new QueryWrapper<>()).size());
	}

}
