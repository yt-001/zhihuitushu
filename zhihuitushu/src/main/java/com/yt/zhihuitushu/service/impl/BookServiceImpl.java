package com.yt.zhihuitushu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yt.zhihuitushu.entity.Book;
import com.yt.zhihuitushu.mapper.BookMapper;
import com.yt.zhihuitushu.service.BookService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookServiceImpl extends ServiceImpl<BookMapper, Book> implements BookService {

    @Override
    public List<Book> listRecommended(Integer limit) {
        LambdaQueryWrapper<Book> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Book::getStatus, 1)
                .orderByDesc(Book::getRating)
                .orderByDesc(Book::getReadCount)
                .last("LIMIT " + limit);
        return list(wrapper);
    }

    @Override
    public List<Book> listHot(Integer limit) {
        LambdaQueryWrapper<Book> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Book::getStatus, 1)
                .orderByDesc(Book::getReadCount)
                .last("LIMIT " + limit);
        return list(wrapper);
    }

    @Override
    public List<Book> listNew(Integer limit) {
        LambdaQueryWrapper<Book> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Book::getStatus, 1)
                .orderByDesc(Book::getPublishDate)
                .orderByDesc(Book::getCreatedAt)
                .last("LIMIT " + limit);
        return list(wrapper);
    }
}
