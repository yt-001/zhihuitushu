package com.yt.zhihuitushu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yt.zhihuitushu.dto.request.BookCreateDTO;
import com.yt.zhihuitushu.dto.request.BookQueryDTO;
import com.yt.zhihuitushu.dto.request.BookUpdateDTO;
import com.yt.zhihuitushu.dto.response.BookVO;
import com.yt.zhihuitushu.entity.Book;
import com.yt.zhihuitushu.entity.Category;
import com.yt.zhihuitushu.mapper.BookMapper;
import com.yt.zhihuitushu.mapper.CategoryMapper;
import com.yt.zhihuitushu.service.BookService;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class BookServiceImpl extends ServiceImpl<BookMapper, Book> implements BookService {

    @Resource
    private BookMapper bookMapper;

    @Resource
    private CategoryMapper categoryMapper;

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

    @Override
    public BookVO createBook(BookCreateDTO dto) {
        Book book = new Book();
        BeanUtils.copyProperties(dto, book);
        book.setRating(0.0);
        book.setReviewCount(0);
        book.setReadCount(0);
        book.setDownloadCount(0);
        book.setCreatedAt(LocalDateTime.now());
        book.setUpdatedAt(LocalDateTime.now());
        bookMapper.insert(book);
        return convertToVO(book);
    }

    @Override
    public BookVO updateBook(Long id, BookUpdateDTO dto) {
        Book book = bookMapper.selectById(id);
        if (book == null) {
            throw new RuntimeException("书籍不存在");
        }
        // 只更新非空字段
        if (StringUtils.hasText(dto.getTitle())) {
            book.setTitle(dto.getTitle());
        }
        if (StringUtils.hasText(dto.getAuthor())) {
            book.setAuthor(dto.getAuthor());
        }
        if (StringUtils.hasText(dto.getCover())) {
            book.setCover(dto.getCover());
        }
        if (dto.getCategoryId() != null) {
            book.setCategoryId(dto.getCategoryId());
        }
        if (StringUtils.hasText(dto.getPublisher())) {
            book.setPublisher(dto.getPublisher());
        }
        if (dto.getPublishDate() != null) {
            book.setPublishDate(dto.getPublishDate());
        }
        if (StringUtils.hasText(dto.getLanguage())) {
            book.setLanguage(dto.getLanguage());
        }
        if (dto.getPages() != null) {
            book.setPages(dto.getPages());
        }
        if (StringUtils.hasText(dto.getIsbn())) {
            book.setIsbn(dto.getIsbn());
        }
        if (StringUtils.hasText(dto.getDescription())) {
            book.setDescription(dto.getDescription());
        }
        if (dto.getStatus() != null) {
            book.setStatus(dto.getStatus());
        }
        book.setUpdatedAt(LocalDateTime.now());
        bookMapper.updateById(book);
        return convertToVO(book);
    }

    @Override
    public void deleteBook(Long id) {
        Book book = bookMapper.selectById(id);
        if (book == null) {
            throw new RuntimeException("书籍不存在");
        }
        bookMapper.deleteById(id);
    }

    @Override
    public BookVO getBookById(Long id) {
        Book book = bookMapper.selectById(id);
        if (book == null) {
            return null;
        }
        return convertToVO(book);
    }

    @Override
    public IPage<BookVO> listBooks(BookQueryDTO queryDTO) {
        Page<Book> pageParam = new Page<>(queryDTO.getPage(), queryDTO.getSize());
        LambdaQueryWrapper<Book> wrapper = new LambdaQueryWrapper<>();

        // 关键词搜索（标题或作者）
        if (StringUtils.hasText(queryDTO.getKeyword())) {
            wrapper.and(w -> w.like(Book::getTitle, queryDTO.getKeyword())
                    .or()
                    .like(Book::getAuthor, queryDTO.getKeyword()));
        }
        // 分类筛选
        if (queryDTO.getCategoryId() != null) {
            wrapper.eq(Book::getCategoryId, queryDTO.getCategoryId());
        }
        // 状态筛选
        if (queryDTO.getStatus() != null) {
            wrapper.eq(Book::getStatus, queryDTO.getStatus());
        }
        // 语言筛选
        if (StringUtils.hasText(queryDTO.getLanguage())) {
            wrapper.eq(Book::getLanguage, queryDTO.getLanguage());
        }
        // 出版社筛选
        if (StringUtils.hasText(queryDTO.getPublisher())) {
            wrapper.eq(Book::getPublisher, queryDTO.getPublisher());
        }

        // 排序
        String sortBy = queryDTO.getSortBy();
        String sortOrder = queryDTO.getSortOrder();
        if (StringUtils.hasText(sortBy)) {
            boolean isAsc = "asc".equalsIgnoreCase(sortOrder);
            switch (sortBy) {
                case "createdAt":
                    wrapper.orderBy(true, isAsc, Book::getCreatedAt);
                    break;
                case "readCount":
                    wrapper.orderBy(true, isAsc, Book::getReadCount);
                    break;
                case "rating":
                    wrapper.orderBy(true, isAsc, Book::getRating);
                    break;
                case "publishDate":
                    wrapper.orderBy(true, isAsc, Book::getPublishDate);
                    break;
                default:
                    wrapper.orderByDesc(Book::getCreatedAt);
            }
        } else {
            wrapper.orderByDesc(Book::getCreatedAt);
        }

        IPage<Book> bookPage = bookMapper.selectPage(pageParam, wrapper);

        // 转换为VO分页对象
        IPage<BookVO> voPage = bookPage.convert(this::convertToVO);
        return voPage;
    }

    @Override
    public void batchDeleteBooks(List<Long> ids) {
        if (ids != null && !ids.isEmpty()) {
            bookMapper.deleteBatchIds(ids);
        }
    }

    @Override
    public void updateBookStatus(Long id, Integer status) {
        Book book = bookMapper.selectById(id);
        if (book == null) {
            throw new RuntimeException("书籍不存在");
        }
        book.setStatus(status);
        book.setUpdatedAt(LocalDateTime.now());
        bookMapper.updateById(book);
    }

    /**
     * 将Book实体转换为BookVO
     */
    private BookVO convertToVO(Book book) {
        BookVO vo = new BookVO();
        BeanUtils.copyProperties(book, vo);
        // 查询分类名称
        if (book.getCategoryId() != null) {
            Category category = categoryMapper.selectById(book.getCategoryId());
            if (category != null) {
                vo.setCategoryName(category.getName());
            }
        }
        return vo;
    }
}