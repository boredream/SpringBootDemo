package com.boredream.springbootdemo;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.boredream.springbootdemo.controller.TheDayController;
import com.boredream.springbootdemo.entity.TheDay;
import com.boredream.springbootdemo.entity.dto.PageResultDTO;
import com.boredream.springbootdemo.entity.dto.ResponseDTO;
import com.boredream.springbootdemo.entity.dto.TheDayQueryDTO;
import com.boredream.springbootdemo.mapper.TheDayMapper;
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
class TheDayTests {

	@Autowired
	TheDayController controller;

	@Autowired
	TheDayMapper mapper;

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
		TheDay body = new TheDay();
		body.setName("在一起");
		body.setTheDayDate("2021-02-05");
		body.setNotifyType(TheDay.NOTIFY_TYPE_TOTAL_COUNT);
		ResponseDTO<Boolean> commitResponse = controller.add(body, curUserId);
		Assertions.assertTrue(commitResponse.getSuccess());

		body = new TheDay();
		body.setName("第一次牵手");
		body.setTheDayDate("2021-03-04");
		body.setNotifyType(TheDay.NOTIFY_TYPE_TOTAL_COUNT);
		commitResponse = controller.add(body, curUserId);
		Assertions.assertTrue(commitResponse.getSuccess());

		body = new TheDay();
		body.setName("仙女的生日");
		body.setTheDayDate("2021-02-14");
		body.setNotifyType(TheDay.NOTIFY_TYPE_YEAR_COUNT_DOWN);
		commitResponse = controller.add(body, cpUserId);
		Assertions.assertTrue(commitResponse.getSuccess());

		body = new TheDay();
		body.setName("二狗子的生日");
		body.setTheDayDate("2021-12-21");
		body.setNotifyType(TheDay.NOTIFY_TYPE_YEAR_COUNT_DOWN);
		commitResponse = controller.add(body, curUserId);
		Assertions.assertTrue(commitResponse.getSuccess());

		body = new TheDay();
		body.setName("结婚");
		commitResponse = controller.add(body, curUserId);
		Assertions.assertTrue(commitResponse.getSuccess());

		body = new TheDay();
		body.setName("XXX");
		commitResponse = controller.add(body, -1L);
		Assertions.assertTrue(commitResponse.getSuccess());

		// query by pages
		TheDayQueryDTO dto = new TheDayQueryDTO();
		dto.setPage(1);
		dto.setSize(20);
		PageResultDTO<TheDay> pageResponse = controller.queryByPage(dto, curUserId).getData();
		Assertions.assertEquals(5, pageResponse.getRecords().size());

		// update
		Long updateId = mapper.selectOne(new QueryWrapper<TheDay>().eq("name", "结婚")).getId();
		String newDate = "2022-05-01";
		body = new TheDay();
		body.setTheDayDate(newDate);
		commitResponse = controller.update(updateId, body, curUserId);
		Assertions.assertTrue(commitResponse.getSuccess());
		Assertions.assertEquals(newDate, mapper.selectById(updateId).getTheDayDate());

		// delete
		commitResponse = controller.delete(pageResponse.getRecords().get(3).getId());
		Assertions.assertTrue(commitResponse.getSuccess());
		pageResponse = controller.queryByPage(dto, curUserId).getData();
		Assertions.assertEquals(4, pageResponse.getRecords().size());
	}

}
