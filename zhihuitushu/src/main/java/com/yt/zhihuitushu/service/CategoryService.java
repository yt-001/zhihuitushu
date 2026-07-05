package com.yt.zhihuitushu.service;

import com.yt.zhihuitushu.entity.Category;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface CategoryService extends IService<Category> {

    List<Category> listHomeCategories(Integer limit);
}
