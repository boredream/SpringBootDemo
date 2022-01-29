package com.boredream.springbootdemo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.boredream.springbootdemo.auth.JwtUtil;
import com.boredream.springbootdemo.entity.User;
import com.boredream.springbootdemo.entity.dto.LoginRequestDTO;
import com.boredream.springbootdemo.entity.dto.VerifyCodeDTO;
import com.boredream.springbootdemo.entity.dto.WxLoginDTO;
import com.boredream.springbootdemo.entity.dto.WxSessionDTO;
import com.boredream.springbootdemo.exception.ApiException;
import com.boredream.springbootdemo.mapper.UserMapper;
import com.boredream.springbootdemo.service.IUserService;
import com.boredream.springbootdemo.service.IVerifyCodeService;
import com.boredream.springbootdemo.service.IWxService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

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

    @Autowired
    private IVerifyCodeService verifyCodeService;

    @Autowired
    private IWxService wxService;

    @Autowired
    public UserServiceImpl(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    private User getUserByUsername(String username) {
        QueryWrapper<User> wrapper = new QueryWrapper<User>().eq("username", username);
        return getOne(wrapper);
    }

    public String register(LoginRequestDTO request) {
        User oldUser = getUserByUsername(request.getUsername());
        if (oldUser != null) {
            throw new ApiException("用户已存在");
        }

        User user = new User();
        user.setNickname("昵称");
        user.setUsername(request.getUsername());
        if (request.getPassword() != null) {
            user.setPassword(passwordEncoder.encode(request.getPassword()));
        }
        save(user);

        return jwtUtil.generateToken(user);
    }

    @Override
    public Boolean setPassword(Long curUserId, String password) {
        User user = getBaseMapper().selectById(curUserId);
        user.setPassword(passwordEncoder.encode(password));
        return updateById(user);
    }

    public String login(LoginRequestDTO request) {
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
        try {
            WxSessionDTO session = wxService.wxLogin(dto);
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
    public String loginWithVerifyCode(VerifyCodeDTO dto) {
        // 使用验证码注册或登录用户
        boolean success = verifyCodeService.checkVerifyCode(dto.getPhone(), dto.getCode());
        if (!success) {
            throw new ApiException("短信验证码错误");
        }

        // 短信验证通过后，判断是登录还是注册
        User user = getUserByUsername(dto.getPhone());
        LoginRequestDTO request = new LoginRequestDTO();
        request.setUsername(dto.getPhone());
        if (user == null) {
            // 注册
            return register(request);
        } else {
            // 登录，验证码登录，无需验证密码
            return jwtUtil.generateToken(user);
        }
    }

    @Override
    public User getUserInfo(Long curUserId) {
        User user = getById(curUserId);
        if (user.getCpUserId() != null) {
            User cpUser = getBaseMapper().selectById(user.getCpUserId());
            cpUser.setPassword(null);
            user.setCpUser(cpUser);
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
