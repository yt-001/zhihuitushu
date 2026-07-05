package com.yt.zhihuitushu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yt.zhihuitushu.entity.Banner;
import com.yt.zhihuitushu.mapper.BannerMapper;
import com.yt.zhihuitushu.service.BannerService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BannerServiceImpl extends ServiceImpl<BannerMapper, Banner> implements BannerService {

    @Override
    public List<Banner> listActiveBanners(Integer limit) {
        LambdaQueryWrapper<Banner> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Banner::getStatus, 1)
                .orderByAsc(Banner::getSortOrder)
                .last("LIMIT " + limit);
        return list(wrapper);
    }
}
