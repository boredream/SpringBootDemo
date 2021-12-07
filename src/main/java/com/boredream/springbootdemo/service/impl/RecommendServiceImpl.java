package com.boredream.springbootdemo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.boredream.springbootdemo.entity.*;
import com.boredream.springbootdemo.mapper.*;
import com.boredream.springbootdemo.service.IRecommendService;
import com.boredream.springbootdemo.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.List;

/**
 * <p>
 * 推荐 服务实现类
 * </p>
 *
 * @author boredream
 * @since 2021-12-07
 */
@Service
@EnableTransactionManagement
public class RecommendServiceImpl implements IRecommendService {

    @Autowired
    RecommendTheDayMapper recommendTheDayMapper;
    @Autowired
    TheDayMapper theDayMapper;

    @Autowired
    RecommendTodoGroupMapper recommendTodoGroupMapper;
    @Autowired
    TodoGroupMapper todoGroupMapper;

    @Autowired
    RecommendTodoMapper recommendTodoMapper;
    @Autowired
    TodoMapper todoMapper;

    @Transactional
    @Override
    public void genRecommendData(Long userId) {
        // 从模板获取推荐数据，加到用户数据里
        List<RecommendTheDay> recommendTheDays = recommendTheDayMapper.selectList(new QueryWrapper<>());
        for (RecommendTheDay recommend : recommendTheDays) {
            TheDay day = new TheDay();
            day.setUserId(userId);
            day.setName(recommend.getName());
            day.setNotifyType(recommend.getNotifyType());
            day.setTheDayDate(recommend.getTheDayDate());

            // 特殊处理
            if ("情人节".equalsIgnoreCase(recommend.getName())) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.MONTH, 1);
                calendar.set(Calendar.DAY_OF_MONTH, 14);
                if (Calendar.getInstance().after(calendar)) {
                    // 如果今年情人节已经过了，延续到下一年
                    calendar.add(Calendar.YEAR, 1);
                }
                day.setTheDayDate(DateUtils.calendar2str(calendar));
                day.setNotifyType(TheDay.NOTIFY_TYPE_YEAR_COUNT_DOWN);
            }

            theDayMapper.insert(day);
        }

        List<RecommendTodoGroup> recommendTodoGroups = recommendTodoGroupMapper.selectList(new QueryWrapper<>());
        for (RecommendTodoGroup recommendGroup : recommendTodoGroups) {
            TodoGroup group = new TodoGroup();
            group.setUserId(userId);
            group.setName(recommendGroup.getName());
            todoGroupMapper.insert(group);

            List<RecommendTodo> recommendTodos = recommendTodoMapper.selectList(new QueryWrapper<RecommendTodo>().eq("todo_group_name", recommendGroup.getName()));
            for (RecommendTodo recommend : recommendTodos) {
                Todo todo = new Todo();
                todo.setUserId(userId);
                todo.setTodoGroupId(group.getId());
                todo.setName(recommend.getName());
                todo.setDetail(recommend.getDetail());
                todo.setDoneDate(recommend.getDoneDate());
                todo.setDone(recommend.getDone());
                todo.setImages(recommend.getImages());
                todoMapper.insert(todo);
            }
        }
    }

    @Transactional
    @Override
    public void mergeRecommendData(Long userId, Long cpUserId) {
        // TODO: chunyang 2021/12/7 检查cp关系

    }

    @Override
    public void splitRecommendData(Long userId, Long cpUserId) {
        // TODO: chunyang 2021/12/7 绑定后直接删除，解绑暂不处理任何逻辑。后续绑定应该改成暂时隐藏
    }
}

