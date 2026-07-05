package com.yt.zhihuitushu.service;

import com.yt.zhihuitushu.dto.request.AdminLoginDTO;
import com.yt.zhihuitushu.dto.response.AdminVO;
import com.yt.zhihuitushu.entity.Admin;

public interface AdminService {

    /**
     * 管理员登录验证
     */
    Admin login(AdminLoginDTO dto);

    /**
     * 根据用户名查询管理员
     */
    Admin findByUsername(String username);

    /**
     * 根据ID查询管理员信息
     */
    AdminVO getAdminInfo(Long id);
}