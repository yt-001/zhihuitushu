package com.yt.zhihuitushu.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yt.zhihuitushu.dto.request.AdminLoginDTO;
import com.yt.zhihuitushu.dto.response.AdminVO;
import com.yt.zhihuitushu.dto.response.ApiResponse;
import com.yt.zhihuitushu.entity.Admin;
import com.yt.zhihuitushu.entity.Book;
import com.yt.zhihuitushu.entity.LoginUser;
import com.yt.zhihuitushu.entity.User;
import com.yt.zhihuitushu.mapper.BookMapper;
import com.yt.zhihuitushu.mapper.UserMapper;
import com.yt.zhihuitushu.service.AdminService;
import com.yt.zhihuitushu.service.TokenService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Resource
    private AdminService adminService;

    @Resource
    private TokenService tokenService;

    @Resource
    private BookMapper bookMapper;

    @Resource
    private UserMapper userMapper;

    /**
     * 管理员登录
     */
    @PostMapping("/login")
    public ApiResponse<LoginUser> login(@Valid @RequestBody AdminLoginDTO dto, HttpServletResponse response) {
        Admin admin = adminService.login(dto);

        // 构建登录用户信息
        LoginUser loginUser = new LoginUser();
        loginUser.setId(admin.getId());
        loginUser.setUsername(admin.getUsername());
        loginUser.setAvatar(admin.getAvatar());
        loginUser.setRole(admin.getRole());

        // 创建token并设置cookie
        String token = tokenService.createToken(loginUser, false);
        tokenService.setTokenCookie(response, token, false);

        return ApiResponse.success("登录成功", loginUser);
    }

    /**
     * 获取管理员信息
     */
    @GetMapping("/info")
    public ApiResponse<AdminVO> getAdminInfo(@RequestAttribute(value = "loginUser", required = false) LoginUser loginUser) {
        if (loginUser == null) {
            return ApiResponse.error(401, "未登录");
        }

        AdminVO adminVO = adminService.getAdminInfo(loginUser.getId());
        return ApiResponse.success(adminVO);
    }

    /**
     * 管理员登出
     */
    @PostMapping("/logout")
    public ApiResponse<Void> logout(HttpServletResponse response) {
        tokenService.clearTokenCookie(response);
        return ApiResponse.success("登出成功", null);
    }

    // ==================== 用户管理 ====================

    /**
     * 获取用户列表
     */
    @GetMapping("/users")
    public ApiResponse<IPage<User>> getUserList(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Integer status) {

        Page<User> pageParam = new Page<>(page, size);
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();

        if (keyword != null && !keyword.trim().isEmpty()) {
            wrapper.like(User::getUsername, keyword).or().like(User::getEmail, keyword);
        }
        if (status != null) {
            wrapper.eq(User::getStatus, status);
        }
        wrapper.orderByDesc(User::getCreatedAt);

        IPage<User> userPage = userMapper.selectPage(pageParam, wrapper);
        return ApiResponse.success(userPage);
    }

    /**
     * 获取用户详情
     */
    @GetMapping("/users/{id}")
    public ApiResponse<User> getUserDetail(@PathVariable Long id) {
        User user = userMapper.selectById(id);
        if (user == null) {
            return ApiResponse.error(404, "用户不存在");
        }
        return ApiResponse.success(user);
    }

    /**
     * 更新用户状态
     */
    @PutMapping("/users/{id}/status")
    public ApiResponse<Void> updateUserStatus(@PathVariable Long id, @RequestBody Map<String, Integer> request) {
        Integer status = request.get("status");
        User user = userMapper.selectById(id);
        if (user == null) {
            return ApiResponse.error(404, "用户不存在");
        }
        user.setStatus(status);
        user.setUpdatedAt(LocalDateTime.now());
        userMapper.updateById(user);
        return ApiResponse.success("状态更新成功", null);
    }

    /**
     * 删除用户
     */
    @DeleteMapping("/users/{id}")
    public ApiResponse<Void> deleteUser(@PathVariable Long id) {
        userMapper.deleteById(id);
        return ApiResponse.success("删除成功", null);
    }

    // ==================== 数据统计 ====================

    /**
     * 获取统计数据概览
     */
    @GetMapping("/statistics")
    public ApiResponse<Map<String, Object>> getStatistics() {
        Map<String, Object> stats = new HashMap<>();

        // 图书总数
        Long totalBooks = bookMapper.selectCount(null);
        stats.put("totalBooks", totalBooks);

        // 用户总数
        Long totalUsers = userMapper.selectCount(null);
        stats.put("totalUsers", totalUsers);

        // 模拟其他统计数据
        stats.put("totalReads", 12580);
        stats.put("totalDownloads", 3560);
        stats.put("todayReads", 256);
        stats.put("todayDownloads", 48);

        return ApiResponse.success(stats);
    }

    /**
     * 获取阅读趋势数据
     */
    @GetMapping("/statistics/reading-trend")
    public ApiResponse<List<Map<String, Object>>> getReadingTrend(@RequestParam(defaultValue = "7") Integer days) {
        // 返回模拟数据
        List<Map<String, Object>> trend = List.of(
            Map.of("date", "2024-01-01", "reads", 120, "downloads", 30),
            Map.of("date", "2024-01-02", "reads", 135, "downloads", 35),
            Map.of("date", "2024-01-03", "reads", 150, "downloads", 42),
            Map.of("date", "2024-01-04", "reads", 128, "downloads", 38),
            Map.of("date", "2024-01-05", "reads", 165, "downloads", 45),
            Map.of("date", "2024-01-06", "reads", 180, "downloads", 52),
            Map.of("date", "2024-01-07", "reads", 195, "downloads", 58)
        );
        return ApiResponse.success(trend);
    }

    /**
     * 获取热门图书排行
     */
    @GetMapping("/statistics/popular-books")
    public ApiResponse<List<Book>> getPopularBooks(@RequestParam(defaultValue = "10") Integer limit) {
        LambdaQueryWrapper<Book> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByDesc(Book::getReadCount).last("LIMIT " + limit);
        List<Book> books = bookMapper.selectList(wrapper);
        return ApiResponse.success(books);
    }

    /**
     * 获取活跃用户排行
     */
    @GetMapping("/statistics/active-users")
    public ApiResponse<List<User>> getActiveUsers(@RequestParam(defaultValue = "10") Integer limit) {
        // 返回前N个用户（实际应该根据阅读量排序）
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getStatus, 1).last("LIMIT " + limit);
        List<User> users = userMapper.selectList(wrapper);
        return ApiResponse.success(users);
    }

    // ==================== 系统设置 ====================

    /**
     * 获取系统设置
     */
    @GetMapping("/settings")
    public ApiResponse<Map<String, Object>> getSystemSettings() {
        Map<String, Object> settings = new HashMap<>();
        settings.put("siteName", "智慧图书阅读平台");
        settings.put("siteDescription", "一个现代化的图书阅读平台");
        settings.put("allowRegister", true);
        settings.put("allowUpload", true);
        settings.put("maxFileSize", 50);
        settings.put("allowedFileTypes", "pdf,epub,txt");
        return ApiResponse.success(settings);
    }

    /**
     * 更新系统设置
     */
    @PutMapping("/settings")
    public ApiResponse<Map<String, Object>> updateSystemSettings(@RequestBody Map<String, Object> settings) {
        // 实际应该保存到数据库或配置文件
        return ApiResponse.success("设置更新成功", settings);
    }
}