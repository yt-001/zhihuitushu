package com.yt.zhihuitushu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yt.zhihuitushu.dto.request.LoginDTO;
import com.yt.zhihuitushu.dto.request.RegisterDTO;
import com.yt.zhihuitushu.dto.request.ResetPasswordDTO;
import com.yt.zhihuitushu.entity.LoginUser;
import com.yt.zhihuitushu.entity.User;
import com.yt.zhihuitushu.mapper.UserMapper;
import com.yt.zhihuitushu.service.UserService;
import com.yt.zhihuitushu.service.VerifyCodeService;
import jakarta.annotation.Resource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.regex.Pattern;

@Service
public class UserServiceImpl implements UserService {

    @Resource
    private UserMapper userMapper;

    @Resource
    private PasswordEncoder passwordEncoder;

    @Resource
    private VerifyCodeService verifyCodeService;

    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");
    private static final Pattern PHONE_PATTERN = Pattern.compile("^1[3-9]\\d{9}$");
    private static final int MAX_FAILED_ATTEMPTS = 3;
    private static final int LOCK_DURATION_MINUTES = 5;

    @Override
    public User findByAccount(String account) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        if (isEmail(account)) {
            wrapper.eq(User::getEmail, account);
        } else if (isPhone(account)) {
            wrapper.eq(User::getPhone, account);
        } else {
            wrapper.eq(User::getUsername, account);
        }
        return userMapper.selectOne(wrapper);
    }

    @Override
    public boolean existsByAccount(String account) {
        return findByAccount(account) != null;
    }

    @Override
    @Transactional
    public LoginUser register(RegisterDTO dto) {
        if (!dto.getPassword().equals(dto.getConfirmPassword())) {
            throw new RuntimeException("两次输入的密码不一致");
        }

        if (!verifyCodeService.verifyCode(dto.getAccount(), dto.getCode())) {
            throw new RuntimeException("验证码错误");
        }

        if (existsByAccount(dto.getAccount())) {
            throw new RuntimeException("该账号已注册，请直接登录");
        }

        User user = new User();
        user.setUsername(dto.getAccount());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));

        if (isEmail(dto.getAccount())) {
            user.setEmail(dto.getAccount());
        } else if (isPhone(dto.getAccount())) {
            user.setPhone(dto.getAccount());
        }

        user.setRole("user");
        user.setStatus(1);
        user.setFailedAttempts(0);
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());

        userMapper.insert(user);
        return convertToLoginUser(user);
    }

    @Override
    @Transactional
    public LoginUser login(LoginDTO dto) {
        User user = findByAccount(dto.getAccount());

        if (user == null) {
            throw new RuntimeException("账号不存在");
        }

        if (user.getStatus() == 0) {
            throw new RuntimeException("账号已被禁用");
        }

        if (isLocked(user)) {
            throw new RuntimeException("账号已被锁定，请5分钟后再试");
        }

        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            handleLoginFailure(user);
            int remainingAttempts = MAX_FAILED_ATTEMPTS - user.getFailedAttempts();
            if (remainingAttempts > 0) {
                throw new RuntimeException("密码错误，还剩" + remainingAttempts + "次机会");
            } else {
                throw new RuntimeException("账号已被锁定，请5分钟后再试");
            }
        }

        userMapper.resetFailedAttempts(user.getId());
        return convertToLoginUser(user);
    }

    @Override
    @Transactional
    public void resetPassword(ResetPasswordDTO dto) {
        User user = findByAccount(dto.getAccount());

        if (user == null) {
            throw new RuntimeException("账号不存在");
        }

        if (!verifyCodeService.verifyCode(dto.getAccount(), dto.getCode())) {
            throw new RuntimeException("验证码错误");
        }

        if (passwordEncoder.matches(dto.getNewPassword(), user.getPassword())) {
            throw new RuntimeException("新密码不能与原密码相同");
        }

        user.setPassword(passwordEncoder.encode(dto.getNewPassword()));
        user.setUpdatedAt(LocalDateTime.now());
        userMapper.updateById(user);
    }

    private void handleLoginFailure(User user) {
        int newAttempts = user.getFailedAttempts() + 1;
        user.setFailedAttempts(newAttempts);

        if (newAttempts >= MAX_FAILED_ATTEMPTS) {
            user.setLockedAt(LocalDateTime.now());
        }

        user.setUpdatedAt(LocalDateTime.now());
        userMapper.updateById(user);
    }

    private boolean isLocked(User user) {
        if (user.getLockedAt() == null) {
            return false;
        }
        return user.getLockedAt().plusMinutes(LOCK_DURATION_MINUTES).isAfter(LocalDateTime.now());
    }

    private LoginUser convertToLoginUser(User user) {
        LoginUser loginUser = new LoginUser();
        loginUser.setId(user.getId());
        loginUser.setUsername(user.getUsername());
        loginUser.setAvatar(user.getAvatar());
        loginUser.setRole(user.getRole());
        return loginUser;
    }

    private boolean isEmail(String account) {
        return EMAIL_PATTERN.matcher(account).matches();
    }

    private boolean isPhone(String account) {
        return PHONE_PATTERN.matcher(account).matches();
    }
}