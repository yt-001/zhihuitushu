package com.yt.zhihuitushu.service;

import com.yt.zhihuitushu.entity.Book;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface BookService extends IService<Book> {

    List<Book> listRecommended(Integer limit);

    List<Book> listHot(Integer limit);

    List<Book> listNew(Integer limit);
}
