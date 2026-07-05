package com.yt.zhihuitushu.controller;

import com.yt.zhihuitushu.dto.response.ApiResponse;
import com.yt.zhihuitushu.entity.Banner;
import com.yt.zhihuitushu.entity.Category;
import com.yt.zhihuitushu.service.BannerService;
import com.yt.zhihuitushu.service.CategoryService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/home")
public class HomeController {

    @Resource
    private BannerService bannerService;

    @Resource
    private CategoryService categoryService;

    @GetMapping("/banners")
    public ApiResponse<List<Banner>> listBanners(
            @RequestParam(value = "limit", defaultValue = "5") Integer limit) {
        List<Banner> banners = bannerService.listActiveBanners(limit);
        return ApiResponse.success(banners);
    }

    @GetMapping("/categories")
    public ApiResponse<List<Category>> listCategories(
            @RequestParam(value = "limit", defaultValue = "10") Integer limit) {
        List<Category> categories = categoryService.listHomeCategories(limit);
        return ApiResponse.success(categories);
    }
}
