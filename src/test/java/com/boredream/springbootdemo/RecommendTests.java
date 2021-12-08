package com.boredream.springbootdemo;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.boredream.springbootdemo.auth.JwtUtil;
import com.boredream.springbootdemo.entity.TheDay;
import com.boredream.springbootdemo.entity.Todo;
import com.boredream.springbootdemo.entity.TodoGroup;
import com.boredream.springbootdemo.entity.User;
import com.boredream.springbootdemo.mapper.TheDayMapper;
import com.boredream.springbootdemo.mapper.TodoGroupMapper;
import com.boredream.springbootdemo.mapper.TodoMapper;
import com.boredream.springbootdemo.mapper.UserMapper;
import com.boredream.springbootdemo.service.IRecommendService;
import com.boredream.springbootdemo.service.IUserService;
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
@MapperScan("com.boredream.springbootdemo.userMapper")
class RecommendTests {

	@Autowired
	IUserService userService;

	@Autowired
	UserMapper userMapper;

	@Autowired
	IRecommendService service;

	@Autowired
	TheDayMapper theDayMapper;

	@Autowired
	TodoGroupMapper todoGroupMapper;

	@Autowired
	TodoMapper todoMapper;

	@Autowired
	JwtUtil jwtUtil;

	@BeforeAll
	void before() {
		// delete all
		User user = userMapper.selectOne(new QueryWrapper<User>().
				eq("username", "13013053605"));
		deleteUserData(user);

		user = userMapper.selectOne(new QueryWrapper<User>()
				.eq("username", "13013053600"));
		deleteUserData(user);
	}

	private void deleteUserData(User user) {
		if (user != null) {
			userMapper.delete(new QueryWrapper<User>().eq("id", user.getId()));
			theDayMapper.delete(new QueryWrapper<TheDay>().eq("user_id", user.getId()));
			todoGroupMapper.delete(new QueryWrapper<TodoGroup>().eq("user_id", user.getId()));
			todoMapper.delete(new QueryWrapper<Todo>().eq("user_id", user.getId()));
		}
	}

	@Test
	void test() {
		// register
		User user1 = new User();
		user1.setUsername("13013053605");
		user1.setPassword("$2a$10$1YaWvz3oJW2lMsEOZOj0ve4t8Qfn/uIXO6.NHzvM8vdCi7.wWo/ti");
		userMapper.insert(user1);

		User user2 = new User();
		user2.setUsername("13013053600");
		user2.setPassword("$2a$10$1YaWvz3oJW2lMsEOZOj0ve4t8Qfn/uIXO6.NHzvM8vdCi7.wWo/ti");
		userMapper.insert(user2);

		// 双方生成推荐内容
		service.genRecommendData(user1.getId());
		service.genRecommendData(user2.getId());
		assertEquals(user1.getId(), todoMapper, 30);
		assertEquals(user2.getId(), todoMapper,  30);

		// 绑定cp后双方合并数据，删除被绑定一方的推荐
		service.mergeRecommendData(user1.getId(), user2.getId());
		assertEquals(user1.getId(), todoMapper, 0);
		assertEquals(user2.getId(), todoMapper,  30);


	}

	private static <T> void assertEquals(Long userId, BaseMapper<T> mapper, int expectedSize) {
		List<T> list = mapper.selectList(new QueryWrapper<T>().eq("user_id", userId));
		Assertions.assertEquals(expectedSize, list.size());
	}

}
