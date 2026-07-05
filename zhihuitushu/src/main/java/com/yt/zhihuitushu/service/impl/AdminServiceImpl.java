package com.yt.zhihuitushu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yt.zhihuitushu.dto.request.AdminLoginDTO;
import com.yt.zhihuitushu.dto.response.AdminVO;
import com.yt.zhihuitushu.entity.Admin;
import com.yt.zhihuitushu.mapper.AdminMapper;
import com.yt.zhihuitushu.service.AdminService;
import jakarta.annotation.Resource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AdminServiceImpl implements AdminService {

    @Resource
    private AdminMapper adminMapper;

    @Resource
    private PasswordEncoder passwordEncoder;

    @Override
    public Admin login(AdminLoginDTO dto) {
        Admin admin = findByUsername(dto.getUsername());

        if (admin == null) {
            throw new RuntimeException("用户名不存在");
        }

        if (admin.getStatus() == 0) {
            throw new RuntimeException("账号已被禁用");
        }

        if (!passwordEncoder.matches(dto.getPassword(), admin.getPassword())) {
            throw new RuntimeException("密码错误");
        }

        return admin;
    }

    @Override
    public Admin findByUsername(String username) {
        LambdaQueryWrapper<Admin> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Admin::getUsername, username);
        return adminMapper.selectOne(wrapper);
    }

    @Override
    public AdminVO getAdminInfo(Long id) {
        Admin admin = adminMapper.selectById(id);
        if (admin == null) {
            throw new RuntimeException("管理员不存在");
        }
        return convertToAdminVO(admin);
    }

    private AdminVO convertToAdminVO(Admin admin) {
        AdminVO vo = new AdminVO();
        vo.setId(admin.getId());
        vo.setUsername(admin.getUsername());
        vo.setRealName(admin.getRealName());
        vo.setPhone(admin.getPhone());
        vo.setEmail(admin.getEmail());
        vo.setAvatar(admin.getAvatar());
        vo.setRole(admin.getRole());
        vo.setStatus(admin.getStatus());
        return vo;
    }
}