package com.boredream.springbootdemo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.boredream.springbootdemo.auth.JwtUtil;
import com.boredream.springbootdemo.entity.LoginRequest;
import com.boredream.springbootdemo.entity.User;
import com.boredream.springbootdemo.entity.dto.WxLoginDTO;
import com.boredream.springbootdemo.entity.dto.WxSessionDTO;
import com.boredream.springbootdemo.exception.ApiException;
import com.boredream.springbootdemo.mapper.TheDayMapper;
import com.boredream.springbootdemo.mapper.TodoGroupMapper;
import com.boredream.springbootdemo.mapper.TodoMapper;
import com.boredream.springbootdemo.mapper.UserMapper;
import com.boredream.springbootdemo.service.IUserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 用户 服务实现类
 * </p>
 *
 * @author boredream
 * @since 2021-09-18
 */
@Service
@EnableTransactionManagement
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    private JwtUtil jwtUtil;

    @Autowired
    PasswordEncoder passwordEncoder;

//    @Autowired
//    RecommendTheDayMapper recommendTheDayMapper;
    @Autowired
    TheDayMapper theDayMapper;

    @Autowired
    TodoGroupMapper todoGroupMapper;

    @Autowired
    TodoMapper todoMapper;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    public UserServiceImpl(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    private User getUserByUsername(String username) {
        QueryWrapper<User> wrapper = new QueryWrapper<User>().eq("username", username);
        return getOne(wrapper);
    }

    public String register(LoginRequest request) {
        User oldUser = getUserByUsername(request.getUsername());
        if (oldUser != null) {
            throw new ApiException("用户已存在");
        }

        User user = new User();
        user.setNickname("小公主/骑士"); // TODO: chunyang 2021/11/24 默认昵称？
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        save(user);

        // 默认生成推荐内容
//        List<RecommendTheDay> recommendTheDays = recommendTheDayMapper.selectList(new QueryWrapper<>());
//        for (RecommendTheDay recommend : recommendTheDays) {
//            // 自动新建一系列推荐数据
//            TheDay day = new TheDay();
//            day.setUserId(user.getId());
//            day.setName(recommend.getName());
//            day.setNotifyType(recommend.getNotifyType());
//            day.setTheDayDate(recommend.getTheDayDate());
//
//            // 特殊处理
//            if("情人节".equalsIgnoreCase(recommend.getName())) {
//                Calendar calendar = Calendar.getInstance();
//                calendar.set(Calendar.MONTH, 2);
//                calendar.set(Calendar.DAY_OF_MONTH, 14);
//                if(Calendar.getInstance().after(calendar)) {
//                    // 如果今年情人节已经过了，延续到下一年
//                    calendar.add(Calendar.YEAR, 1);
//                }
//                day.setTheDayDate(DateUtils.calendar2str(calendar));
//                day.setNotifyType(TheDay.NOTIFY_TYPE_YEAR_COUNT_DOWN);
//            }
//
//            theDayMapper.insert(day);
//        }

        return jwtUtil.generateToken(user);
    }

    public String login(LoginRequest request) {
        User user = getUserByUsername(request.getUsername());
        if (user == null) {
            throw new ApiException("用户不存在");
        }

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new ApiException("密码不正确");
        }

        return jwtUtil.generateToken(user);
    }

    public String wxLogin(WxLoginDTO dto) {
        // TODO: chunyang 2021/10/28 使用配置
        String url = "https://api.weixin.qq.com/sns/jscode2session?appid={appid}&secret={secret}&js_code={code}&grant_type=authorization_code";
        Map<String, String> requestMap = new HashMap<>();
        requestMap.put("appid", "wx0896ca1ddd64114e");
        requestMap.put("secret", "068602332cdecb1a9a5ec5c4b00b16f8");
        requestMap.put("code", dto.getCode());

        String json = restTemplate.getForObject(url, String.class, requestMap);
        try {
            WxSessionDTO session = new ObjectMapper().readValue(json, WxSessionDTO.class);
            QueryWrapper<User> wrapper = new QueryWrapper<User>().eq("open_id", session.getOpenid());
            User user = getOne(wrapper);

            if (user == null) {
                // 自动新建账号
                user = new User();
                user.setUsername(session.getOpenid());
                user.setOpenId(session.getOpenid());
                // TODO: chunyang 2021/11/25 从微信取默认用户信息
                user.setNickname("用户" + session.getOpenid().substring(0, 8));
                save(user);
            }
            return jwtUtil.generateToken(user);
        } catch (Exception e) {
            throw new ApiException("微信登录失败 " + e.getMessage());
        }
    }

    @Override
    public User getUserInfo(Long curUserId) {
        User user = getById(curUserId);
        if (user.getCpUserId() != null) {
            user.setCpUser(getBaseMapper().selectById(user.getCpUserId()));
        }
        user.setPassword(null);
        return user;
    }

    @Transactional
    @Override
    public User bindCp(Long curUserId, Long cpUserId) {
        User curUser = getBaseMapper().selectById(curUserId);
        User cpUser = getBaseMapper().selectById(cpUserId);

        if (cpUser == null) {
            throw new ApiException("目标绑定用户不存在");
        }

        // 先判断是否已经各自有cp
        if (curUser.getCpUserId() != null) {
            throw new ApiException("无法绑定。您已经绑定过伴侣了，请先解绑后再重新尝试");
        }
        if (cpUser.getCpUserId() != null) {
            throw new ApiException("无法绑定。对方已经绑定过伴侣了");
        }

        // 自己绑cp
        curUser.setCpUserId(cpUserId);
        boolean curBindSuccess = updateById(curUser);

        // cp绑自己
        cpUser.setCpUserId(curUserId);
        boolean cpBindSuccess = updateById(cpUser);

        cpUser.setPassword(null);
        return cpUser;
    }

    @Override
    public boolean unbindCp(Long curUserId, Long cpUserId) {
        User curUser = getBaseMapper().selectById(curUserId);
        User cpUser = getBaseMapper().selectById(cpUserId);

        if (cpUser == null) {
            throw new ApiException("目标绑定用户不存在");
        }

        // 自己解绑
        curUser.setCpUserId(null);
        LambdaUpdateWrapper<User> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.set(User::getCpUserId, null).eq(User::getId, curUserId);
        boolean curBindSuccess = update(updateWrapper);

        // cp解绑
        cpUser.setCpUserId(null);
        updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.set(User::getCpUserId, null).eq(User::getId, cpUserId);
        boolean cpBindSuccess = update(updateWrapper);

        return curBindSuccess && cpBindSuccess;
    }
}
