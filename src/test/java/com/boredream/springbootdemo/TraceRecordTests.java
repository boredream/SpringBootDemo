package com.boredream.springbootdemo;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.boredream.springbootdemo.controller.TraceLocationController;
import com.boredream.springbootdemo.controller.TraceRecordController;
import com.boredream.springbootdemo.entity.TraceRecord;
import com.boredream.springbootdemo.entity.dto.PageResultDTO;
import com.boredream.springbootdemo.entity.dto.ResponseDTO;
import com.boredream.springbootdemo.entity.dto.TraceRecordQueryDTO;
import com.boredream.springbootdemo.mapper.TraceLocationMapper;
import com.boredream.springbootdemo.mapper.TraceRecordMapper;
import com.boredream.springbootdemo.mapper.UserMapper;
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
class TraceRecordTests {

	@Autowired
	UserMapper userMapper;

	@Autowired
	TraceRecordMapper groupMapper;

	@Autowired
	TraceRecordController groupController;

	@Autowired
	TraceLocationController controller;

	@Autowired
	TraceLocationMapper mapper;

	private Long curUserId = 1L;

	@BeforeAll
	void before() {
		// delete all
		groupMapper.delete(new QueryWrapper<>());
		mapper.delete(new QueryWrapper<>());
	}

	@Test
	void testRecordSort() {
		// add group
		for (int i = 1; i < 5; i++) {
			TraceRecord group = new TraceRecord();
			group.setUserId(curUserId);
			group.setName("轨迹A");
			group.setStartTime(1000L * i);
			group.setEndTime(2000L + group.getStartTime());
			group.setDistance(100);
			ResponseDTO<Boolean> commitResponse = groupController.add(group, curUserId);
			Assertions.assertTrue(commitResponse.getSuccess());
		}

		TraceRecordQueryDTO queryDTO = new TraceRecordQueryDTO();
		queryDTO.setPage(1);
		queryDTO.setSize(10);
		ResponseDTO<PageResultDTO<TraceRecord>> queryResponse = groupController.queryByPage(queryDTO, curUserId);
		List<TraceRecord> records = queryResponse.getData().getRecords();
		Assertions.assertTrue(records.get(0).getStartTime() > records.get(1).getStartTime());
	}

	@Test
	void testAddRecord() {
		// add group
		TraceRecord group = new TraceRecord();
		group.setName("轨迹A");
		group.setTraceListStr("1670649408256,31.211536660702205,121.35730474509023_1670649410261,31.21151721209348,121.35738285946206_1670649413005,31.21152318708789,121.35740809024271_1670649415011,31.21149942949412,121.35744620438744");
		ResponseDTO<Boolean> commitResponse = groupController.add(group, curUserId);
		Assertions.assertTrue(commitResponse.getSuccess());

		TraceRecordQueryDTO queryDTO = new TraceRecordQueryDTO();
		queryDTO.setPage(1);
		queryDTO.setSize(10);
		ResponseDTO<PageResultDTO<TraceRecord>> queryResponse = groupController.queryByPage(queryDTO, curUserId);
		Assertions.assertTrue(queryResponse.getData().getRecords().size() > 0);
	}

}
