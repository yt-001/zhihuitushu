package com.yt.zhihuitushu.service;

import com.yt.zhihuitushu.entity.Banner;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface BannerService extends IService<Banner> {

    List<Banner> listActiveBanners(Integer limit);
}
