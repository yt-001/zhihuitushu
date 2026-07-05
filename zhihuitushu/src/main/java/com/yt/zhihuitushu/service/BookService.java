package com.yt.zhihuitushu.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yt.zhihuitushu.dto.request.BookCreateDTO;
import com.yt.zhihuitushu.dto.request.BookQueryDTO;
import com.yt.zhihuitushu.dto.request.BookUpdateDTO;
import com.yt.zhihuitushu.dto.response.BookVO;
import com.yt.zhihuitushu.entity.Book;

import java.util.List;

public interface BookService extends IService<Book> {

    /**
     * 获取推荐书籍列表
     */
    List<Book> listRecommended(Integer limit);

    /**
     * 获取热门书籍列表
     */
    List<Book> listHot(Integer limit);

    /**
     * 获取新书列表
     */
    List<Book> listNew(Integer limit);

    /**
     * 创建书籍
     */
    BookVO createBook(BookCreateDTO dto);

    /**
     * 更新书籍
     */
    BookVO updateBook(Long id, BookUpdateDTO dto);

    /**
     * 删除书籍
     */
    void deleteBook(Long id);

    /**
     * 根据ID查询书籍详情
     */
    BookVO getBookById(Long id);

    /**
     * 分页查询书籍列表
     */
    IPage<BookVO> listBooks(BookQueryDTO queryDTO);

    /**
     * 批量删除书籍
     */
    void batchDeleteBooks(List<Long> ids);

    /**
     * 更新书籍状态
     */
    void updateBookStatus(Long id, Integer status);
}